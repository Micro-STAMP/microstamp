INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",19,1,0,1,"JAXA Ground Station",NULL,8);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",20,1,0,1,"NASA Ground Station",NULL,8);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller",21,1,0,1,"International Space Station (ISS)",NULL,8);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess",22,1,0,1,"Autonomous H-II Transfer Vehicle (HTV)",NULL,8);


INSERT INTO `connections` VALUES (19,0,1,19,20,8);
INSERT INTO `connections` VALUES (20,1,1,20,19,8);
INSERT INTO `connections` VALUES (21,0,1,20,21,8);
INSERT INTO `connections` VALUES (22,1,1,21,20,8);
INSERT INTO `connections` VALUES (23,0,1,21,22,8);
INSERT INTO `connections` VALUES (24,1,1,22,21,8);


INSERT INTO `labels` VALUES (38,"Abort",19);
INSERT INTO `labels` VALUES (39,"Retreat",19);
INSERT INTO `labels` VALUES (40,"Hold",19);
INSERT INTO `labels` VALUES (41,"FRGF Separation",19);
INSERT INTO `labels` VALUES (42,"Acknowledgements",20);
INSERT INTO `labels` VALUES (43,"HTV Status",20);
INSERT INTO `labels` VALUES (44,"Abort",21);
INSERT INTO `labels` VALUES (45,"Retreat",21);
INSERT INTO `labels` VALUES (46,"Hold",21);
INSERT INTO `labels` VALUES (47,"FRGF Separation Enable",21);
INSERT INTO `labels` VALUES (48,"FRGF Separation Hold",21);
INSERT INTO `labels` VALUES (49,"FRGF Separation",21);
INSERT INTO `labels` VALUES (50,"HTV Mode",22);
INSERT INTO `labels` VALUES (51,"HTV Fault Status",22);
INSERT INTO `labels` VALUES (52,"Crew status",22);
INSERT INTO `labels` VALUES (53,"Telemetry",22);
INSERT INTO `labels` VALUES (54,"Free Drift",23);
INSERT INTO `labels` VALUES (55,"Capture",23);
INSERT INTO `labels` VALUES (56,"Abort",23);
INSERT INTO `labels` VALUES (57,"Retreat",23);
INSERT INTO `labels` VALUES (58,"Hold",23);
INSERT INTO `labels` VALUES (59,"FRGF Separation Enable",23);
INSERT INTO `labels` VALUES (60,"FRGF Separation Inhibit",23);
INSERT INTO `labels` VALUES (61,"FRGF Separation",23);
INSERT INTO `labels` VALUES (62,"HTV Mode",24);
INSERT INTO `labels` VALUES (63,"HTV Fault Status",24);
INSERT INTO `labels` VALUES (64,"Position (visual)",24);
INSERT INTO `labels` VALUES (65,"Speed (visual)",24);




