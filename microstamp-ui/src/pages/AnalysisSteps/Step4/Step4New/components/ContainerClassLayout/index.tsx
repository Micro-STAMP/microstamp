import ClassSection from "@pages/AnalysisSteps/Step4/Step4New/components/ClassSection";
import styles from "./ContainerClassLayout.module.css";

interface ContainerClassLayoutProps {
	class1Content: React.ReactNode;
	class2Content: React.ReactNode;
	class3Content: React.ReactNode;
	class4Content: React.ReactNode;
}
function ContainerClassLayout({
	class1Content,
	class2Content,
	class3Content,
	class4Content
}: ContainerClassLayoutProps) {
	return (
		<div className={styles.class_layout}>
			<ClassSection title="Class 1">{class1Content}</ClassSection>
			<ClassSection title="Class 2">{class2Content}</ClassSection>
			<ClassSection title="Class 3">{class3Content}</ClassSection>
			<ClassSection title="Class 4">{class4Content}</ClassSection>
		</div>
	);
}

export default ContainerClassLayout;
