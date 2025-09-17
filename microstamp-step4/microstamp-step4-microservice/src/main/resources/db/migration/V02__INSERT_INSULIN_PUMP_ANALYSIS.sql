
INSERT INTO step4.four_tuples (id,analysis_id,associated_causal_factor,code,rationale,recommendation,scenario) VALUES
    ('0710603a-b71c-44c0-ab72-946128b2c3af','6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45','Flaw in the modifications or algorithm was not updated to support the modifications.','LC-5','Algorithm should be updated properly for each change.','After each modification in the algorithm, it must be revised and tested to minimize errors.','[Inadequate Control Algorithm] Algorithm updated incorrectly.'),
    ('10569ba6-a794-40ad-965f-7f3869a13f31','6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45','Failure in the communication between Insulin Pump and the external system.','LC-1','This external system are out of the scope of the system under analysis.','The communication between Insulin Pump and external system must be improved.','[Control input external information wrong or missing] Insulin Pump receives the wrong value of Glucose Level.'),
    ('6eb922ef-cf43-487b-896c-c621c08856f5','6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45','Algorithm wrong or incomplete or lack of knowledge of the system.','LC-3','Simulations of the system can help to validate the algorithm.','The algorithm must be revised and tested after each change to minimize errors.','[Inadequate Control Algorithm] An incorrect algorithm was designed.'),
    ('8afc0570-0170-47d1-a165-1a8b6f9647f2','6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45','Failure in the communication between Insulin Pump and the external system.','LC-2','This external system are out of the scope of the system under analysis.','The communication between Insulin Pump and external system must be improved.','[Control input or external information wrong or missing] Value of Glucose level is missing.'),
    ('9a06bcf7-20d9-49c3-b4cd-d5d5e1ea73e0','6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45','Algorithm was not updated to support changes of the process.','LC-4','Algorithm must be revised and adapted to support the process changes.','Algorithm must be updated, revised and tested after each change in the process to minimize errors.','[Inadequate Control Algorithm] Algorithm ineffective, unsafe or incomplete after process changes.');

INSERT INTO step4.four_tuple_unsafe_control_actions (four_tuple_id,unsafe_control_action_id) VALUES
    ('10569ba6-a794-40ad-965f-7f3869a13f31','7d5e9f7e-4506-4970-ad30-3b8392faf8c6'),
    ('8afc0570-0170-47d1-a165-1a8b6f9647f2','7d5e9f7e-4506-4970-ad30-3b8392faf8c6'),
    ('6eb922ef-cf43-487b-896c-c621c08856f5','7d5e9f7e-4506-4970-ad30-3b8392faf8c6'),
    ('9a06bcf7-20d9-49c3-b4cd-d5d5e1ea73e0','7d5e9f7e-4506-4970-ad30-3b8392faf8c6'),
    ('0710603a-b71c-44c0-ab72-946128b2c3af','7d5e9f7e-4506-4970-ad30-3b8392faf8c6');
