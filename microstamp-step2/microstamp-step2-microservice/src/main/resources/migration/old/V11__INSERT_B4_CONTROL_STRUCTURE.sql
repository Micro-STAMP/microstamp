INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",37,1,0,1,"Treatment Definition - D0",NULL,4);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",38,1,0,1,"Treatment Delivery - D1",NULL,4);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",39,1,0,1,"PROSCAN Design Team",38,4);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",40,1,0,1,"Operations Management",38,4);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",41,1,0,1,"Maintenance",38,4);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",42,1,0,1,"Operators",38,4);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",43,1,0,1,"Medical Team",38,4);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",44,1,0,1,"PROSCAN facility(physical actuators and sensors, automated controllers)",38,4);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",45,1,0,1,"Patient",NULL,4);

INSERT INTO `connections` VALUES (44,0,1,37,39,4);
INSERT INTO `connections` VALUES (45,0,1,39,44,4);
INSERT INTO `connections` VALUES (46,0,1,44,45,4);
INSERT INTO `connections` VALUES (47,1,1,40,39,4);
INSERT INTO `connections` VALUES (48,0,1,39,40,4);
INSERT INTO `connections` VALUES (49,0,1,37,40,4);
INSERT INTO `connections` VALUES (50,0,1,40,41,4);
INSERT INTO `connections` VALUES (51,1,1,41,40,4);
INSERT INTO `connections` VALUES (52,0,1,41,44,4);
INSERT INTO `connections` VALUES (53,1,1,44,41,4);
INSERT INTO `connections` VALUES (54,0,1,40,42,4);
INSERT INTO `connections` VALUES (55,1,1,42,40,4);
INSERT INTO `connections` VALUES (56,0,1,42,44,4);
INSERT INTO `connections` VALUES (57,1,1,44,42,4);
INSERT INTO `connections` VALUES (58,1,1,40,37,4);
INSERT INTO `connections` VALUES (59,1,1,43,42,4);
INSERT INTO `connections` VALUES (60,0,1,40,43,4);
INSERT INTO `connections` VALUES (61,1,1,43,40,4);
INSERT INTO `connections` VALUES (62,0,1,43,44,4);
INSERT INTO `connections` VALUES (63,1,1,44,43,4);
INSERT INTO `connections` VALUES (64,0,1,43,45,4);
INSERT INTO `connections` VALUES (65,1,1,45,43,4);
INSERT INTO `connections` VALUES (66,1,1,45,44,4);
INSERT INTO `connections` VALUES (67,1,1,45,37,4);


INSERT INTO `labels` VALUES (102,"Capability upgrade request",44);
INSERT INTO `labels` VALUES (103,"Software revisions",45);
INSERT INTO `labels` VALUES (104,"Hardware modifications",45);
INSERT INTO `labels` VALUES (105,"Patient Position",46);
INSERT INTO `labels` VALUES (106,"Beam Creation and Delivery",46);
INSERT INTO `labels` VALUES (107,"Problem reports",47);
INSERT INTO `labels` VALUES (108,"Incidents",47);
INSERT INTO `labels` VALUES (109,"Change requests",47);
INSERT INTO `labels` VALUES (110,"Performance audits",47);
INSERT INTO `labels` VALUES (111,"Revised operating procedures",48);
INSERT INTO `labels` VALUES (112,"Treatment specifications (fraction definition, patient positioning information, beam characteristics)",49);
INSERT INTO `labels` VALUES (113,"Work orders",50);
INSERT INTO `labels` VALUES (114,"Resources",50);
INSERT INTO `labels` VALUES (115,"Problem reports",51);
INSERT INTO `labels` VALUES (116,"Change requests",51);
INSERT INTO `labels` VALUES (117,"Hardware replacements",52);
INSERT INTO `labels` VALUES (118,"Test results",53);
INSERT INTO `labels` VALUES (119,"Procedures",54);
INSERT INTO `labels` VALUES (120,"Problems reports",55);
INSERT INTO `labels` VALUES (121,"Change requests",55);
INSERT INTO `labels` VALUES (122,"Start treatment",56);
INSERT INTO `labels` VALUES (123,"Interrupt treatment",56);
INSERT INTO `labels` VALUES (124,"QA result",57);
INSERT INTO `labels` VALUES (125,"Sensor information",57);
INSERT INTO `labels` VALUES (126,"QA result",58);
INSERT INTO `labels` VALUES (127,"Room clear",59);
INSERT INTO `labels` VALUES (128,"Procedures",60);
INSERT INTO `labels` VALUES (129,"Problems reports",61);
INSERT INTO `labels` VALUES (130,"Change requests",61);
INSERT INTO `labels` VALUES (131,"Patient position",62);
INSERT INTO `labels` VALUES (132,"Interrupt treatment",62);
INSERT INTO `labels` VALUES (133,"Position",63);
INSERT INTO `labels` VALUES (134,"Movement",63);
INSERT INTO `labels` VALUES (135,"Patient position",64);
INSERT INTO `labels` VALUES (136,"Patient well being",65);
INSERT INTO `labels` VALUES (137,"Patient physiognomy changes",65);
INSERT INTO `labels` VALUES (138,"Panic button",66);
INSERT INTO `labels` VALUES (139,"(delayed) Cure evaluation",67);