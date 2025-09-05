import Button from "@components/Button";
import { ModalSelectStep4 } from "@components/Modal";
import ModalSelectStep3 from "@components/Modal/ModalSelectStep3";
import StepIcon from "@components/StepIcon";
import { ISteps } from "@interfaces/ISteps";
import { useState } from "react";
import { PiCirclesFour as StepsIcon } from "react-icons/pi";
import { useNavigate } from "react-router-dom";
import styles from "./AnalysisStepsMenu.module.css";

interface AnalysisStepsMenuProps {
	analysisId: string;
}
function AnalysisStepsMenu({ analysisId }: AnalysisStepsMenuProps) {
	const navigate = useNavigate();

	const [modalSelectStep3Open, setModalSelectStep3Open] = useState(false);
	const toggleModalSelectStep3 = () => setModalSelectStep3Open(!modalSelectStep3Open);

	const [modalSelectStep4Open, setModalSelectStep4Open] = useState(false);
	const toggleModalSelectStep4 = () => setModalSelectStep4Open(!modalSelectStep4Open);

	const Step1Icon = () => <StepIcon step={ISteps.STEP_1} />;
	const Step2Icon = () => <StepIcon step={ISteps.STEP_2} />;
	const Step3Icon = () => <StepIcon step={ISteps.STEP_3} />;
	const Step4Icon = () => <StepIcon step={ISteps.STEP_4} />;

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
						Define the Purpose of the Analysis
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
						onClick={toggleModalSelectStep3}
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
			<ModalSelectStep3
				open={modalSelectStep3Open}
				onClose={toggleModalSelectStep3}
				analysisId={analysisId}
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
