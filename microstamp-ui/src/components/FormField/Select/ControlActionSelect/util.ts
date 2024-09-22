import { SelectOption } from "@components/FormField/Templates";
import { IControlAction } from "@interfaces/IStep2";
import { truncateText } from "@services/text";

const controlActionToSelectOption = (controlAction: IControlAction): SelectOption => {
	return {
		label: `${controlAction.code}: ${truncateText(controlAction.name, 30)}`,
		value: controlAction.id
	};
};
const controlActionsToSelectOptions = (controlActions: IControlAction[]): SelectOption[] => {
	return controlActions.map(controlAction => controlActionToSelectOption(controlAction));
};

export { controlActionsToSelectOptions, controlActionToSelectOption };
