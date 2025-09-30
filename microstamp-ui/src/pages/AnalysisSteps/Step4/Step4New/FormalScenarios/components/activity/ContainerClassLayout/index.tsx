import { ModalFormalClassImages } from "@components/Modal";
import { getFormalClassTitle, IFormalScenariosClass } from "@interfaces/IStep4New/Enums";
import { ClassSection } from "@pages/AnalysisSteps/Step4/Step4New/FormalScenarios/components/activity";
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

	const [formalClass, setFormalClass] = useState<IFormalScenariosClass>(
		IFormalScenariosClass.CLASS_1
	);
	const handleViewClassImage = (className: IFormalScenariosClass) => {
		setFormalClass(className);
		toggleModalClassImage();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<div className={styles.class_layout}>
				<ClassSection
					onViewClassImage={() => handleViewClassImage(IFormalScenariosClass.CLASS_1)}
					title={getFormalClassTitle(IFormalScenariosClass.CLASS_1)}
				>
					{class1Content}
				</ClassSection>
				<ClassSection
					onViewClassImage={() => handleViewClassImage(IFormalScenariosClass.CLASS_2)}
					title={getFormalClassTitle(IFormalScenariosClass.CLASS_2)}
				>
					{class2Content}
				</ClassSection>
				<ClassSection
					onViewClassImage={() => handleViewClassImage(IFormalScenariosClass.CLASS_3)}
					title={getFormalClassTitle(IFormalScenariosClass.CLASS_3)}
				>
					{class3Content}
				</ClassSection>
				<ClassSection
					onViewClassImage={() => handleViewClassImage(IFormalScenariosClass.CLASS_4)}
					title={getFormalClassTitle(IFormalScenariosClass.CLASS_4)}
				>
					{class4Content}
				</ClassSection>
			</div>
			<ModalFormalClassImages
				open={modalClassImageOpen}
				onClose={toggleModalClassImage}
				formalClass={formalClass}
			/>
		</>
	);
}

export default ContainerClassLayout;
