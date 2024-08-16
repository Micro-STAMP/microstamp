INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",46,1,0,1,"ATC Manager",NULL,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",47,1,0,1,"Controller A",NULL,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",48,0,0,1,"Controller B",NULL,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",49,1,0,1,"ITP Flight Crew",NULL,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",50,1,0,1,"Ref Flight Crew",NULL,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",51,1,0,1,"ITP Aircraft",NULL,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",52,1,0,1,"ITP Equipment",51,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",53,1,0,1,"TCAS/Transponder",51,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",54,1,0,1,"GNSSU Receiver",51,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",55,1,0,1,"ADS-B",51,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",56,1,0,1,"Reference Aicraft",NULL,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",57,1,0,1,"TCAS/Transponder",56,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Sensor",58,1,0,1,"Other Sensors",56,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",59,1,0,1,"ADS-B",56,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",60,1,0,1,"GNSSU Receiver",56,1);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",61,1,0,1,"GPS Constellation",NULL,1);

INSERT INTO `connections` VALUES (68,3,1,1,46,1);
INSERT INTO `connections` VALUES (69,4,1,46,1,1);
INSERT INTO `connections` VALUES (70,0,1,46,47,1);
INSERT INTO `connections` VALUES (71,1,1,47,46,1);
INSERT INTO `connections` VALUES (72,0,1,47,49,1);
INSERT INTO `connections` VALUES (73,1,1,49,47,1);
INSERT INTO `connections` VALUES (74,0,1,49,51,1);
INSERT INTO `connections` VALUES (75,1,1,51,49,1);
INSERT INTO `connections` VALUES (76,0,1,53,57,1);
INSERT INTO `connections` VALUES (77,1,1,57,53,1);
INSERT INTO `connections` VALUES (78,0,1,54,55,1);
INSERT INTO `connections` VALUES (79,1,1,59,55,1);
INSERT INTO `connections` VALUES (80,1,1,61,54,1);
INSERT INTO `connections` VALUES (81,2,0,46,48,1);
INSERT INTO `connections` VALUES (82,2,0,48,46,1);
INSERT INTO `connections` VALUES (83,2,0,47,48,1);
INSERT INTO `connections` VALUES (84,2,0,48,47,1);
INSERT INTO `connections` VALUES (85,2,0,47,50,1);
INSERT INTO `connections` VALUES (86,2,0,50,47,1);
INSERT INTO `connections` VALUES (87,2,0,48,49,1);
INSERT INTO `connections` VALUES (88,2,0,48,50,1);
INSERT INTO `connections` VALUES (89,0,1,50,56,1);
INSERT INTO `connections` VALUES (90,1,1,56,50,1);
INSERT INTO `connections` VALUES (91,1,1,60,59,1);
INSERT INTO `connections` VALUES (92,1,1,61,60,1);


INSERT INTO `labels` VALUES (140,"Policy",68);
INSERT INTO `labels` VALUES (141,"Certification Information",69);
INSERT INTO `labels` VALUES (142,"Instruction",70);
INSERT INTO `labels` VALUES (143,"Procedures",70);
INSERT INTO `labels` VALUES (144,"Training, Reviews",70);
INSERT INTO `labels` VALUES (145,"Status Reports",71);
INSERT INTO `labels` VALUES (146,"Incident Reports",71);
INSERT INTO `labels` VALUES (147,"Flight",72);
INSERT INTO `labels` VALUES (148,"Instructions",72);
INSERT INTO `labels` VALUES (149,"ITP Clearance",72);
INSERT INTO `labels` VALUES (150,"Request Clearance",73);
INSERT INTO `labels` VALUES (151,"Transcribe ITP Info",73);
INSERT INTO `labels` VALUES (152,"Maneuver Command",74);
INSERT INTO `labels` VALUES (153,"Attitude Information",75);
INSERT INTO `labels` VALUES (154,"TCAS Interrogations",77);
INSERT INTO `labels` VALUES (155,"Ref Aircraft",79);
INSERT INTO `labels` VALUES (156,"State(speed, heading, alt, etc)",79);
INSERT INTO `labels` VALUES (157,"Information",79);
INSERT INTO `labels` VALUES (158,"Time/State Data",80);
INSERT INTO `labels` VALUES (159,"Airspace Transfer",83);
INSERT INTO `labels` VALUES (160,"Airspace Transfer",84);
INSERT INTO `labels` VALUES (161,"Flight Instructions",85);
INSERT INTO `labels` VALUES (162,"Request/Transmit Information",86);
INSERT INTO `labels` VALUES (163,"Flight Instructions",87);
INSERT INTO `labels` VALUES (164,"Maneuver Command",89);
INSERT INTO `labels` VALUES (165,"Attitude Information",90);