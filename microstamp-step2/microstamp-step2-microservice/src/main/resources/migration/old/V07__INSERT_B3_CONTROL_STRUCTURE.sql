INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller", 10, 1, 0, 1, "Treatment Definition", null, 3);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("Controller", 11, 1, 0, 1, "Treatment Delivery", null, 3);

INSERT INTO components (dtype, id, border, is_control_structure, is_visible, name, father_id, control_structure_id)
VALUES("ControlledProcess", 12, 1, 0, 1, "Patient", null, 3);

INSERT INTO `connections` VALUES (1,0,1,10,11,3);
INSERT INTO `connections` VALUES (2,0,1,11,12,3);
INSERT INTO `connections` VALUES (3,1,1,11,10,3);
INSERT INTO `connections` VALUES (4,1,1,12,11,3);
INSERT INTO `connections` VALUES (5,1,1,12,10,3);

INSERT INTO `labels` VALUES (6,'Therapeutic Requirements',1);
INSERT INTO `labels` VALUES (7,'1. Treatment Specifications (fraction definition, target positioning information, steering file)',1);
INSERT INTO `labels` VALUES (8,'2. Capability Upgrade Requests',1);
INSERT INTO `labels` VALUES (9,'Patient Preparation',2);
INSERT INTO `labels` VALUES (10,'Beam Creation and Delivery',2);
INSERT INTO `labels` VALUES (11,'QA results',3);
INSERT INTO `labels` VALUES (12,'Patient physionomy change',3);
INSERT INTO `labels` VALUES (13,'Patient well-being',4);
INSERT INTO `labels` VALUES (14,'Patient physiognomy changes',4);
INSERT INTO `labels` VALUES (15,'(delayed) Patient health outcome',5);