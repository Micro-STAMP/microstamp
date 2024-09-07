SET @analysis_id = '';

INSERT INTO components (dtype, id, border, is_visible, name, father_id, code, analysis_id) VALUES
("Environment",7,1,0,"Environment",NULL,"Env",@analysis_id),
("Controller",13,1,1,"Driver",NULL,"C.01",@analysis_id),
("Controller",14,1,1,"Auto-Hold Module",NULL,"C.02",@analysis_id),
("ControlledProcess",15,0,1,"Physical Vehicle",NULL,"CP.01",@analysis_id),
("ControlledProcess",16,1,1,"Braking System",15,"CP.02",@analysis_id),
("ControlledProcess",17,1,1,"Propulsion System",15,"CP.03",@analysis_id);


INSERT INTO connections (id, style, sourceId, targetId, code, analysis_id) VALUES
(6,1,7,15,"Cn.01",@analysis_id),
(7,1,15,7,"Cn.02",@analysis_id),
(8,1,7,13,"Cn.03",@analysis_id),
(9,1,14,13,"Cn.04",@analysis_id),
(10,1,13,17,"Cn.05",@analysis_id),
(11,1,15,13,"Cn.06",@analysis_id),
(12,1,14,16,"Cn.07",@analysis_id),
(13,1,16,14,"Cn.08",@analysis_id),
(14,1,17,14,"Cn.09",@analysis_id),
(15,1,15,14,"Cn.10",@analysis_id),
(16,1,13,15,"Cn.11",@analysis_id),
(17,1,15,13,"Cn.12",@analysis_id),
(18,1,13,14,"Cn.13",@analysis_id);


INSERT INTO connection_actions (id, connection_action_type, name, code, connection_id) VALUES
(16,3,"Visual cues","PI.1",8),
(17,3,"Physical feedback","PI.2",8),
(18,1,"AH Enable","FB.1",9),
(19,1,"AH Disabled","FB.2",9),
(20,0,"Accelerate","CA.1",10),
(21,0,"Shift","CA.2",10),
(22,1,"Vehicle speed","FB.3",11),
(23,1,"Visual feedback","FB.4",11),
(24,0,"Hold","CA.3",12),
(25,0,"Release","CA.4",12),
(26,0,"Additional Pressure","CA.5",12),
(27,1,"Wheel speed","FB.5",13),
(28,1,"Accel pos.","FB.6",14),
(29,1,"PRNDL","FB.7",14),
(30,1,"Driver presence","FB.8",15),
(31,1,"Inclination","FB.9",15),
(32,0,"Brake","CA.6",16),
(33,1,"Pedal response","FB.10",17),
(34,0,"Enable AH","CA.7",18),
(35,0,"Disable AH","CA.8",18),
(36,0,"Brake pedal on","CA.9",18),
(37,0,"Brake pedal off","CA.10",18);