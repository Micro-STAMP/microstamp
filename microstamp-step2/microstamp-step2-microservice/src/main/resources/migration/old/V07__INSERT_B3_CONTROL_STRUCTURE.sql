SET @analysis_id = "";

INSERT INTO components (dtype, id, border, is_visible, name, father_id, code, analysis_id) VALUES
("Controller",10,1,1,"Treatment Definition",NULL,"C.01",@analysis_id),
("Controller",11,1,1,"Treatment Delivery",NULL,"C.02",@analysis_id),
("ControlledProcess",12,1,1,"Patient",NULL,"CP.01",@analysis_id);


INSERT INTO connections (id, style, sourceId, targetId, code, analysis_id) VALUES
(1,1,10,11,"Cn.01",@analysis_id),
(2,1,11,12,"Cn.02",@analysis_id),
(3,1,11,10,"Cn.03",@analysis_id),
(4,1,12,11,"Cn.04",@analysis_id),
(5,1,12,10,"Cn.05",@analysis_id);


INSERT INTO connection_actions (id, connection_action_type, name, code, connection_id) VALUES
(6,0,"Therapeutic Requirements","CA.1",1),
(7,0,"1. Treatment Specifications (fraction definition, target positioning information, steering file)","CA.2",1),
(8,0,"2. Capability Upgrade Requests","CA.3",1),
(9,0,"Patient Preparation","CA.4",2),
(10,0,"Beam Creation and Delivery","CA.5",2),
(11,1,"QA results","FB.1",3),
(12,1,"Patient physionomy change","FB.2",3),
(13,1,"Patient well-being","FB.3",4),
(14,1,"Patient physiognomy changes","FB.4",4),
(15,1,"(delayed) Patient health outcome","FB.5",5);