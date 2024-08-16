INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",105,1,0,1,"Controller(s)",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",106,1,0,1,"Human Controller",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",107,1,0,1,"Model of Other Controllers",106,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",108,1,0,1,"Model of Enviroment",106,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",109,1,0,1,"Model of Automation",106,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",110,1,0,1,"Model of Controlled Process",106,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",111,1,0,1,"Control Action Generation/ Mental Processing",106,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",112,1,0,1,"Other Controllers/Systems",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",113,1,0,1,"Controls",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Sensor",114,1,0,1,"Sensors",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",115,1,0,1,"Displays, alarms, other sensory feedback",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",116,1,0,1,"Automated Controller",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Sensor",117,1,0,1,"Model of Human Controller",116,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",118,1,0,1,"Operational Mode",116,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",119,1,0,1,"Model of Other Controllers",116,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",120,1,0,1,"Control Algorithm",116,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",121,1,0,1,"Model of Controlled Process",116,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",122,1,0,1,"Other Controllers/Systems",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Actuator",123,1,0,1,"Actuators",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Sensor",124,1,0,1,"Sensors",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",125,1,0,1,"Other Controllers/Systems",NULL,10);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",126,1,0,1,"Controlled Process",NULL,10);

INSERT INTO `connections` VALUES (182,3,1,18,106,10);
INSERT INTO `connections` VALUES (183,0,1,105,106,10);
INSERT INTO `connections` VALUES (184,1,1,106,112,10);
INSERT INTO `connections` VALUES (185,0,1,112,106,10);
INSERT INTO `connections` VALUES (186,3,1,18,106,10);
INSERT INTO `connections` VALUES (187,0,1,107,111,10);
INSERT INTO `connections` VALUES (188,1,1,111,107,10);
INSERT INTO `connections` VALUES (189,0,1,108,111,10);
INSERT INTO `connections` VALUES (190,1,1,111,108,10);
INSERT INTO `connections` VALUES (191,0,1,109,111,10);
INSERT INTO `connections` VALUES (192,1,1,111,109,10);
INSERT INTO `connections` VALUES (193,0,1,110,111,10);
INSERT INTO `connections` VALUES (194,1,1,111,110,10);
INSERT INTO `connections` VALUES (195,0,1,111,113,10);
INSERT INTO `connections` VALUES (196,2,0,113,111,10);
INSERT INTO `connections` VALUES (197,0,1,111,114,10);
INSERT INTO `connections` VALUES (198,2,0,114,111,10);
INSERT INTO `connections` VALUES (199,2,0,111,123,10);
INSERT INTO `connections` VALUES (200,2,0,123,111,10);
INSERT INTO `connections` VALUES (201,2,0,123,113,10);
INSERT INTO `connections` VALUES (202,0,1,113,116,10);
INSERT INTO `connections` VALUES (203,1,1,116,113,10);
INSERT INTO `connections` VALUES (204,0,1,114,116,10);
INSERT INTO `connections` VALUES (205,0,1,106,115,10);
INSERT INTO `connections` VALUES (206,1,1,115,106,10);
INSERT INTO `connections` VALUES (207,0,1,116,115,10);
INSERT INTO `connections` VALUES (208,3,1,18,116,10);
INSERT INTO `connections` VALUES (209,0,1,112,116,10);
INSERT INTO `connections` VALUES (210,1,1,116,112,10);
INSERT INTO `connections` VALUES (211,0,1,117,120,10);
INSERT INTO `connections` VALUES (212,1,1,120,117,10);
INSERT INTO `connections` VALUES (213,0,1,118,120,10);
INSERT INTO `connections` VALUES (214,1,1,120,118,10);
INSERT INTO `connections` VALUES (215,0,1,119,120,10);
INSERT INTO `connections` VALUES (216,1,1,120,119,10);
INSERT INTO `connections` VALUES (217,0,1,120,121,10);
INSERT INTO `connections` VALUES (218,1,1,121,120,10);
INSERT INTO `connections` VALUES (219,0,1,120,123,10);
INSERT INTO `connections` VALUES (220,1,1,123,120,10);
INSERT INTO `connections` VALUES (221,1,1,124,120,10);
INSERT INTO `connections` VALUES (222,2,0,124,115,10);
INSERT INTO `connections` VALUES (223,0,1,123,126,10);
INSERT INTO `connections` VALUES (224,0,1,122,126,10);
INSERT INTO `connections` VALUES (225,1,1,126,122,10);
INSERT INTO `connections` VALUES (226,1,1,126,124,10);
INSERT INTO `connections` VALUES (227,2,0,126,106,10);
INSERT INTO `connections` VALUES (228,3,1,18,126,10);
INSERT INTO `connections` VALUES (229,5,1,18,126,10);
INSERT INTO `connections` VALUES (230,4,1,126,18,10);


INSERT INTO `labels` VALUES (287,"Written/Trained",182);
INSERT INTO `labels` VALUES (288,"Procedures",182);
INSERT INTO `labels` VALUES (289,"Other Training",182);
INSERT INTO `labels` VALUES (290,"Commands/Info",183);
INSERT INTO `labels` VALUES (291,"Environmental Inputs",186);
INSERT INTO `labels` VALUES (292,"Environmental Inputs",208);
INSERT INTO `labels` VALUES (293,"Process Inputs",228);
INSERT INTO `labels` VALUES (294,"Disturbances",229);
INSERT INTO `labels` VALUES (295,"Process Outputs",230);