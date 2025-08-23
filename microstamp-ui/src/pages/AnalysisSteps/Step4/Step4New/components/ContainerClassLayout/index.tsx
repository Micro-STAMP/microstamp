import { ModalClassImages } from "@components/Modal";
import ClassSection from "@pages/AnalysisSteps/Step4/Step4New/components/ClassSection";
import { useState } from "react";
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
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle View Class Image Modal

	const [modalClassImageOpen, setModalClassImageOpen] = useState(false);
	const toggleModalClassImage = () => setModalClassImageOpen(!modalClassImageOpen);

	const [formalClass, setFormalClass] = useState<"class1" | "class2" | "class3" | "class4">(
		"class1"
	);
	const handleViewClassImage = (className: typeof formalClass) => {
		setFormalClass(className);
		toggleModalClassImage();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<div className={styles.class_layout}>
				<ClassSection
					onViewClassImage={() => handleViewClassImage("class1")}
					title="Class 1"
				>
					{class1Content}
				</ClassSection>
				<ClassSection
					onViewClassImage={() => handleViewClassImage("class2")}
					title="Class 2"
				>
					{class2Content}
				</ClassSection>
				<ClassSection
					onViewClassImage={() => handleViewClassImage("class3")}
					title="Class 3"
				>
					{class3Content}
				</ClassSection>
				<ClassSection
					onViewClassImage={() => handleViewClassImage("class4")}
					title="Class 4"
				>
					{class4Content}
				</ClassSection>
			</div>
			<ModalClassImages
				open={modalClassImageOpen}
				onClose={toggleModalClassImage}
				formalClass={formalClass}
			/>
		</>
	);
}

export default ContainerClassLayout;
