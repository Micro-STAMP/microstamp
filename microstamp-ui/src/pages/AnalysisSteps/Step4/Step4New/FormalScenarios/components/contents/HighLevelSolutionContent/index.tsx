import Button from "@components/Button";
import { ModalHighLevelSolutions } from "@components/Modal/ModalEntity";
import { IFormalScenarioClassDto } from "@interfaces/IStep4New/IFormalScenarios";
import { IHighLevelSolutionsReadDto } from "@interfaces/IStep4New/IHighLevelSolutions";
import { useState } from "react";
import { BiSolidEditAlt } from "react-icons/bi";
import styles from "./HighLevelSolutionContent.module.css";

interface HighLevelSolutionContent {
	formalScenario: IFormalScenarioClassDto;
	solution: IHighLevelSolutionsReadDto;
}
function HighLevelSolutionContent({ formalScenario, solution }: HighLevelSolutionContent) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update High Level Solution

	const [modalUpdateHighLevelSolutionsOpen, setModalUpdateHighLevelSolutionsOpen] =
		useState(false);
	const toggleModalUpdateHighLevelSolutions = () =>
		setModalUpdateHighLevelSolutionsOpen(!modalUpdateHighLevelSolutionsOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<div className={styles.hls_content}>
				<div className={styles.hls_item}>
					<span className={styles.hls_title}>Controller Behavior:</span>
					<span className={styles.hls_value}>{solution.controllerBehavior || "-"}</span>
				</div>
				<div className={styles.hls_item}>
					<span className={styles.hls_title}>Process Behavior:</span>
					<span className={styles.hls_value}>{solution.processBehavior || "-"}</span>
				</div>
				<div className={styles.hls_item}>
					<span className={styles.hls_title}>Other Solutions:</span>
					<span className={styles.hls_value}>{solution.otherSolutions || "-"}</span>
				</div>
				<div className={styles.actions}>
					<Button
						icon={BiSolidEditAlt}
						iconPosition="left"
						variant="dark"
						onClick={() => toggleModalUpdateHighLevelSolutions()}
						size="small"
					>
						Update High-Level Solutions
					</Button>
				</div>
			</div>
			<ModalHighLevelSolutions
				formalScenario={formalScenario}
				highLevelSolutions={solution}
				open={modalUpdateHighLevelSolutionsOpen}
				onClose={toggleModalUpdateHighLevelSolutions}
			/>
		</>
	);
}

export default HighLevelSolutionContent;
