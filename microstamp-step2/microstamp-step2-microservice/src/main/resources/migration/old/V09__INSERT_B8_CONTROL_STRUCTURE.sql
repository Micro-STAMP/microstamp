SET @analysis_id = '';

INSERT INTO components (dtype, id, border, is_visible, name, father_id, code, analysis_id) VALUES
("Controller",19,1,1,"JAXA Ground Station",NULL,"C.01",@analysis_id),
("Controller",20,1,1,"NASA Ground Station",NULL,"C.02",@analysis_id),
("Controller",21,1,1,"International Space Station (ISS)",NULL,"C.03",@analysis_id),
("ControlledProcess",22,1,1,"Autonomous H-II Transfer Vehicle (HTV)",NULL,"CP.01",@analysis_id);


INSERT INTO connections (id, style, sourceId, targetId, code, analysis_id) VALUES
(19,1,19,20,"Cn.01",@analysis_id),
(20,1,20,19,"Cn.02",@analysis_id),
(21,1,20,21,"Cn.03",@analysis_id),
(22,1,21,20,"Cn.04",@analysis_id),
(23,1,21,22,"Cn.05",@analysis_id),
(24,1,22,21,"Cn.06",@analysis_id);


INSERT INTO connection_actions (id, connection_action_type, name, code, connection_id) VALUES
(38,0,"Abort","CA.1",19),
(39,0,"Retreat","CA.2",19),
(40,0,"Hold","CA.3",19),
(41,0,"FRGF Separation","CA.4",19),
(42,1,"Acknowledgements","FB.1",20),
(43,1,"HTV Status","FB.2",20),
(44,0,"Abort","CA.5",21),
(45,0,"Retreat","CA.6",21),
(46,0,"Hold","CA.7",21),
(47,0,"FRGF Separation Enable","CA.8",21),
(48,0,"FRGF Separation Hold","CA.9",21),
(49,0,"FRGF Separation","CA.10",21),
(50,1,"HTV Mode","FB.3",22),
(51,1,"HTV Fault Status","FB.4",22),
(52,1,"Crew status","FB.5",22),
(53,1,"Telemetry","FB.6",22),
(54,0,"Free Drift","CA.11",23),
(55,0,"Capture","CA.12",23),
(56,0,"Abort","CA.13",23),
(57,0,"Retreat","CA.14",23),
(58,0,"Hold","CA.15",23),
(59,0,"FRGF Separation Enable","CA.16",23),
(60,0,"FRGF Separation Inhibit","CA.17",23),
(61,0,"FRGF Separation","CA.18",23),
(62,1,"HTV Mode","FB.7",24),
(63,1,"HTV Fault Status","FB.8",24),
(64,1,"Position (visual)","FB.9",24),
(65,1,"Speed (visual)","FB.10",24);