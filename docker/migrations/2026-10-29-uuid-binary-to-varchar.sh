set -euo pipefail

DB_HOST="${DB_HOST:-127.0.0.1}"
DB_PORT="${DB_PORT:-3306}"
DB_USER="${DB_USER:-root}"
DB_PASS="${DB_PASS:-123456}"
SCHEMAS="${SCHEMAS:-cast_step3 cast_step4 cast_step5 step2}"

export MYSQL_PWD="$DB_PASS"
MYSQL=(mysql -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" --batch --skip-column-names)

schema_list=$(printf "'%s'," $SCHEMAS)
schema_list="${schema_list%,}"

columns=$("${MYSQL[@]}" -e "
    SELECT table_schema, table_name, column_name, is_nullable
    FROM information_schema.columns
    WHERE column_type = 'binary(16)' AND table_schema IN ($schema_list)
    ORDER BY table_schema, table_name, column_name;")

if [ -z "$columns" ]; then
    echo "Nenhuma coluna binary(16) encontrada em: $SCHEMAS. Nada a fazer."
    exit 0
fi

echo "Colunas que serao convertidas para varchar(36):"
echo "$columns" | awk '{ printf "  %s.%s.%s\n", $1, $2, $3 }'

# 1) Backup antes de qualquer alteracao
existing=$("${MYSQL[@]}" -e "SELECT GROUP_CONCAT(schema_name SEPARATOR ' ') FROM information_schema.schemata WHERE schema_name IN ($schema_list);")
backup="backup-uuid-migration-$(date +%Y%m%d-%H%M%S).sql"
mysqldump -h "$DB_HOST" -P "$DB_PORT" -u "$DB_USER" --single-transaction --databases $existing > "$backup"
echo "Backup salvo em: $(pwd)/$backup"

# 2) O MySQL nao permite mudar o tipo de colunas que participam de FK: guarda as FKs envolvidas,
#    remove, converte e recria com o mesmo nome e as mesmas regras.
fks=$("${MYSQL[@]}" -e "
    SELECT k.table_schema, k.table_name, k.constraint_name, k.column_name,
           k.referenced_table_schema, k.referenced_table_name, k.referenced_column_name,
           r.update_rule, r.delete_rule
    FROM information_schema.key_column_usage k
    JOIN information_schema.referential_constraints r
      ON r.constraint_schema = k.constraint_schema AND r.constraint_name = k.constraint_name
    JOIN information_schema.columns c
      ON (c.table_schema = k.table_schema AND c.table_name = k.table_name AND c.column_name = k.column_name)
      OR (c.table_schema = k.referenced_table_schema AND c.table_name = k.referenced_table_name AND c.column_name = k.referenced_column_name)
    WHERE k.referenced_table_name IS NOT NULL
      AND c.column_type = 'binary(16)' AND c.table_schema IN ($schema_list)
    GROUP BY k.table_schema, k.table_name, k.constraint_name, k.column_name,
             k.referenced_table_schema, k.referenced_table_name, k.referenced_column_name,
             r.update_rule, r.delete_rule;")

sql="SET FOREIGN_KEY_CHECKS = 0;"
while IFS=$'\t' read -r schema table name column ref_schema ref_table ref_column on_update on_delete; do
    [ -z "$name" ] && continue
    sql+="
ALTER TABLE \`$schema\`.\`$table\` DROP FOREIGN KEY \`$name\`;"
done <<< "$fks"

# 3) Conversao: binary(16) -> varbinary(36) mantem os 16 bytes; BIN_TO_UUID grava o texto; depois vira varchar(36).
while read -r schema table column nullable; do
    null_clause=$([ "$nullable" = "NO" ] && echo "NOT NULL" || echo "NULL")
    sql+="
ALTER TABLE \`$schema\`.\`$table\` MODIFY \`$column\` VARBINARY(36) $null_clause;
UPDATE \`$schema\`.\`$table\` SET \`$column\` = BIN_TO_UUID(\`$column\`) WHERE \`$column\` IS NOT NULL;
ALTER TABLE \`$schema\`.\`$table\` MODIFY \`$column\` VARCHAR(36) $null_clause;"
done <<< "$columns"

while IFS=$'\t' read -r schema table name column ref_schema ref_table ref_column on_update on_delete; do
    [ -z "$name" ] && continue
    sql+="
ALTER TABLE \`$schema\`.\`$table\` ADD CONSTRAINT \`$name\` FOREIGN KEY (\`$column\`)
    REFERENCES \`$ref_schema\`.\`$ref_table\` (\`$ref_column\`) ON UPDATE $on_update ON DELETE $on_delete;"
done <<< "$fks"
sql+="
SET FOREIGN_KEY_CHECKS = 1;"

"${MYSQL[@]}" -e "$sql"

remaining=$("${MYSQL[@]}" -e "SELECT COUNT(*) FROM information_schema.columns WHERE column_type = 'binary(16)' AND table_schema IN ($schema_list);")
echo "Conversao concluida. Colunas binary(16) restantes: $remaining"

# 4) Aviso: a CAC ganhou UNIQUE(code, analysis_id). Codigos repetidos impedem o Hibernate de criar a restricao.
if "${MYSQL[@]}" -e "SELECT 1 FROM information_schema.tables WHERE table_schema='cast_step3' AND table_name='control_action_contributions'" | grep -q 1; then
    duplicates=$("${MYSQL[@]}" -e "
        SELECT analysis_id, code, COUNT(*) FROM cast_step3.control_action_contributions
        GROUP BY analysis_id, code HAVING COUNT(*) > 1;")
    if [ -n "$duplicates" ]; then
        echo "ATENCAO: CACs com codigo repetido na mesma analise (renomeie antes de subir o cast-step3):"
        echo "$duplicates"
    fi
fi
