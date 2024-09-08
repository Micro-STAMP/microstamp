SET @guest_id = '2e776941-de10-4aca-98bb-5dabac287229';

SET @b1_id = '52a0730f-15f7-472e-b82e-1936394e49e3';
SET @b2_id = '48d599e6-1e0d-4426-bde1-c4c859eee851';
SET @b3_id = 'b4a1df5f-6404-4b72-a35b-21e35e0265e0';
SET @b4_id = '42d10008-6d14-4275-92f5-90736bba719d';
SET @b5_id = '7e578b44-c0b4-4458-a65e-5fad7595718a';
SET @b6_id = '2feefd14-f678-45b9-8cde-8971f467ee4a';
SET @b7_id = '5d275773-f6cb-4b65-ad5d-bbcf26ea16a2';
SET @b8_id = '64d43d5c-9c8b-4c97-974a-f51df00078b6';
SET @g1_id = 'c5797971-01e1-453d-bbf4-7bc71eda11fd';
SET @g2_id = '6e271a2e-bbba-40ee-a63e-75d746929820';

INSERT INTO analyses (id, name, description, created_at, user_id) VALUES
(@b1_id,"B.1: Control structure for NextGen In-Trail Procedure with new equipment (aviation)",'This analysis illustrates the modeling of the control structure in figure "Figure B.1: Control structure for NextGen In-Trail Procedure with new equipment (aviation)" from Appendix B of the STPA handbook. (https://psas.scripts.mit.edu/home/get_file.php?name=STPA_handbook.pdf)',now(),@guest_id),
(@b2_id,"B.2: Control structure for a Fictional Missile Intercept System",'This analysis illustrates the modeling of the control structure in figure "Figure B.2: Control structure for a Fictional Missile Intercept System" from Appendix B of the STPA handbook. (https://psas.scripts.mit.edu/home/get_file.php?name=STPA_handbook.pdf)',now(),@guest_id),
(@b3_id,"B.3: High-level (abstract) control structure for a proton therapy machine",'This analysis illustrates the modeling of the control structure in figure "Figure B.3: High-level (abstract) control structure for a proton therapy machine" from Appendix B of the STPA handbook. (https://psas.scripts.mit.edu/home/get_file.php?name=STPA_handbook.pdf)',now(),@guest_id),
(@b4_id,"B.4: Zooming into the Treatment Delivery part of Figure B.3 with the Treatment Definition and Patient components shown to provide context",'This analysis illustrates the modeling of the control structure in figure "Figure B.4: Zooming into the Treatment Delivery part of Figure B.3 with the Treatment Definition and Patient components shown to provide context" from Appendix B of the STPA handbook. (https://psas.scripts.mit.edu/home/get_file.php?name=STPA_handbook.pdf)',now(),@guest_id),
(@b5_id,"B.5: U.S. Pharmaceutical Approval Safety Control Structure",'This analysis illustrates the modeling of the control structure in figure "Figure B.5: U.S. Pharmaceutical Approval Safety Control Structure" from Appendix B of the STPA handbook. (https://psas.scripts.mit.edu/home/get_file.php?name=STPA_handbook.pdf)',now(),@guest_id),
(@b6_id,"B.6: Automotive Adaptive Cruise Control System",'This analysis illustrates the modeling of the control structure in figure "Figure B.6: Automotive Adaptive Cruise Control System" from Appendix B of the STPA handbook. (https://psas.scripts.mit.edu/home/get_file.php?name=STPA_handbook.pdf)',now(),@guest_id),
(@b7_id,"B.7: Control Structure for Automotive Auto-Hold System",'This analysis illustrates the modeling of the control structure in figure "Figure B.7: Control Structure for Automotive Auto-Hold System" from Appendix B of the STPA handbook. (https://psas.scripts.mit.edu/home/get_file.php?name=STPA_handbook.pdf)',now(),@guest_id),
(@b8_id,"B.8: Control structure for example autonomous space vehicle operations",'This analysis illustrates the modeling of the control structure in figure "Figure B.8: Control structure for example autonomous space vehicle operations" from Appendix B of the STPA handbook. (https://psas.scripts.mit.edu/home/get_file.php?name=STPA_handbook.pdf)',now(),@guest_id),
(@g1_id,"G.1: Previous General Model to Assist in Generating Causal Scenarios",'This analysis illustrates the modeling of the control structure in figure "Figure G.1: Previous General Model to Assist in Generating Causal Scenarios" from Appendix G of the STPA handbook. (https://psas.scripts.mit.edu/home/get_file.php?name=STPA_handbook.pdf)',now(),@guest_id),
(@g2_id,"G.2: New General Causal Control Model for STPA Causal Analysis Scenario Generation",'This analysis illustrates the modeling of the control structure in figure "Figure G-2. New General Causal Control Model for STPA Causal Analysis Scenario Generation" from Appendix G of the STPA handbook. (https://psas.scripts.mit.edu/home/get_file.php?name=STPA_handbook.pdf)',now(),@guest_id);