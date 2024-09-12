INSERT INTO variables (id, code, name, component_id) VALUES
('dd6627a0-30f9-4188-9488-50a905427f4e', 'V.01', 'Reservoir level', '67857dc1-ae1c-45c1-a36e-f01376d53769'),
('fce80ce3-8328-4cef-b335-a2d505cbc5f7', 'V.02', 'Battery level', '67857dc1-ae1c-45c1-a36e-f01376d53769'),
('8e0e3eae-49b4-4ad6-86f3-5b5800d696e7', 'V.03', 'Pump operational status', '67857dc1-ae1c-45c1-a36e-f01376d53769'),
('02538a23-c77e-4b59-94e9-a8a1876d83f1', 'V.04', 'Basal Rate', '67857dc1-ae1c-45c1-a36e-f01376d53769'),
('65fb2b8d-925e-42ee-b9fe-3f85d08c90e1', 'V.01', 'Glucose level', '8d666300-6ab4-4022-8d05-2e10ec23680f');


INSERT INTO states (id, code, name, variable_id) VALUES
('1255f2fa-416a-4b6f-b52c-26fba404c30c', 'S.01', 'Below', '02538a23-c77e-4b59-94e9-a8a1876d83f1'),
('d869a433-7bbb-4bb8-aa49-ca550af5ae17', 'S.02', 'Normal', '02538a23-c77e-4b59-94e9-a8a1876d83f1'),
('343b7ca7-4f8f-439b-b424-64c1becb7863', 'S.03', 'Above', '02538a23-c77e-4b59-94e9-a8a1876d83f1'),
('f09b690b-29d8-49f7-9f16-3a252d751b90', 'S.01', 'Below', '65fb2b8d-925e-42ee-b9fe-3f85d08c90e1'),
('dc941db1-57b7-4504-b493-e4ab2a439a65', 'S.02', 'Normal', '65fb2b8d-925e-42ee-b9fe-3f85d08c90e1'),
('63601cd8-3f56-4c88-965b-0bbdedd4bc40', 'S.03', 'Above', '65fb2b8d-925e-42ee-b9fe-3f85d08c90e1'),
('aeda5d2c-3c07-4617-81a7-9934d48ac677', 'S.01', 'Transmitting', '8e0e3eae-49b4-4ad6-86f3-5b5800d696e7'),
('76eb0c9b-e051-4118-b5e8-1c60a577078d', 'S.02', 'Not Transmitting', '8e0e3eae-49b4-4ad6-86f3-5b5800d696e7'),
('7955e4c3-3770-4977-ac2a-11112143141a', 'S.01', 'Below', 'dd6627a0-30f9-4188-9488-50a905427f4e'),
('4a6726b1-3694-4df4-89ff-5b2f8a8cb34c', 'S.02', 'Normal', 'dd6627a0-30f9-4188-9488-50a905427f4e'),
('c339b988-e20c-4ba0-98d1-3c140ab8e767', 'S.01', 'Below', 'fce80ce3-8328-4cef-b335-a2d505cbc5f7'),
('df04829e-d2d5-4a0d-9fdc-0d97f62446c8', 'S.02', 'Normal', 'fce80ce3-8328-4cef-b335-a2d505cbc5f7');