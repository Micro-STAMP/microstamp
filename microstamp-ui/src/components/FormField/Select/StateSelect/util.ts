import { SelectOption } from "@components/FormField/Templates";
import { IStateReadDto } from "@interfaces/IStep2";
import { truncateText } from "@services/text";

const stateToSelectOption = (state: IStateReadDto): SelectOption => {
	return {
		label: `${state.code}: ${truncateText(state.name, 30)}`,
		value: state.id
	};
};
const statesToSelectOptions = (states: IStateReadDto[]): SelectOption[] => {
	return states.map(state => stateToSelectOption(state));
};

export { statesToSelectOptions, stateToSelectOption };
