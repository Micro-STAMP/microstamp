import Button from "@components/Button";
import { ModalSelectControlAction } from "@components/Modal";
import { useState } from "react";
import { GoGoal as Step1Icon } from "react-icons/go";
import { IoWarningOutline as Step3Icon } from "react-icons/io5";
import { PiTreeStructure as Step2Icon, PiCirclesFour as StepsIcon } from "react-icons/pi";
import { RiArrowGoBackLine as Step4Icon } from "react-icons/ri";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import styles from "./AnalysisStepsMenu.module.css";

interface AnalysisStepsMenuProps {
	analysisId: string;
}
function AnalysisStepsMenu({ analysisId }: AnalysisStepsMenuProps) {
	const navigate = useNavigate();

	const [modalSelectControlActionOpen, setModalSelectControlActionOpen] = useState(false);
	const toggleModalSelectControlAction = () =>
		setModalSelectControlActionOpen(!modalSelectControlActionOpen);

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
							// navigate("loss-scenarios")
							toast.info(
								"Step 4 has not been implemented yet, but will be added soon."
							);
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
		</>
	);
}

export default AnalysisStepsMenu;
