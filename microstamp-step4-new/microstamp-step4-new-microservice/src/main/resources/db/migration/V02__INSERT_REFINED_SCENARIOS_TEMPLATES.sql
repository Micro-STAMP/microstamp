INSERT INTO refined_scenarios_common_causes (id, code, common_cause) VALUES
 ('3d7e9b92-1f5f-4e4e-9cbe-ae9c2e1a1a01', 'A', 'Responsibilities (e.g., desired end states) that would produce this UCA'),
 ('6a9f1e04-5a37-4d75-8b5b-7e1f314c2b02', 'B', 'Control algorithms or decision-making rationale that would explain the UCA'),
 ('d1c2f3a4-b5c6-4789-9a0b-1c2d3e4f5a03', 'C', 'Process models that would explain this UCA'),
 ('9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04', 'D', 'Interpretation or process model updates that would explain the UCA'),
 ('0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05', 'E', 'Internal controller states/modes that would explain the UCA (failure, lame duck mode, etc.)'),
 ('aabbccdd-eeff-1122-3344-55667788990f', 'F', 'Controller inputs (control actions, feedback, or other inputs) that would explain the UCA');


INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde001', '3d7e9b92-1f5f-4e4e-9cbe-ae9c2e1a1a01', '<controller> by design is responsible for always assigning ___ to every ___ (even if ___.).', NULL);

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde002', '6a9f1e04-5a37-4d75-8b5b-7e1f314c2b02', '<controller> control algorithm is designed to trigger <control-action> during a fallback to ___ strategy, which can happen if ___', NULL);

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde003', '6a9f1e04-5a37-4d75-8b5b-7e1f314c2b02', 'If ___, then <controller> may select <control-action> to ___', NULL);

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde004', 'd1c2f3a4-b5c6-4789-9a0b-1c2d3e4f5a03', '<controller> incorrectly believes ___', NULL);

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde005', 'd1c2f3a4-b5c6-4789-9a0b-1c2d3e4f5a03', '<controller> believes ___', NULL);

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde006', '9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04', 'PM-1 is not updated when ___', 'NOT_PROVIDED');

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde007', '9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04', 'PM-1 is the initial (default) belief before feedback/input ___ received', 'NOT_PROVIDED');

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde008', '9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04', 'PM-1 is updated incorrectly due to feedback/input ___ that indicates ___', 'PROVIDED');

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde009', '9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04', 'PM-1 is updated too late due to ___', 'TOO_LATE');

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde00a', '9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04', 'PM-1 is updated too early due to ___', 'TOO_EARLY');

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde00b', '9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04', 'PM-1 stops updating too soon before ___', 'STOPPED_TOO_SOON');

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde00c', '9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04', 'PM-1 continues to be updated too long after ___', 'APPLIED_TOO_LONG');

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde00d', '0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05', 'If <controller> is in ___ mode/state, it will continue to <control-action> using alternate input ___.', NULL);

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde00e', '0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05', 'If ___ is disabled, then <controller> can ___.', NULL);

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde00f', 'aabbccdd-eeff-1122-3344-55667788990f', '<controller> does not receive <other feedback> when <other context>', NULL);

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde010', 'aabbccdd-eeff-1122-3344-55667788990f', '<controller> does not receive <other input/feedback> when <other context>', NULL);

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde011', 'aabbccdd-eeff-1122-3344-55667788990f', '<controller> does not prevent <control-action> when alternate input ___ is received.', NULL);

INSERT INTO refined_scenarios_templates (id, id_common_causes, template, unsafe_control_action_type) VALUES
 ('0f1e2d3c-4b5a-6978-8899-aabbccdde012', 'aabbccdd-eeff-1122-3344-55667788990f', 'Although <Input> is correct, the feedback from ___ may be incorrect and may cause <controller> to ___.', NULL);