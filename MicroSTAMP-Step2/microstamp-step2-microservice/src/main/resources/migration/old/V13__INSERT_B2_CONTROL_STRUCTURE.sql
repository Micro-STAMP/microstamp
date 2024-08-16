INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",62,1,0,1,"Command Authority",NULL,2);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",63,1,0,1,"Operators",NULL,2);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",64,1,0,1,"Early Warning System",NULL,2);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Sensor",65,1,0,1,"Radar",NULL,2);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",66,1,0,1,"Fire Control",NULL,2);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",67,1,0,1,"Laucher",NULL,2);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",68,1,0,1,"Interceptor Simulator",NULL,2);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",69,1,0,1,"Lauch Station",NULL,2);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",70,1,0,1,"Flight Computer",NULL,2);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",71,1,0,1,"Interceptor Hardware",NULL,2);

INSERT INTO `connections` VALUES (93,0,1,62,63,2);
INSERT INTO `connections` VALUES (94,1,1,63,62,2);
INSERT INTO `connections` VALUES (95,0,1,63,66,2);
INSERT INTO `connections` VALUES (96,1,1,66,63,2);
INSERT INTO `connections` VALUES (97,0,1,68,69,2);
INSERT INTO `connections` VALUES (98,1,1,69,68,2);
INSERT INTO `connections` VALUES (99,0,1,64,66,2);
INSERT INTO `connections` VALUES (100,1,1,66,64,2);
INSERT INTO `connections` VALUES (101,0,1,65,66,2);
INSERT INTO `connections` VALUES (102,1,1,66,65,2);
INSERT INTO `connections` VALUES (103,0,1,67,69,2);
INSERT INTO `connections` VALUES (104,1,1,69,67,2);
INSERT INTO `connections` VALUES (105,0,1,66,69,2);
INSERT INTO `connections` VALUES (106,1,1,69,66,2);
INSERT INTO `connections` VALUES (107,0,1,69,70,2);
INSERT INTO `connections` VALUES (108,1,1,70,69,2);
INSERT INTO `connections` VALUES (109,0,1,70,71,2);
INSERT INTO `connections` VALUES (110,1,1,71,70,2);
INSERT INTO `connections` VALUES (111,1,1,71,69,2);


INSERT INTO `labels` VALUES (166,"Doctrine",93);
INSERT INTO `labels` VALUES (167,"Engagement Criteria",93);
INSERT INTO `labels` VALUES (168,"Training",93);
INSERT INTO `labels` VALUES (169,"TTP",93);
INSERT INTO `labels` VALUES (170,"Workarounds",93);
INSERT INTO `labels` VALUES (171,"Exercise Results",94);
INSERT INTO `labels` VALUES (172,"Readiness",94);
INSERT INTO `labels` VALUES (173,"Status",94);
INSERT INTO `labels` VALUES (174,"Wargame Results",94);
INSERT INTO `labels` VALUES (175,"Engage Target",95);
INSERT INTO `labels` VALUES (176,"Operational Mode Change",95);
INSERT INTO `labels` VALUES (177,"Readiness State Change",95);
INSERT INTO `labels` VALUES (178,"Weapons Free",95);
INSERT INTO `labels` VALUES (179,"Weapons Hold",95);
INSERT INTO `labels` VALUES (180,"Operational Mode",96);
INSERT INTO `labels` VALUES (181,"Readiness State",96);
INSERT INTO `labels` VALUES (182,"System Status",96);
INSERT INTO `labels` VALUES (183,"Track Data",96);
INSERT INTO `labels` VALUES (184,"Weapon and System Status",96);
INSERT INTO `labels` VALUES (185,"Acknowledgements",97);
INSERT INTO `labels` VALUES (186,"BIT results",97);
INSERT INTO `labels` VALUES (187,"Health and Status",97);
INSERT INTO `labels` VALUES (188,"Abort",98);
INSERT INTO `labels` VALUES (189,"Arm",98);
INSERT INTO `labels` VALUES (190,"BIT command",98);
INSERT INTO `labels` VALUES (191,"Task Load",98);
INSERT INTO `labels` VALUES (192,"Launch",98);
INSERT INTO `labels` VALUES (193,"Operating Mode",98);
INSERT INTO `labels` VALUES (194,"Power",98);
INSERT INTO `labels` VALUES (195,"Safe",98);
INSERT INTO `labels` VALUES (196,"Software Updates",98);
INSERT INTO `labels` VALUES (197,"Launch Report",99);
INSERT INTO `labels` VALUES (198,"Status Report",99);
INSERT INTO `labels` VALUES (199,"Heartbeat",99);
INSERT INTO `labels` VALUES (200,"Status Request",100);
INSERT INTO `labels` VALUES (201,"Status",101);
INSERT INTO `labels` VALUES (202,"Track Data",101);
INSERT INTO `labels` VALUES (203,"Radar Tasking",102);
INSERT INTO `labels` VALUES (204,"Readiness Mode Change",102);
INSERT INTO `labels` VALUES (205,"Status Request",102);
INSERT INTO `labels` VALUES (206,"BIT Results",103);
INSERT INTO `labels` VALUES (207,"Launcher Position",103);
INSERT INTO `labels` VALUES (208,"Launcher Position",104);
INSERT INTO `labels` VALUES (209,"Stow Position",104);
INSERT INTO `labels` VALUES (210,"Perform BIT",104);
INSERT INTO `labels` VALUES (211,"Fire Disable",105);
INSERT INTO `labels` VALUES (212,"Fire Enable",105);
INSERT INTO `labels` VALUES (213,"Operational Mode Change",105);
INSERT INTO `labels` VALUES (214,"Readiness State Change",105);
INSERT INTO `labels` VALUES (215,"Interceptor Tasking",105);
INSERT INTO `labels` VALUES (216,"Task Cancellation",105);
INSERT INTO `labels` VALUES (217,"Command Responses",106);
INSERT INTO `labels` VALUES (218,"System Status",106);
INSERT INTO `labels` VALUES (219,"Launch Report",106);
INSERT INTO `labels` VALUES (220,"Abort",107);
INSERT INTO `labels` VALUES (221,"Arm",107);
INSERT INTO `labels` VALUES (222,"BIT Command",107);
INSERT INTO `labels` VALUES (223,"Task Load",107);
INSERT INTO `labels` VALUES (224,"Launch",107);
INSERT INTO `labels` VALUES (225,"Operating Mode",107);
INSERT INTO `labels` VALUES (226,"Power",107);
INSERT INTO `labels` VALUES (227,"Safe",107);
INSERT INTO `labels` VALUES (228,"Software Updates",107);
INSERT INTO `labels` VALUES (229,"Acknowledgements",108);
INSERT INTO `labels` VALUES (230,"BIT results",108);
INSERT INTO `labels` VALUES (231,"Health and Status",108);
INSERT INTO `labels` VALUES (232,"Arm",109);
INSERT INTO `labels` VALUES (233,"Safe",109);
INSERT INTO `labels` VALUES (234,"Ignite",109);
INSERT INTO `labels` VALUES (235,"BIT Info",110);
INSERT INTO `labels` VALUES (236,"Safe and Arm Status",110);
INSERT INTO `labels` VALUES (237,"Break wires",111);
INSERT INTO `labels` VALUES (238,"Safe and Arm Status",111);
INSERT INTO `labels` VALUES (239,"Voltages",111);