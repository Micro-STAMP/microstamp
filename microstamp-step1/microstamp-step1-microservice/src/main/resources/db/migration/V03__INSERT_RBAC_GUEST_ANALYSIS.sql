INSERT INTO step1.system_goals (id,analysis_id,code,name) VALUES
	 ('3538ee6e-e71c-4006-b751-f6a90cda0d41','3560faf9-eed1-4708-a22d-d50eebfe6b26','SG-6','Ensure Availability'),
	 ('37186a54-0077-481a-b6fc-e47cce380c44','3560faf9-eed1-4708-a22d-d50eebfe6b26','SG-5','Facilitate Role Management'),
	 ('61e021c7-5127-4fd2-9635-325994b40ace','3560faf9-eed1-4708-a22d-d50eebfe6b26','SG-7','Ensure Auditability'),
	 ('707322aa-6247-4c32-9270-cf4ae0489442','3560faf9-eed1-4708-a22d-d50eebfe6b26','SG-3','Maintain Data Confidentiality '),
	 ('9946fd9c-9f64-4051-bf28-f7da53cc8de0','3560faf9-eed1-4708-a22d-d50eebfe6b26','SG-4','Maintain Data Integrity'),
	 ('a9dbb5a1-6dea-4f1a-af78-019839b476b0','3560faf9-eed1-4708-a22d-d50eebfe6b26','SG-1','Ensure Authentication'),
	 ('ca57d668-2402-49d4-bde1-968caee4d085','3560faf9-eed1-4708-a22d-d50eebfe6b26','SG-2','Enforce Authorization');

INSERT INTO step1.system_safety_constraints (id,analysis_id,code,name) VALUES
    ('0136fdae-7aa7-432e-a6f3-0fb158da4c9c','3560faf9-eed1-4708-a22d-d50eebfe6b26','SSC-5','The repository must have protection'),
    ('26f40caa-e8f2-4259-9a70-724829fa6f58','3560faf9-eed1-4708-a22d-d50eebfe6b26','SSC-1','The system must not be overloaded'),
    ('3d4af182-3e57-4774-8ccc-24ad9ab3cb02','3560faf9-eed1-4708-a22d-d50eebfe6b26','SSC-4','Credentials must be validated before authorization'),
    ('4c7cc801-3cfa-4853-aabb-77c6c9482b6d','3560faf9-eed1-4708-a22d-d50eebfe6b26','SSC-6','Users must lose access after permissions are revoked'),
    ('e3ca6ccc-1f22-4258-b0f1-d593fc147595','3560faf9-eed1-4708-a22d-d50eebfe6b26','SSC-7','Only authenticated users are able to access the system'),
    ('f4c33b63-74d6-4541-85c8-abb9e750d849','3560faf9-eed1-4708-a22d-d50eebfe6b26','SSC-2','Password security policy must be in place'),
    ('fda3d662-56c6-4486-b003-f2711a6fddf7','3560faf9-eed1-4708-a22d-d50eebfe6b26','SSC-3','The Authenticator must associate a role with an authenticated user');

INSERT INTO step1.assumptions (id,analysis_id,code,name) VALUES
	 ('1baf483d-1418-42ef-8c05-e40ce96022fa','3560faf9-eed1-4708-a22d-d50eebfe6b26','AS-6','Scalability and Performance'),
	 ('5cc8a8f5-105c-40a2-a848-2bf74424f1b1','3560faf9-eed1-4708-a22d-d50eebfe6b26','AS-4','Roles'),
	 ('7306447e-435b-48cc-a5aa-d205553ef30f','3560faf9-eed1-4708-a22d-d50eebfe6b26','AS-5','Regulatory and Legal compliance'),
	 ('90d44e2c-cc00-4e25-a8cf-3c6504197160','3560faf9-eed1-4708-a22d-d50eebfe6b26','AS-2','System Architecture'),
	 ('aac7fc31-7fb7-41d7-ab1b-626d0f49bf19','3560faf9-eed1-4708-a22d-d50eebfe6b26','AS-3','Web Environment'),
	 ('e0389c23-ce37-4828-8854-08295814d5a8','3560faf9-eed1-4708-a22d-d50eebfe6b26','AS-1','System Boundaries');
	 
INSERT INTO step1.hazards (id,analysis_id,code,name,father_id) VALUES
	 ('27e81d19-ec78-4e15-ae94-d85174ef1e8f','3560faf9-eed1-4708-a22d-d50eebfe6b26','H-5','Lack of data protection',NULL),
	 ('34761ce9-1520-481a-8bfb-449c25cfd909','3560faf9-eed1-4708-a22d-d50eebfe6b26','H-6','User retains access after permissions are revoked',NULL),
	 ('3a3b081e-80ed-46f6-a080-8fa8a9436f6e','3560faf9-eed1-4708-a22d-d50eebfe6b26','H-2','Absence of password security policies',NULL),
	 ('6003135b-ad87-4624-82dd-5e30ec63ae2a','3560faf9-eed1-4708-a22d-d50eebfe6b26','H-1','System overload',NULL),
	 ('944a7bfa-2f56-4126-9207-7dfcd0873b8d','3560faf9-eed1-4708-a22d-d50eebfe6b26','H-4',' No validation of credentials before authorization',NULL),
	 ('cf5b648c-fcfe-433e-9805-e513880895ba','3560faf9-eed1-4708-a22d-d50eebfe6b26','H-3','No role assigned to an authenticated user',NULL);

