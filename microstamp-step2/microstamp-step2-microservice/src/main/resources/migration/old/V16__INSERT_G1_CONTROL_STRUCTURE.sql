SET @analysis_id = '';

INSERT INTO components (dtype, id, border, is_visible, name, father_id, code, analysis_id) VALUES
("Environment",9,1,0,"Environment",NULL,"Env",@analysis_id),
("Controller",98,1,1,"Control Algorithm",NULL,"C.01",@analysis_id),
("Controller",99,1,1,"Process Model",98,"C.02",@analysis_id),
("Controller",100,1,1,"",NULL,"C.03",@analysis_id),
("Actuator",101,1,1,"Inadequate operation",NULL,"A.01",@analysis_id),
("Sensor",102,1,1,"Inadequate operation",NULL,"S.01",@analysis_id),
("Controller",103,1,1,"",NULL,"C.04",@analysis_id),
("ControlledProcess",104,1,1,"components failures/Changes over time",NULL,"CP.01",@analysis_id);


INSERT INTO connections (id, style, sourceId, targetId, code, analysis_id) VALUES
(171,1,9,98,"Cn.01",@analysis_id),
(172,1,98,100,"Cn.02",@analysis_id),
(173,1,100,98,"Cn.03",@analysis_id),
(174,1,98,101,"Cn.04",@analysis_id),
(175,1,102,98,"Cn.05",@analysis_id),
(176,1,101,104,"Cn.06",@analysis_id),
(177,1,104,102,"Cn.07",@analysis_id),
(178,1,103,104,"Cn.08",@analysis_id),
(179,1,9,104,"Cn.09",@analysis_id),
(180,1,9,104,"Cn.10",@analysis_id),
(181,1,104,9,"Cn.11",@analysis_id);


INSERT INTO connection_actions (id, connection_action_type, name, code, connection_id) VALUES
(276,3,"Control input or external information wrong or missing","PI.1",171),
(277,0,"Missing or wrong communication with another components","CA.1",172),
(278,1,"Missing or wrong communication with another components","FB.1",173),
(279,0,"Provided Control Action(inappropriate, ineffective or missing)","CA.2",174),
(280,1,"Received Feedback(inappropriate, missing or delayed)","FB.2",175),
(281,0,"Received Control Action(Delayed, etc.)","CA.3",176),
(282,1,"Provided Feedback(Incorrect, no information provided, measurement inaccuracies, delays)","FB.3",177),
(283,0,"Conflicting control actions","CA.4",178),
(284,3,"Process input missing or wrong","PI.2",179),
(285,4,"Process output contributes to system hazard","PO.1",181),
(286,5,"Unidentified or out-of-range disturbance","DI.1",180);