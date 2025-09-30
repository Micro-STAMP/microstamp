INSERT INTO step4new.formal_scenarios (id,analysis_id,unsafe_control_action_id) VALUES
	 ('1a2b3c4d-5e6f-7890-abcd-ef1234567890','6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45','38d1a1bc-804e-4246-adc9-30a704d02c9f');

INSERT INTO step4new.formal_scenario_classes (id,code,formal_scenario_id) VALUES
	 ('2b3c4d5e-6f78-90ab-cdef-123456789012','[CLASS-1]','1a2b3c4d-5e6f-7890-abcd-ef1234567890'),
	 ('3c4d5e6f-7890-abcd-ef12-34567890123a','[CLASS-2]','1a2b3c4d-5e6f-7890-abcd-ef1234567890'),
	 ('4d5e6f78-90ab-cdef-1234-567890123abc','[CLASS-3]','1a2b3c4d-5e6f-7890-abcd-ef1234567890'),
	 ('5e6f7890-abcd-ef12-3456-7890123abcde','[CLASS-4]','1a2b3c4d-5e6f-7890-abcd-ef1234567890');

INSERT INTO step4new.high_level_solutions (id,controller_behavior,other_solutions,process_behavior,formal_scenario_class_id) VALUES
	 -- Class 1 - Unsafe Controller Behavior
	 ('6f789012-abcd-ef12-3456-7890123abcde','Update the software to avoid errors in calculating insulin dosage based on glucose measurements. The software should alert the patient if the device is on but without predefined data.','','','2b3c4d5e-6f78-90ab-cdef-123456789012'),
	 -- Class 2 - Unsafe Feedback Path
	 ('78901234-bcde-f123-4567-890123abcdef','','Separate Insulin Pump and GCM in two components. The GCM must send the feedback to Insulin Pump and Mobile Device.','','3c4d5e6f-7890-abcd-ef12-34567890123a'),
	 -- Class 3 - Unsafe Control Path
	 ('8901234a-bcde-f123-4567-890123abcdef','Add an audible alert whenever the reservoir or the battery level are low.','','','4d5e6f78-90ab-cdef-1234-567890123abc'),
	 -- Class 4 - Unsafe Controlled Process Behavior
	 ('901234ab-cdef-1234-5678-90123abcdef0','','The quality of the insulin is compromised, either because it is not of the correct origin or because it has been stored incorrectly or for too long.','','5e6f7890-abcd-ef12-3456-7890123abcde');

