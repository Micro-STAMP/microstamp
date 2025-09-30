import { ModalStepsMenu } from "@components/Modal";
import StepIcon from "@components/StepIcon";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { getStepLabel, ISteps } from "@interfaces/ISteps";
import { useState } from "react";
import { BiRefresh, BiSolidChevronLeftCircle, BiSolidCog, BiUndo } from "react-icons/bi";
import { BsFillInfoCircleFill } from "react-icons/bs";
import { PiCirclesFourFill as StepsIcon } from "react-icons/pi";
import { useNavigate } from "react-router-dom";
import styles from "./AnalysisHeader.module.css";

interface AnalysisHeaderProps {
	step: ISteps;
	analysis: IAnalysisReadDto;
	component?: string;
	controlAction?: string;
	onChangeControlAction?: () => void;
	uca?: string;
	onChangeUCA?: () => void;
	formalScenarioView?: "class" | "activity";
	onChangeView?: (view: "class" | "activity") => void;
}
function AnalysisHeader({
	analysis,
	step,
	component,
	controlAction,
	uca,
	formalScenarioView,
	onChangeControlAction,
	onChangeUCA,
	onChangeView
}: AnalysisHeaderProps) {
	const navigate = useNavigate();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Step Menu Modal

	const [modalStepsMenuOpen, setModalStepsMenuOpen] = useState(false);
	const toggleModalStepsMenu = () => setModalStepsMenuOpen(!modalStepsMenuOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<header className={styles.analysis_header}>
				<div className={styles.step_wrapper}>
					<div className={styles.step_section}>
						<span className={styles.step_icon}>
							<StepIcon step={step} />
						</span>
						<span className={styles.step_label}>{getStepLabel(step)}</span>
					</div>
					<div className={styles.buttons}>
						<button
							type="button"
							className={styles.step_button}
							onClick={() => toggleModalStepsMenu()}
						>
							<StepsIcon />
							Change Step
						</button>
						<button
							type="button"
							className={styles.back_button}
							onClick={() => navigate(-1)}
						>
							<BiSolidChevronLeftCircle />
							Go Back
						</button>
					</div>
				</div>
				<div className={styles.analysis_wrapper}>
					<div className={styles.title}>
						<BsFillInfoCircleFill />
						<span>Analysis Details:</span>
					</div>
					<div className={styles.analysis_info}>
						<span className={styles.label}>Analysis:</span>
						<span className={styles.name}>{analysis.name}</span>
						<button
							type="button"
							className={styles.change_button}
							onClick={() => navigate(`/analyses/${analysis.id}`)}
							title="Return to analysis page"
						>
							<BiUndo />
						</button>
					</div>
					{component && (
						<div className={styles.analysis_info}>
							<span className={styles.label}>Component:</span>
							<span className={styles.name}>{component}</span>
						</div>
					)}
					{controlAction && (
						<div className={styles.analysis_info}>
							<span className={styles.label}>Control Action:</span>
							<span className={styles.name}>{controlAction}</span>
							{onChangeControlAction && (
								<button
									type="button"
									className={styles.change_button}
									onClick={onChangeControlAction}
									title="Change control action"
								>
									<BiRefresh />
								</button>
							)}
						</div>
					)}
					{uca && (
						<div className={styles.analysis_info}>
							<span className={styles.label}>Unsafe Control Action:</span>
							<span className={styles.name}>{uca}</span>
							{onChangeUCA && (
								<button
									type="button"
									className={styles.change_button}
									onClick={onChangeUCA}
									title="Change UCA"
								>
									<BiRefresh />
								</button>
							)}
						</div>
					)}
				</div>
				{formalScenarioView && onChangeView && (
					<div className={styles.view_wrapper}>
						<span className={styles.view_description}>
							<BiSolidCog />
							Choose how to organize the containers for your analysis:
						</span>
						<button
							className={`${styles.toggle_button} ${
								formalScenarioView === "class" ? styles.active : ""
							}`}
							onClick={() => onChangeView("class")}
							type="button"
						>
							By Class
						</button>
						<button
							className={`${styles.toggle_button} ${
								formalScenarioView === "activity" ? styles.active : ""
							}`}
							onClick={() => onChangeView("activity")}
							type="button"
						>
							By Activity
						</button>
					</div>
				)}
			</header>
			<ModalStepsMenu
				open={modalStepsMenuOpen}
				onClose={toggleModalStepsMenu}
				analysisId={analysis.id}
			/>
		</>
	);
}

export default AnalysisHeader;
