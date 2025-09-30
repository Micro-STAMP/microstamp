import { SelectOption } from "@components/FormField/Templates";
import { IRefinedScenarioReadDto } from "./IRefinedScenarioReadDto";

const refinedScenarioToSelectOption = (refinedScenario: IRefinedScenarioReadDto): SelectOption => {
	return {
		label: `${refinedScenario.refinedScenario}`,
		value: refinedScenario.id
	};
};
const refinedScenariosToSelectOptions = (
	refinedScenarios: IRefinedScenarioReadDto[]
): SelectOption[] => {
	return refinedScenarios.map(refinedScenario => refinedScenarioToSelectOption(refinedScenario));
};

export { refinedScenariosToSelectOptions, refinedScenarioToSelectOption };
