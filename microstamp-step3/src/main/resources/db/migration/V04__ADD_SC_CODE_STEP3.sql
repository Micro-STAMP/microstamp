WITH sequence AS (
    SELECT id as uca_id, CAST(REGEXP_REPLACE(uca.uca_code, '[^0-9]', '') AS UNSIGNED) AS number FROM step3.unsafe_control_action uca
)
UPDATE safety_constraint sc JOIN sequence seq ON sc.uca_id = seq.uca_id
SET sc.safety_constraint_code = CONCAT('SC-', seq.number)
WHERE sc.uca_id = seq.uca_id;

ALTER TABLE safety_constraint MODIFY COLUMN safety_constraint_code VARCHAR(20) NOT NULL UNIQUE;