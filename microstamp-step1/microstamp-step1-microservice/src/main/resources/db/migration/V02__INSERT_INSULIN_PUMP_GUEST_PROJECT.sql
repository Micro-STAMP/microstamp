INSERT INTO system_goals (id, code, name, analysis_id)
VALUES("d5c1c19e-d593-4332-b5e3-8c2e844ce37a", "G-1", "Monitor the patient’s glucose level", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO system_goals (id, code, name, analysis_id)
VALUES("f4a067b0-d334-47cd-957a-e2dd27c4bfb6", "G-2", "Control the injection of insulin", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO system_goals (id, code, name, analysis_id)
VALUES("61d419f7-aa44-4f2f-a97a-6b1dfa3bc7b5", "G-3", "Provide alerts about the system’s operation", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");


INSERT INTO assumptions (id, code, name, analysis_id)
VALUES("3293ada4-82c8-4bbf-b7ef-a8f106aba556", "A-1", "The system operates with a smartphone with Internet connection", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO assumptions (id, code, name, analysis_id)
VALUES("70dd1ed9-b5f7-4d0b-bd9f-f6efc2ad7e6b", "A-2", "The smartphone has an app to aid the control", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");


INSERT INTO losses (id, code, name, analysis_id)
VALUES("f95a3565-1611-43b8-9a5e-c3790f20b4c9", "L-1", "Patient is injured or killed from overdose or underdose", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO losses (id, code, name, analysis_id)
VALUES("ed7bb288-0a2a-4264-aa68-7885ef084921", "L-2", "Loss of the manufacturer’s credibility", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO losses (id, code, name, analysis_id)
VALUES("f36aa774-eb53-4e68-a5de-3f04e875517f", "L-3", "Loss of personal information (e.g. level of glucose, amount of glucose, and etc.)", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");


INSERT INTO system_safety_constraints (id, code, name, analysis_id)
VALUES("988fb832-ad47-48e8-afcb-2be853c0eb41", "SSC-1", "The pumping of insulin must be stopped when the glucose level goes below a configurable minimum level (for both Bolus and Basal)", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO system_safety_constraints (id, code, name, analysis_id)
VALUES("88a142df-57e8-462e-8c20-8c9eb6ac5a13", "SSC-2", "The system must automatically start pumping insulin after reaching some maximum configurable level", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO system_safety_constraints (id, code, name, analysis_id)
VALUES("b6c317fd-ad1d-42aa-b2c4-c9ea90f23ddf", "SSC-3", "The system must send an alert when detects a battery or reservoir level near the below level", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO system_safety_constraints (id, code, name, analysis_id)
VALUES("5ada36fc-527d-4624-9d87-5faa810dca1b", "SSC-4", "The system must never exposure patient data without the patient's consent", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO system_safety_constraints (id, code, name, analysis_id)
VALUES("c7e3f849-8ec1-4542-9f66-6f437999928f", "SSC-5", "Reservoir must be filled only with the recommended insulin", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO system_safety_constraints (id, code, name, analysis_id)
VALUES("331ced2a-1cec-4419-b3bb-e31495e5420f", "SSC-6", "Mobile device always must be paired with insulin pump", "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");


INSERT INTO hazards (id, code, name, father_id, analysis_id)
VALUES("48552ec1-1d4e-474b-8808-77658967b8cb", "H-1", "Pumping insulin when glucose level is going down – hypoglycemia", null, "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO hazards (id, code, name, father_id, analysis_id)
VALUES("73331fdb-f9a4-4538-b5cf-4a73decdb92e", "H-2", "Not pumping insulin when glucose level is going up – hyperglycemia", null, "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO hazards (id, code, name, father_id, analysis_id)
VALUES("e7de763e-694f-4eb4-9a19-cd4e9ac1f93d", "H-3", "System in operation with battery or reservoir level below recommended values", null, "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO hazards (id, code, name, father_id, analysis_id)
VALUES("8c733bab-70ee-4456-a3c3-4a62c28cfffe", "H-4", "Disclosure sensitive information (exposure of patient data)", null, "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO hazards (id, code, name, father_id, analysis_id)
VALUES("1573a7cc-f560-4dfb-a6d0-bef702175f93", "H-5", "Reservoir filled with not recommended insulin or another product", null, "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");

INSERT INTO hazards (id, code, name, father_id, analysis_id)
VALUES("e3cde296-38e9-4164-82ab-17f43ab8c10a", "H-6", "Mobile device not paired with insulin pump", null, "6cf687f7-0f2d-4f0f-8df4-d3668bdf1a45");


INSERT INTO hazard_loss (hazard_id, loss_id)
VALUES("48552ec1-1d4e-474b-8808-77658967b8cb", "f95a3565-1611-43b8-9a5e-c3790f20b4c9");

INSERT INTO hazard_loss (hazard_id, loss_id)
VALUES("73331fdb-f9a4-4538-b5cf-4a73decdb92e", "f95a3565-1611-43b8-9a5e-c3790f20b4c9");

INSERT INTO hazard_loss (hazard_id, loss_id)
VALUES("e7de763e-694f-4eb4-9a19-cd4e9ac1f93d", "f95a3565-1611-43b8-9a5e-c3790f20b4c9");

INSERT INTO hazard_loss (hazard_id, loss_id)
VALUES("8c733bab-70ee-4456-a3c3-4a62c28cfffe", "ed7bb288-0a2a-4264-aa68-7885ef084921");

INSERT INTO hazard_loss (hazard_id, loss_id)
VALUES("8c733bab-70ee-4456-a3c3-4a62c28cfffe", "f36aa774-eb53-4e68-a5de-3f04e875517f");

INSERT INTO hazard_loss (hazard_id, loss_id)
VALUES("1573a7cc-f560-4dfb-a6d0-bef702175f93", "f95a3565-1611-43b8-9a5e-c3790f20b4c9");

INSERT INTO hazard_loss (hazard_id, loss_id)
VALUES("e3cde296-38e9-4164-82ab-17f43ab8c10a", "f95a3565-1611-43b8-9a5e-c3790f20b4c9");

INSERT INTO hazard_loss (hazard_id, loss_id)
VALUES("e3cde296-38e9-4164-82ab-17f43ab8c10a", "ed7bb288-0a2a-4264-aa68-7885ef084921");

INSERT INTO system_safety_constraint_hazard (system_safety_constraint_id, hazard_id)
VALUES("988fb832-ad47-48e8-afcb-2be853c0eb41", "48552ec1-1d4e-474b-8808-77658967b8cb");

INSERT INTO system_safety_constraint_hazard (system_safety_constraint_id, hazard_id)
VALUES("88a142df-57e8-462e-8c20-8c9eb6ac5a13", "73331fdb-f9a4-4538-b5cf-4a73decdb92e");

INSERT INTO system_safety_constraint_hazard (system_safety_constraint_id, hazard_id)
VALUES("b6c317fd-ad1d-42aa-b2c4-c9ea90f23ddf", "e7de763e-694f-4eb4-9a19-cd4e9ac1f93d");

INSERT INTO system_safety_constraint_hazard (system_safety_constraint_id, hazard_id)
VALUES("5ada36fc-527d-4624-9d87-5faa810dca1b", "8c733bab-70ee-4456-a3c3-4a62c28cfffe");

INSERT INTO system_safety_constraint_hazard (system_safety_constraint_id, hazard_id)
VALUES("c7e3f849-8ec1-4542-9f66-6f437999928f", "1573a7cc-f560-4dfb-a6d0-bef702175f93");

INSERT INTO system_safety_constraint_hazard (system_safety_constraint_id, hazard_id)
VALUES("331ced2a-1cec-4419-b3bb-e31495e5420f", "e3cde296-38e9-4164-82ab-17f43ab8c10a");