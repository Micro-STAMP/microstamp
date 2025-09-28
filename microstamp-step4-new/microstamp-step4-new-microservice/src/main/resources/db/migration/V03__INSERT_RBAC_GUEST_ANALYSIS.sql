INSERT INTO step4new.formal_scenarios (id,analysis_id,unsafe_control_action_id) VALUES
	 ('d530d55a-7712-4661-97fa-72591f60d871','3560faf9-eed1-4708-a22d-d50eebfe6b26','77656f07-824a-468a-a37f-47b2c71af475');

INSERT INTO step4new.formal_scenario_classes (id,code,formal_scenario_id) VALUES
	 ('7598f095-200b-414a-8327-ade3b6ca7246','[CLASS-1]','d530d55a-7712-4661-97fa-72591f60d871'),
	 ('ace9e35a-a697-4472-b43e-346335efd8e8','[CLASS-4]','d530d55a-7712-4661-97fa-72591f60d871'),
	 ('ded7ef04-0eb7-4b14-a1e8-003658c64466','[CLASS-2]','d530d55a-7712-4661-97fa-72591f60d871'),
	 ('fbfea1c8-2826-4f81-80b5-24e2522c2193','[CLASS-3]','d530d55a-7712-4661-97fa-72591f60d871');

INSERT INTO step4new.high_level_solutions (id,controller_behavior,other_solutions,process_behavior,formal_scenario_class_id) VALUES
	 ('62020826-5655-4201-8072-a2c259caac58','The Login Controller does not enforce or respond to Provide Credentials','The Login Controller must always respond to the Provide Credentials','Provide Credentials is received by the Login Controller when the user is provided, the password is provided','ace9e35a-a697-4472-b43e-346335efd8e8'),
	 ('7f92f3f6-ac58-4de0-9112-8fded6123f20','User fails to provide credentials when username is provided, password is provided.','There must be a way to tell when a user has provided credentials but they have not been received by the Login Controller.','Provide Credentials is not received by Login Controller when user is provided, password is provided.','fbfea1c8-2826-4f81-80b5-24e2522c2193'),
	 ('87c5ab7c-cd81-4c60-8f64-cea6896d87f1','The user provided the password and username','The feedback the user receives must always correctly indicate whether the username is provided and the password is provided','Feedback received by User does not correctly indicate that username is provided and password is provided','ded7ef04-0eb7-4b14-a1e8-003658c64466'),
	 ('b4579515-7199-495e-918c-4c5cd6fb4660','User does not provide credentials','The user must be able to provide credentials correctly','User receives feedback indicating that username is provided, password is provided','7598f095-200b-414a-8327-ade3b6ca7246');

INSERT INTO step4new.refined_scenarios (id,refined_scenario,unsafe_control_action_id,id_common_causes,formal_scenario_class_id) VALUES
	 ('021ad64a-c132-4f7e-bc57-e15053bd3bbe','The form does not update when the user fills in the required fields.','77656f07-824a-468a-a37f-47b2c71af475','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','ded7ef04-0eb7-4b14-a1e8-003658c64466'),
	 ('5b0fa259-9397-4553-910f-36d294270f8d','The submit form button is disabled.','77656f07-824a-468a-a37f-47b2c71af475','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','7598f095-200b-414a-8327-ade3b6ca7246'),
	 ('600d6c9e-b66c-4985-98ef-2aa8af0909f1','The form is confusing and the user did not find the button to submit the provided credentials.','77656f07-824a-468a-a37f-47b2c71af475','aabbccdd-eeff-1122-3344-55667788990f','7598f095-200b-414a-8327-ade3b6ca7246'),
	 ('75a53c39-8bfd-4378-b751-02e2d33652ab','The system is overloaded and the Login Controller is unable to validate or receive your credentials.','77656f07-824a-468a-a37f-47b2c71af475','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','ace9e35a-a697-4472-b43e-346335efd8e8'),
	 ('a20b0df8-227f-48d9-99d3-dbdcbce5b440','The user is not connected to the internet when trying to provide credentials.','77656f07-824a-468a-a37f-47b2c71af475','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','fbfea1c8-2826-4f81-80b5-24e2522c2193'),
	 ('e6325980-dfce-4faf-879c-8ae7f50f5ad6','The form leaves fields unreadable when the user is typing.','77656f07-824a-468a-a37f-47b2c71af475','0a1b2c3d-4e5f-6789-8a7b-6c5d4e3f2a05','ded7ef04-0eb7-4b14-a1e8-003658c64466');

INSERT INTO step4new.mitigations (id,mitigation,refined_scenario_id) VALUES
	 ('25ea0d87-d233-47ba-9013-36c9c99687f0','The submit form button must always be enabled.','5b0fa259-9397-4553-910f-36d294270f8d'),
	 ('40cece14-f3a8-4fce-bb14-776709561092','The user should receive feedback that the Login Controller has provided credentials.','a20b0df8-227f-48d9-99d3-dbdcbce5b440'),
	 ('941ecd17-9023-49cc-a4e5-1a27e3bbe5fb','The Login Controller must always warn the User when it is overloaded or not after an action.','75a53c39-8bfd-4378-b751-02e2d33652ab'),
	 ('d3018076-9402-4e84-866c-0bd8a663fb32','The form should always update after filling in the fields and submitting the form with a success message.','021ad64a-c132-4f7e-bc57-e15053bd3bbe'),
	 ('d7a22bc5-6c61-4925-88db-69c3d0ccbd22','The form should provide feedback on the input fields.','e6325980-dfce-4faf-879c-8ae7f50f5ad6'),
	 ('ef2ce7c6-9edb-447c-9b07-cbb2a7507a20','The form should be self-explanatory and clear about where to submit credentials.','600d6c9e-b66c-4985-98ef-2aa8af0909f1');

