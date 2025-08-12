import Button from "@components/Button";
import { ModalSelectControlAction, ModalSelectStep4 } from "@components/Modal";
import { useState } from "react";
import { GoGoal as Step1Icon } from "react-icons/go";
import { IoWarningOutline as Step3Icon } from "react-icons/io5";
import { PiTreeStructure as Step2Icon, PiCirclesFour as StepsIcon } from "react-icons/pi";
import { RiArrowGoBackLine as Step4Icon } from "react-icons/ri";
import { useNavigate } from "react-router-dom";
import styles from "./AnalysisStepsMenu.module.css";

interface AnalysisStepsMenuProps {
	analysisId: string;
}
function AnalysisStepsMenu({ analysisId }: AnalysisStepsMenuProps) {
	const navigate = useNavigate();

	const [modalSelectControlActionOpen, setModalSelectControlActionOpen] = useState(false);
	const toggleModalSelectControlAction = () =>
		setModalSelectControlActionOpen(!modalSelectControlActionOpen);

	const [modalSelectStep4Open, setModalSelectStep4Open] = useState(false);
	const toggleModalSelectStep4 = () => setModalSelectStep4Open(!modalSelectStep4Open);

	return (
		<>
			<div className={styles.steps_menu}>
				<span className={styles.name}>
					<StepsIcon className={styles.icon} />
					Analysis Steps
				</span>
				<div className={styles.steps_buttons}>
					<Button
						size="small"
						icon={Step1Icon}
						variant="dark"
						onClick={() => navigate("purpose")}
					>
						Define Purpose of the Analysis
					</Button>
					<Button
						size="small"
						icon={Step2Icon}
						variant="dark"
						onClick={() => navigate("control-structure")}
					>
						Model the Control Structure
					</Button>
					<Button
						size="small"
						icon={Step3Icon}
						variant="dark"
						onClick={toggleModalSelectControlAction}
					>
						Identify Unsafe Control Actions
					</Button>
					<Button
						size="small"
						icon={Step4Icon}
						variant="dark"
						onClick={() => {
							toggleModalSelectStep4();
						}}
					>
						Identify Loss Scenarios
					</Button>
				</div>
			</div>
			<ModalSelectControlAction
				analysisId={analysisId}
				open={modalSelectControlActionOpen}
				onClose={toggleModalSelectControlAction}
			/>
			<ModalSelectStep4
				open={modalSelectStep4Open}
				onClose={toggleModalSelectStep4}
				analysisId={analysisId}
			/>
		</>
	);
}

export default AnalysisStepsMenu;
