INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",13,1,0,1,"Driver",NULL,7);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",14,1,0,1,"Auto-Hold Module",NULL,7);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",15,0,1,1,"Physical Vehicle",NULL,7);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",16,1,0,1,"Braking System",15,7);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",17,1,0,1,"Propulsion System",15,7);


INSERT INTO `connections` VALUES (6,3,1,7,15,7);
INSERT INTO `connections` VALUES (7,4,1,15,7,7);
INSERT INTO `connections` VALUES (8,3,1,7,13,7);
INSERT INTO `connections` VALUES (9,1,1,14,13,7);
INSERT INTO `connections` VALUES (10,0,1,13,17,7);
INSERT INTO `connections` VALUES (11,1,1,15,13,7);
INSERT INTO `connections` VALUES (12,0,1,14,16,7);
INSERT INTO `connections` VALUES (13,1,1,16,14,7);
INSERT INTO `connections` VALUES (14,1,1,17,14,7);
INSERT INTO `connections` VALUES (15,1,1,15,14,7);
INSERT INTO `connections` VALUES (16,0,1,13,15,7);
INSERT INTO `connections` VALUES (17,1,1,15,13,7);
INSERT INTO `connections` VALUES (18,0,1,13,14,7);


INSERT INTO `labels` VALUES (16,"Visual cues",8);
INSERT INTO `labels` VALUES (17,"Physical feedback",8);
INSERT INTO `labels` VALUES (18,"AH Enable",9);
INSERT INTO `labels` VALUES (19,"AH Disabled",9);
INSERT INTO `labels` VALUES (20,"Accelerate",10);
INSERT INTO `labels` VALUES (21,"Shift",10);
INSERT INTO `labels` VALUES (22,"Vehicle speed",11);
INSERT INTO `labels` VALUES (23,"Visual feedback",11);
INSERT INTO `labels` VALUES (24,"Hold",12);
INSERT INTO `labels` VALUES (25,"Release",12);
INSERT INTO `labels` VALUES (26,"Additional Pressure",12);
INSERT INTO `labels` VALUES (27,"Wheel speed",13);
INSERT INTO `labels` VALUES (28,"Accel pos.",14);
INSERT INTO `labels` VALUES (29,"PRNDL",14);
INSERT INTO `labels` VALUES (30,"Driver presence",15);
INSERT INTO `labels` VALUES (31,"Inclination",15);
INSERT INTO `labels` VALUES (32,"Brake",16);
INSERT INTO `labels` VALUES (33,"Pedal response",17);
INSERT INTO `labels` VALUES (34,"Enable AH",18);
INSERT INTO `labels` VALUES (35,"Disable AH",18);
INSERT INTO `labels` VALUES (36,"Brake pedal on",18);
INSERT INTO `labels` VALUES (37,"Brake pedal off",18);