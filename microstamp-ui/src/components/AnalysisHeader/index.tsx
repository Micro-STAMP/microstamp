import StepIcon from "@components/StepIcon";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { getStepLabel, ISteps } from "@interfaces/ISteps";
import { BiRefresh, BiSolidChevronLeftCircle, BiSolidCog } from "react-icons/bi";
import { BsFillInfoCircleFill } from "react-icons/bs";
import { useNavigate } from "react-router-dom";
import styles from "./AnalysisHeader.module.css";

interface AnalysisHeaderProps {
	step: ISteps;
	analysis: IAnalysisReadDto;
	component?: string;
	onChangeComponent?: () => void;
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
	onChangeComponent,
	onChangeControlAction,
	onChangeUCA,
	onChangeView
}: AnalysisHeaderProps) {
	const navigate = useNavigate();

	return (
		<header className={styles.analysis_header}>
			<div className={styles.step_wrapper}>
				<div className={styles.step_section}>
					<span className={styles.step_icon}>
						<StepIcon step={step} />
					</span>
					<span className={styles.step_label}>{getStepLabel(step)}</span>
				</div>
				<button type="button" className={styles.back_button} onClick={() => navigate(-1)}>
					<BiSolidChevronLeftCircle />
					Go Back
				</button>
			</div>
			<div className={styles.analysis_wrapper}>
				<div className={styles.title}>
					<BsFillInfoCircleFill />
					<span>Analysis Details:</span>
				</div>
				<div className={styles.analysis_info}>
					<span className={styles.label}>Analysis:</span>
					<span className={styles.name}>{analysis.name}</span>
				</div>
				{component && (
					<div className={styles.analysis_info}>
						<span className={styles.label}>Component:</span>
						<span className={styles.name}>{component}</span>
						{onChangeComponent && (
							<button
								type="button"
								className={styles.change_button}
								onClick={onChangeComponent}
								title="Change component"
							>
								<BiRefresh />
							</button>
						)}
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
	);
}

export default AnalysisHeader;
