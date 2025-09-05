import { IFormalScenariosActivity } from "@interfaces/IStep4New/Enums";
import { ActivitySection } from "@pages/AnalysisSteps/Step4/Step4New/FormalScenarios/components/class";
import styles from "./ContainerActivityLayout.module.css";

interface ContainerActivityLayoutProps {
	activity1Content: React.ReactNode;
	activity2Content: React.ReactNode;
	activity3Content: React.ReactNode;
	activity4Content: React.ReactNode;
}
function ContainerActivityLayout({
	activity1Content,
	activity2Content,
	activity3Content,
	activity4Content
}: ContainerActivityLayoutProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<div className={styles.activity_layout}>
				<ActivitySection activity={IFormalScenariosActivity.HIGH_LEVEL_SCENARIOS}>
					{activity1Content}
				</ActivitySection>
				<ActivitySection activity={IFormalScenariosActivity.HIGH_LEVEL_SOLUTIONS}>
					{activity2Content}
				</ActivitySection>
				<ActivitySection activity={IFormalScenariosActivity.REFINED_SCENARIOS}>
					{activity3Content}
				</ActivitySection>
				<ActivitySection activity={IFormalScenariosActivity.REFINED_SOLUTIONS}>
					{activity4Content}
				</ActivitySection>
			</div>
		</>
	);
}

export default ContainerActivityLayout;