INSERT INTO step1.losses (id,analysis_id,code,name) VALUES
	 ('0390fbda-035e-4f1c-987d-d795ee688daf','3560faf9-eed1-4708-a22d-d50eebfe6b26','L-1','User cannot log in'),
	 ('152b3bc8-f90c-47e8-a723-6125a719c60c','3560faf9-eed1-4708-a22d-d50eebfe6b26','L-4','Unauthorized user gains access to confidential information'),
	 ('6e142906-ed68-4a1b-9826-0b45c0b00174','3560faf9-eed1-4708-a22d-d50eebfe6b26','L-5','System inability to recover user data'),
	 ('d1d8e626-c686-4fc6-89c5-cf684ffce2c5','3560faf9-eed1-4708-a22d-d50eebfe6b26','L-6','User receives incorrect role from the Authorizer'),
	 ('d3e428c7-8bdf-431d-81f8-4948da6ba62a','3560faf9-eed1-4708-a22d-d50eebfe6b26','L-2','Disclosure of sensitive user data'),
	 ('e15d4220-ad4e-4818-ab68-a915d317450f','3560faf9-eed1-4708-a22d-d50eebfe6b26','L-7','User incorrectly authenticated by the Authenticator'),
	 ('e3196b7d-d351-4943-860d-6eff804af7a4','3560faf9-eed1-4708-a22d-d50eebfe6b26','L-3','User account compromised due to third-party intrusion');
	 
INSERT INTO	step1.system_safety_constraint_hazard (system_safety_constraint_id,hazard_id) VALUES
	 ('26f40caa-e8f2-4259-9a70-724829fa6f58','6003135b-ad87-4624-82dd-5e30ec63ae2a'),
	 ('f4c33b63-74d6-4541-85c8-abb9e750d849','3a3b081e-80ed-46f6-a080-8fa8a9436f6e'),
	 ('fda3d662-56c6-4486-b003-f2711a6fddf7','cf5b648c-fcfe-433e-9805-e513880895ba'),
	 ('3d4af182-3e57-4774-8ccc-24ad9ab3cb02','944a7bfa-2f56-4126-9207-7dfcd0873b8d'),
	 ('0136fdae-7aa7-432e-a6f3-0fb158da4c9c','27e81d19-ec78-4e15-ae94-d85174ef1e8f'),
	 ('4c7cc801-3cfa-4853-aabb-77c6c9482b6d','34761ce9-1520-481a-8bfb-449c25cfd909'),
	 ('e3ca6ccc-1f22-4258-b0f1-d593fc147595','3a3b081e-80ed-46f6-a080-8fa8a9436f6e'),
	 ('e3ca6ccc-1f22-4258-b0f1-d593fc147595','944a7bfa-2f56-4126-9207-7dfcd0873b8d');
	 
INSERT INTO step1.hazard_loss (hazard_id,loss_id) VALUES
	 ('6003135b-ad87-4624-82dd-5e30ec63ae2a','0390fbda-035e-4f1c-987d-d795ee688daf'),
	 ('3a3b081e-80ed-46f6-a080-8fa8a9436f6e','d3e428c7-8bdf-431d-81f8-4948da6ba62a'),
	 ('944a7bfa-2f56-4126-9207-7dfcd0873b8d','d3e428c7-8bdf-431d-81f8-4948da6ba62a'),
	 ('27e81d19-ec78-4e15-ae94-d85174ef1e8f','d3e428c7-8bdf-431d-81f8-4948da6ba62a'),
	 ('34761ce9-1520-481a-8bfb-449c25cfd909','d3e428c7-8bdf-431d-81f8-4948da6ba62a'),
	 ('3a3b081e-80ed-46f6-a080-8fa8a9436f6e','e3196b7d-d351-4943-860d-6eff804af7a4'),
	 ('944a7bfa-2f56-4126-9207-7dfcd0873b8d','e3196b7d-d351-4943-860d-6eff804af7a4'),
	 ('27e81d19-ec78-4e15-ae94-d85174ef1e8f','e3196b7d-d351-4943-860d-6eff804af7a4'),
	 ('34761ce9-1520-481a-8bfb-449c25cfd909','e3196b7d-d351-4943-860d-6eff804af7a4'),
	 ('cf5b648c-fcfe-433e-9805-e513880895ba','152b3bc8-f90c-47e8-a723-6125a719c60c'),
	 ('944a7bfa-2f56-4126-9207-7dfcd0873b8d','152b3bc8-f90c-47e8-a723-6125a719c60c'),
	 ('34761ce9-1520-481a-8bfb-449c25cfd909','152b3bc8-f90c-47e8-a723-6125a719c60c'),
	 ('6003135b-ad87-4624-82dd-5e30ec63ae2a','6e142906-ed68-4a1b-9826-0b45c0b00174'),
	 ('27e81d19-ec78-4e15-ae94-d85174ef1e8f','6e142906-ed68-4a1b-9826-0b45c0b00174'),
	 ('cf5b648c-fcfe-433e-9805-e513880895ba','d1d8e626-c686-4fc6-89c5-cf684ffce2c5'),
	 ('944a7bfa-2f56-4126-9207-7dfcd0873b8d','d1d8e626-c686-4fc6-89c5-cf684ffce2c5'),
	 ('944a7bfa-2f56-4126-9207-7dfcd0873b8d','e15d4220-ad4e-4818-ab68-a915d317450f');