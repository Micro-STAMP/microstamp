import Container from "@components/Container";
import { ListItem as Scenario } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { IFormalScenariosReadDto } from "@interfaces/IStep4New/IFormalScenarios";
import ContainerClassLayout from "@pages/AnalysisSteps/Step4/Step4New/components/ContainerClassLayout";

interface HighLevelScenariosContainerProps {
	formalScenarios?: IFormalScenariosReadDto;
	isLoading?: boolean;
	isError?: boolean;
}
function HighLevelScenariosContainer({
	formalScenarios,
	isLoading = false,
	isError = false
}: HighLevelScenariosContainerProps) {
	return (
		<Container
			title="4.1 Identify High-Level Scenarios"
			collapsible
			justTitle
			isLoading={isLoading}
			isError={isError || formalScenarios === undefined}
		>
			{formalScenarios && (
				<ContainerClassLayout
					class1Content={
						<ListWrapper>
							<Scenario.Root>
								<Scenario.Name code="Output" name={formalScenarios.class1.output} />
							</Scenario.Root>
							<Scenario.Root>
								<Scenario.Name code="Input" name={formalScenarios.class1.input} />
							</Scenario.Root>
						</ListWrapper>
					}
					class2Content={
						<ListWrapper>
							<Scenario.Root>
								<Scenario.Name code="Output" name={formalScenarios.class2.output} />
							</Scenario.Root>
							<Scenario.Root>
								<Scenario.Name code="Input" name={formalScenarios.class2.input} />
							</Scenario.Root>
						</ListWrapper>
					}
					class3Content={
						<ListWrapper>
							<Scenario.Root>
								<Scenario.Name code="Output" name={formalScenarios.class3.output} />
							</Scenario.Root>
							<Scenario.Root>
								<Scenario.Name code="Input" name={formalScenarios.class3.input} />
							</Scenario.Root>
						</ListWrapper>
					}
					class4Content={
						<ListWrapper>
							<Scenario.Root>
								<Scenario.Name code="Output" name={formalScenarios.class4.output} />
							</Scenario.Root>
							<Scenario.Root>
								<Scenario.Name code="Input" name={formalScenarios.class4.input} />
							</Scenario.Root>
						</ListWrapper>
					}
				/>
			)}
		</Container>
	);
}

export default HighLevelScenariosContainer;
