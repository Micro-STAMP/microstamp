INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",98,1,0,1,"Control Algorithm",NULL,9);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",99,1,0,1,"Process Model",98,9);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",100,1,0,1,"",NULL,9);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Actuator",101,1,0,1,"Inadequate operation",NULL,9);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Sensor",102,1,0,1,"Inadequate operation",NULL,9);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",103,1,0,1,"",NULL,9);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",104,1,0,1,"components failures/Changes over time",NULL,9);

INSERT INTO `connections` VALUES (171,3,1,9,98,9);
INSERT INTO `connections` VALUES (172,0,1,98,100,9);
INSERT INTO `connections` VALUES (173,1,1,100,98,9);
INSERT INTO `connections` VALUES (174,0,1,98,101,9);
INSERT INTO `connections` VALUES (175,1,1,102,98,9);
INSERT INTO `connections` VALUES (176,0,1,101,104,9);
INSERT INTO `connections` VALUES (177,1,1,104,102,9);
INSERT INTO `connections` VALUES (178,0,1,103,104,9);

INSERT INTO `connections` VALUES (179,3,1,9,104,9);
INSERT INTO `connections` VALUES (180,5,1,9,104,9);
INSERT INTO `connections` VALUES (181,4,1,104,9,9);


INSERT INTO `labels` VALUES (276,"Control input or external information wrong or missing",171);
INSERT INTO `labels` VALUES (277,"Missing or wrong communication with another components",172);
INSERT INTO `labels` VALUES (278,"Missing or wrong communication with another components",173);
INSERT INTO `labels` VALUES (279,"Provided Control Action(inappropriate, ineffective or missing)",174);
INSERT INTO `labels` VALUES (280,"Received Feedback(inappropriate, missing or delayed)",175);
INSERT INTO `labels` VALUES (281,"Received Control Action(Delayed, etc.)",176);
INSERT INTO `labels` VALUES (282,"Provided Feedback(Incorrect, no information provided, measurement inaccuracies, delays)",177);
INSERT INTO `labels` VALUES (283,"Conflicting control actions",178);
INSERT INTO `labels` VALUES (284,"Process input missing or wrong",179);
INSERT INTO `labels` VALUES (285,"Process output contributes to system hazard",181);
INSERT INTO `labels` VALUES (286,"Unidentified or out-of-range disturbance",180);