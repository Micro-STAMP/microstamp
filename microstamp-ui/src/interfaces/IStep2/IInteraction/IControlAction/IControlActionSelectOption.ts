import { SelectOption } from "@components/FormField/Templates";
import { IControlAction } from "@interfaces/IStep2";

const controlActionToSelectOption = (controlAction: IControlAction): SelectOption => {
	return {
		label: `${controlAction.code}: ${controlAction.name}`,
		value: controlAction.id
	};
};
const controlActionsToSelectOptions = (controlActions: IControlAction[]): SelectOption[] => {
	return controlActions.map(controlAction => controlActionToSelectOption(controlAction));
};

export { controlActionsToSelectOptions, controlActionToSelectOption };
