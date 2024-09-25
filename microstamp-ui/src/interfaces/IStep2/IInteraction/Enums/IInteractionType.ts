import { SelectOption } from "@components/FormField/Templates";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Interaction Type Enum

enum IInteractionType {
	CONTROL_ACTION = "CONTROL_ACTION",
	FEEDBACK = "FEEDBACK",
	COMMUNICATION_CHANNEL = "COMMUNICATION_CHANNEL",
	PROCESS_INPUT = "PROCESS_INPUT",
	PROCESS_OUTPUT = "PROCESS_OUTPUT",
	DISTURBANCE = "DISTURBANCE"
}
export { IInteractionType };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Interaction Type Select Options

const interactionTypesSelectOptions: SelectOption[] = [
	{ label: "Control Action", value: IInteractionType.CONTROL_ACTION },
	{ label: "Feedback", value: IInteractionType.FEEDBACK },
	{ label: "Communication Channel", value: IInteractionType.COMMUNICATION_CHANNEL },
	{ label: "Process Input", value: IInteractionType.PROCESS_INPUT },
	{ label: "Process Output", value: IInteractionType.PROCESS_OUTPUT },
	{ label: "Disturbance", value: IInteractionType.DISTURBANCE }
];

export { interactionTypesSelectOptions };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Interaction Type To Select Options

const interactionTypeToSelectOptionMap: Record<IInteractionType, SelectOption> = {
	[IInteractionType.CONTROL_ACTION]: interactionTypesSelectOptions[0],
	[IInteractionType.FEEDBACK]: interactionTypesSelectOptions[1],
	[IInteractionType.COMMUNICATION_CHANNEL]: interactionTypesSelectOptions[2],
	[IInteractionType.PROCESS_INPUT]: interactionTypesSelectOptions[3],
	[IInteractionType.PROCESS_OUTPUT]: interactionTypesSelectOptions[4],
	[IInteractionType.DISTURBANCE]: interactionTypesSelectOptions[5]
};
const interactionTypeToSelectOption = (
	interactionType: IInteractionType
): SelectOption => {
	return interactionTypeToSelectOptionMap[interactionType];
};

export { interactionTypeToSelectOption };

/* - - - - - - - - - - - - - - - - - - - - - - */
