-- ALTER TABLE unsafe_control_action ADD COLUMN uca_code VARCHAR(20);

WITH sequence AS (
    SELECT id, ROW_NUMBER() OVER () AS number FROM step3.unsafe_control_action uca
)
UPDATE unsafe_control_action uca JOIN sequence seq ON uca.id = seq.id
SET uca.uca_code = CONCAT('UCA-', seq.number)
WHERE uca.id = seq.id;

ALTER TABLE unsafe_control_action MODIFY COLUMN uca_code VARCHAR(20) NOT NULL UNIQUE;