INSERT INTO step4new.refined_scenarios (id,code,refined_scenario,unsafe_control_action_id,id_common_causes,formal_scenario_class_id) VALUES
	 -- Class 1 - Unsafe Controller Behavior
	 ('a1234bcd-ef12-3456-7890-123abcdef012','RSC-1','(a) Insulin Pump by design is responsible for always providing continuous monitoring of blood glucose level.','38d1a1bc-804e-4246-adc9-30a704d02c9f','3d7e9b92-1f5f-4e4e-9cbe-ae9c2e1a1a01','2b3c4d5e-6f78-90ab-cdef-123456789012'),
	 ('b234cdef-1234-5678-901a-bcdef0123456','RSC-2','(b) Insulin Pump control algorithm is not designed to trigger pump insulin if no initial configuration is set.','38d1a1bc-804e-4246-adc9-30a704d02c9f','6a9f1e04-5a37-4d75-8b5b-7e1f314c2b02','2b3c4d5e-6f78-90ab-cdef-123456789012'),
	 ('c345def0-1234-5678-901a-bcdef0123456','RSC-3','(c) Glucose level is not updated correctly on the process model when the glucose level is high.','38d1a1bc-804e-4246-adc9-30a704d02c9f','d1c2f3a4-b5c6-4789-9a0b-1c2d3e4f5a03','2b3c4d5e-6f78-90ab-cdef-123456789012'),
	 ('d456ef01-2345-6789-01ab-cdef01234567','RSC-4','(e) Insulin Pump failure causes the pump to be unable to update the process model or pump insulin when needed.','38d1a1bc-804e-4246-adc9-30a704d02c9f','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','2b3c4d5e-6f78-90ab-cdef-123456789012'),
	 ('e567f012-3456-789a-bcde-f01234567890','RSC-5','(e) Insulin Pump do not have enough battery for pumping insulin into the patient''s body.','38d1a1bc-804e-4246-adc9-30a704d02c9f','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','2b3c4d5e-6f78-90ab-cdef-123456789012'),
	 -- Class 2 - Unsafe Feedback Path
	 ('f678012a-bcde-f123-4567-890123abcdef','RSC-6','Feedback of Glucose level is not provided when the Glucose level is high.','38d1a1bc-804e-4246-adc9-30a704d02c9f','aabbccdd-eeff-1122-3344-55667788990f','3c4d5e6f-7890-abcd-ef12-34567890123a'),
	 ('0789123b-cdef-1234-5678-90123abcdef0','RSC-7','Faulty or out-of-calibration GCM sensor, not detecting glucose levels accurately.','38d1a1bc-804e-4246-adc9-30a704d02c9f','aabbccdd-eeff-1122-3344-55667788990f','3c4d5e6f-7890-abcd-ef12-34567890123a'),
	 -- Class 3 - Unsafe Control Path
	 ('189a234c-def1-2345-6789-0123abcdef01','RSC-8','The reservoir level does not have enough insulin to pump.','38d1a1bc-804e-4246-adc9-30a704d02c9f','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','4d5e6f78-90ab-cdef-1234-567890123abc'),
	 ('29ab345d-ef12-3456-789a-bcdef0123456','RSC-9','The battery level is not sufficient to pump insulin in a patient''s body.','38d1a1bc-804e-4246-adc9-30a704d02c9f','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','4d5e6f78-90ab-cdef-1234-567890123abc'),
	 ('3abc456e-f123-4567-89ab-cdef01234567','RSC-10','Obstruction in the insulin pump tube.','38d1a1bc-804e-4246-adc9-30a704d02c9f','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','4d5e6f78-90ab-cdef-1234-567890123abc'),
	 ('4bcd567f-0123-4567-89ab-cdef01234567','RSC-11','Failure in the insulin flow control valve.','38d1a1bc-804e-4246-adc9-30a704d02c9f','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','4d5e6f78-90ab-cdef-1234-567890123abc'),
	 ('5cde6780-1234-5678-9abc-def012345678','RSC-12','Failure in the insulin pump that prevents delivery of the programmed dose.','38d1a1bc-804e-4246-adc9-30a704d02c9f','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','4d5e6f78-90ab-cdef-1234-567890123abc'),
	 -- Class 4 - Unsafe Controlled Process Behavior
	 ('6def7891-2345-6789-abcd-ef0123456789','RSC-13','The patient used an insulin not approved by agencies such as ANVISA or FDA.','38d1a1bc-804e-4246-adc9-30a704d02c9f','9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04','5e6f7890-abcd-ef12-3456-7890123abcde'),
	 ('7ef089a2-3456-789a-bcde-f0123456789a','RSC-14','Insulin was not stored at the correct temperature or it was exposed to extreme temperatures.','38d1a1bc-804e-4246-adc9-30a704d02c9f','9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04','5e6f7890-abcd-ef12-3456-7890123abcde'),
	 ('8f019ab3-4567-89ab-cdef-0123456789ab','RSC-15','Patient used an expired insulin','38d1a1bc-804e-4246-adc9-30a704d02c9f','9f8e7d6c-5b4a-4321-9a8b-7c6d5e4f3a04','5e6f7890-abcd-ef12-3456-7890123abcde');

