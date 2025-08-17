import Container from "@components/Container";
import { ListItem as Scenario } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { getFormalScenariosByUCA } from "@http/Step4New/FormalScenarios";
import { useQuery } from "@tanstack/react-query";
import { BiSolidCircle } from "react-icons/bi";
import styles from "./HighLevelScenariosContainer.module.css";

interface HighLevelScenariosContainerProps {
	ucaId: string;
}
function HighLevelScenariosContainer({ ucaId }: HighLevelScenariosContainerProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Four Tuples

	const {
		data: formalScenarios,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["formal-scenarios", ucaId],
		queryFn: () => getFormalScenariosByUCA(ucaId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<Container
			title="4.1 Identify High-Level Scenarios"
			collapsible
			justTitle
			isLoading={isLoading}
			isError={isError || formalScenarios === undefined}
		>
			{formalScenarios && (
				<div className={styles.high_level_scenarios}>
					{Object.entries(formalScenarios).map(([classKey, classData]) => (
						<div key={classKey} className={styles.class_item}>
							<span className={styles.title}>
								<BiSolidCircle />
								{classKey.replace("class", "Class ")}:
							</span>
							<ListWrapper>
								<Scenario.Root>
									<Scenario.Name code="Output" name={classData.output} />
								</Scenario.Root>
								<Scenario.Root>
									<Scenario.Name code="Input" name={classData.input} />
								</Scenario.Root>
							</ListWrapper>
						</div>
					))}
				</div>
			)}
		</Container>
	);
}

export default HighLevelScenariosContainer;
