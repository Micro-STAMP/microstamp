-- UCA code
ALTER TABLE step3.unsafe_control_action
    DROP INDEX uca_code;

ALTER TABLE step3.unsafe_control_action
    DROP INDEX UK_ctcngvi3mrrcp8s7davum0ie9;

ALTER TABLE step3.unsafe_control_action
    MODIFY COLUMN uca_code VARCHAR(20) NOT NULL;

CREATE UNIQUE INDEX ux_uca_analysis_code
    ON step3.unsafe_control_action (analysis_id, uca_code);

-- safety constraint code
ALTER TABLE step3.safety_constraint
    DROP INDEX UK_m5e8afrirdw6q4742jhce2l6r;

ALTER TABLE step3.safety_constraint
    DROP INDEX safety_constraint_code;

ALTER TABLE step3.safety_constraint
    MODIFY COLUMN safety_constraint_code VARCHAR(20) NOT NULL;