INSERT INTO step4new.mitigations (id,code,mitigation,refined_scenario_id) VALUES
	 -- Class 1 Solutions
	 ('912abc4d-5678-9abc-def0-123456789abc','RSO-1','Update the Continuous Glucose Monitor (GCM) to ensure that the insulin pump provides the continuous monitoring of blood glucose level.','a1234bcd-ef12-3456-7890-123abcdef012'),
	 ('a23bcd5e-6789-abcd-ef01-23456789abcd','RSO-2','The software must issue an audible alert if no initial configuration is set or if the insulin pump.','b234cdef-1234-5678-901a-bcdef0123456'),
	 ('b34cde6f-789a-bcde-f012-3456789abcde','RSO-3','The software must update the Glucose level continuously (e.g. every five minutes) to avoid hypoglycemia or hyperglycemia.','c345def0-1234-5678-901a-bcdef0123456'),
	 ('c45def70-89ab-cdef-0123-456789abcdef','RSO-4','The insulin pump must issue an audible alert when it is connected and the glucose level is not updated after a while.','d456ef01-2345-6789-01ab-cdef01234567'),
	 ('d56ef081-9abc-def0-1234-56789abcdef0','RSO-5','The software must issue an audible alert when the battery is not healthy.','e567f012-3456-789a-bcde-f01234567890'),
	 -- Class 2 Solutions
	 ('e67f0192-abcd-ef01-2345-6789abcdef01','RSO-6','The separation (in two devices) of Insulin Pump and CGM must mitigate the errors on feedback (Insulin pump and Mobile device + App receives the feedback separately).','f678012a-bcde-f123-4567-890123abcdef'),
	 ('f78012a3-bcde-f012-3456-789abcdef012','RSO-7','The GCM sensor must be calibrated and undergo review/maintenance according to the time stipulated by the manufacturer.','0789123b-cdef-1234-5678-90123abcdef0'),
	 -- Class 3 Solutions
	 ('089123b4-cdef-0123-4567-89abcdef0123','RSO-8','Add alerts for the patient (audible alert for the Insulin Pump and a notification to the Mobile device + App) to inform that the reservoir level of the insulin pump is low and needs to charge.','189a234c-def1-2345-6789-0123abcdef01'),
	 ('19a234c5-def0-1234-5678-9abcdef01234','RSO-9','Add alerts for the patient (audible alert for the Insulin Pump and a notification to the Mobile device + App) to inform that the battery level of the insulin pump is low and needs to charge.','29ab345d-ef12-3456-789a-bcdef0123456'),
	 ('2ab345d6-ef01-2345-6789-abcdef012345','RSO-10','Add an alert whenever the insulin pump tube is obstructed.','3abc456e-f123-4567-89ab-cdef01234567'),
	 ('3bc456e7-f012-3456-789a-bcdef0123456','RSO-11','The Insulin Pump must undergo review/maintenance according to the time stipulated by the manufacturer.','4bcd567f-0123-4567-89ab-cdef01234567'),
	 ('4cd567f8-0123-4567-89ab-cdef01234567','RSO-12','The insulin pump must issue an audible alert when it is connected and it suddenly stops to deliver the preset glucose quantity.','5cde6780-1234-5678-9abc-def012345678'),
	 -- Class 4 Solutions
	 ('5de67809-1234-5678-9abc-def012345678','RSO-13','The patient must buy insulin approved by regulatory agencies, preferably using the brands recommended by the Insulin Pump''s manufacturer.','6def7891-2345-6789-abcd-ef0123456789'),
	 ('6ef7890a-2345-6789-abcd-ef012345678a','RSO-14','The insulin must be stored in the refrigerator between 2°C and 8°C before use and the patient must avoid exposing the insulin to excessive heat and direct sunlight.','7ef089a2-3456-789a-bcde-f0123456789a'),
	 ('7f0890ab-3456-789a-bcde-f012345678ab','RSO-15','The patient must avoid using insulin after 28 days inside the Insulin Pump.','8f019ab3-4567-89ab-cdef-0123456789ab');
