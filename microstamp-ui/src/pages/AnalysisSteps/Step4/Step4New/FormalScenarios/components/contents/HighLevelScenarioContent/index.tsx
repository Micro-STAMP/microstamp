import { ListItem as Scenario } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { IFormalScenarioClassDto } from "@interfaces/IStep4New/IFormalScenarios";

interface HighLevelScenarioContentProps {
	formalScenario: IFormalScenarioClassDto;
}
function HighLevelScenarioContent({ formalScenario }: HighLevelScenarioContentProps) {
	return (
		<>
			<ListWrapper>
				<Scenario.Root>
					<Scenario.Name code="Output" name={formalScenario.output} />
				</Scenario.Root>
				<Scenario.Root>
					<Scenario.Name code="Input" name={formalScenario.input} />
				</Scenario.Root>
			</ListWrapper>
		</>
	);
}

export default HighLevelScenarioContent;
