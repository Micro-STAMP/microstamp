
CREATE TABLE hazard (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE safety_constraint (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE context (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE variable (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE value (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    variable_id BIGINT NOT NULL,
    FOREIGN KEY (variable_id) REFERENCES variable(id)
);

CREATE TABLE controller (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE control_action (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    controller_id BIGINT,
    FOREIGN KEY (controller_id) REFERENCES controller(id)
);

CREATE TABLE unsafe_control_action (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    control_action_id BIGINT,
    context_id BIGINT,
    constraint_id BIGINT,
    hazard_id BIGINT,
    type VARCHAR(50) NOT NULL,
    FOREIGN KEY (control_action_id) REFERENCES control_action(id),
    FOREIGN KEY (context_id) REFERENCES context(id),
    FOREIGN KEY (constraint_id) REFERENCES safety_constraint(id),
    FOREIGN KEY (hazard_id) REFERENCES hazard(id),
    FOREIGN KEY (controller_id) REFERENCES controller(id)
);

-- DROP TABLE IF EXISTS hazard;
-- DROP TABLE IF EXISTS safety_constraint;
-- DROP TABLE IF EXISTS context;
-- DROP TABLE IF EXISTS variable;
-- DROP TABLE IF EXISTS value;
-- DROP TABLE IF EXISTS controller;
-- DROP TABLE IF EXISTS control_action;
-- DROP TABLE IF EXISTS unsafe_control_action;