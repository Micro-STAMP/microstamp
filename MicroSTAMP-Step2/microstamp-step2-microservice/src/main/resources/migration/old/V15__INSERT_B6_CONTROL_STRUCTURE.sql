INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",23,1,0,1,"Driver",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",24,0,0,1,"Brake Pedal",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",25,0,0,1,"Multi-function Switch",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",26,0,0,1,"Instrument Cluster",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",27,0,0,1,"Accelerator Pedal",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",28,1,0,1,"Adaptive Cruise Control(ACC) Module",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",29,1,0,1,"Brake Control Module",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Sensor",30,0,0,1,"Radar",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",31,1,0,1,"Powertrain Control Module",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",32,0,0,1,"Service Brakes",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Sensor",33,0,0,1,"Whell Speed Sensor",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",34,0,0,1,"Electronic Throttle Body",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",35,1,0,1,"Vehicle",NULL,6);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",36,1,0,1,"Lead Vehicle",NULL,6);

INSERT INTO `connections` VALUES (25,0,1,23,24,6);
INSERT INTO `connections` VALUES (26,0,1,24,29,6);
INSERT INTO `connections` VALUES (27,0,1,29,32,6);
INSERT INTO `connections` VALUES (28,0,1,32,35,6);
INSERT INTO `connections` VALUES (29,0,1,23,26,6);
INSERT INTO `connections` VALUES (30,0,1,26,28,6);
INSERT INTO `connections` VALUES (31,1,1,29,28,6);
INSERT INTO `connections` VALUES (32,1,1,33,29,6);
INSERT INTO `connections` VALUES (33,1,1,35,33,6);
INSERT INTO `connections` VALUES (34,1,1,26,23,6);
INSERT INTO `connections` VALUES (35,1,1,28,26,6);
INSERT INTO `connections` VALUES (36,1,1,30,28,6);
INSERT INTO `connections` VALUES (37,1,1,28,31,6);
INSERT INTO `connections` VALUES (38,1,1,23,27,6);
INSERT INTO `connections` VALUES (39,1,1,27,31,6);
INSERT INTO `connections` VALUES (40,1,1,31,34,6);
INSERT INTO `connections` VALUES (41,0,1,34,31,6);
INSERT INTO `connections` VALUES (42,1,1,34,35,6);
INSERT INTO `connections` VALUES (43,2,0,36,30,6);


INSERT INTO `labels` VALUES (66,"Brake Cmd",25);
INSERT INTO `labels` VALUES (67,"Braking Signal",26);
INSERT INTO `labels` VALUES (68,"Brake Cmd",27);
INSERT INTO `labels` VALUES (69,"Friction",28);
INSERT INTO `labels` VALUES (70,"On",29);
INSERT INTO `labels` VALUES (71,"Off",29);
INSERT INTO `labels` VALUES (72,"Set",29);
INSERT INTO `labels` VALUES (73,"Cancel",29);
INSERT INTO `labels` VALUES (74,"Inc",29);
INSERT INTO `labels` VALUES (75,"Dec",29);
INSERT INTO `labels` VALUES (76,"On",30);
INSERT INTO `labels` VALUES (77,"Off",30);
INSERT INTO `labels` VALUES (78,"Set",30);
INSERT INTO `labels` VALUES (79,"Cancel",30);
INSERT INTO `labels` VALUES (80,"Inc",30);
INSERT INTO `labels` VALUES (81,"Dec",30);
INSERT INTO `labels` VALUES (82,"Braking status",31);
INSERT INTO `labels` VALUES (83,"Vehicle speed",31);
INSERT INTO `labels` VALUES (84,"Wheel Speed",32);
INSERT INTO `labels` VALUES (85,"Wheel Speed",33);
INSERT INTO `labels` VALUES (86,"ACC On",34);
INSERT INTO `labels` VALUES (87,"ACC Off",34);
INSERT INTO `labels` VALUES (88,"ACC Canceled",34);
INSERT INTO `labels` VALUES (89,"ACC Active",34);
INSERT INTO `labels` VALUES (90,"ACC On",35);
INSERT INTO `labels` VALUES (91,"ACC Off",35);
INSERT INTO `labels` VALUES (92,"ACC Canceled",35);
INSERT INTO `labels` VALUES (93,"ACC Active",35);
INSERT INTO `labels` VALUES (94,"Distance to lead vehicle",36);
INSERT INTO `labels` VALUES (95,"Acceleration Signal",37);
INSERT INTO `labels` VALUES (96,"Accelerate Cmd",38);
INSERT INTO `labels` VALUES (97,"Acceleration Signal",39);
INSERT INTO `labels` VALUES (98,"Throttle opening",40);
INSERT INTO `labels` VALUES (99,"Throttle position",41);
INSERT INTO `labels` VALUES (100,"Friction",42);
INSERT INTO `labels` VALUES (101,"Distance to lead vehicle",43);