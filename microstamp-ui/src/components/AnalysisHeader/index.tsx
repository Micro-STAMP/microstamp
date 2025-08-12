import { getAnalysis } from "@http/Analyses";
import { useQuery } from "@tanstack/react-query";
import { BiAnalyse as AnalysesIcon, BiSolidLeftArrowCircle as GoBackIcon } from "react-icons/bi";
import { GoGoal as Step1Icon } from "react-icons/go";
import { IoWarningOutline as Step3Icon } from "react-icons/io5";
import { PiTreeStructure as Step2Icon } from "react-icons/pi";
import { RiArrowGoBackLine as Step4Icon } from "react-icons/ri";
import { useNavigate } from "react-router-dom";
import styles from "./AnalysisHeader.module.css";

type AnalysisIconType = "step1" | "step2" | "step3" | "step4" | "default";
const Icon = ({ type }: { type: AnalysisIconType }) => {
	if (type === "default") return <AnalysesIcon className={styles.analysis_icon} />;
	if (type === "step1") return <Step1Icon className={styles.analysis_icon} />;
	if (type === "step2") return <Step2Icon className={styles.analysis_icon} />;
	if (type === "step3") return <Step3Icon className={styles.analysis_icon} />;
	if (type === "step4") return <Step4Icon className={styles.analysis_icon} />;
};

interface AnalysisHeaderProps {
	analysisId: string;
	component?: string;
	controlAction?: string;
	uca?: string;
	icon?: AnalysisIconType;
}
function AnalysisHeader({
	analysisId,
	component,
	controlAction,
	uca,
	icon = "default"
}: AnalysisHeaderProps) {
	const navigate = useNavigate();

	const { data: analysis, isLoading } = useQuery({
		queryKey: ["analysis-header", analysisId],
		queryFn: () => getAnalysis(analysisId)
	});

	return (
		<header className={styles.analysis_header}>
			<div className={styles.analysis}>
				<Icon type={icon} />
				<div className={styles.analysis_name}>
					<strong>Analysis: </strong>
					<span>{isLoading || analysis === undefined ? "Loading" : analysis.name}</span>
				</div>
				{component && (
					<div className={styles.component_name}>
						<strong>Component: </strong>
						<span>{component}</span>
					</div>
				)}
				{controlAction && (
					<div className={styles.component_name}>
						<strong>Control Action: </strong>
						<span>{controlAction}</span>
					</div>
				)}
				{uca && (
					<div className={styles.component_name}>
						<strong>UCA: </strong>
						<span>{uca}</span>
					</div>
				)}
			</div>
			<button type="button" className={styles.back_button} onClick={() => navigate(-1)}>
				<GoBackIcon className={styles.icon} />
				<span>Go Back</span>
			</button>
		</header>
	);
}

export default AnalysisHeader;
