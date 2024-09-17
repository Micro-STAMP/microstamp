import { SelectOption } from "@components/FormField/Templates";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Connection Action Type Enum

enum IConnectionActionType {
	CONTROL_ACTION = "CONTROL_ACTION",
	FEEDBACK = "FEEDBACK",
	COMMUNICATION_CHANNEL = "COMMUNICATION_CHANNEL",
	PROCESS_INPUT = "PROCESS_INPUT",
	PROCESS_OUTPUT = "PROCESS_OUTPUT",
	DISTURBANCE = "DISTURBANCE"
}
export { IConnectionActionType };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Connection Action Type Select Options

const connectionActionTypesSelectOptions: SelectOption[] = [
	{ label: "Control Action", value: IConnectionActionType.CONTROL_ACTION },
	{ label: "Feedback", value: IConnectionActionType.FEEDBACK },
	{ label: "Communication Channel", value: IConnectionActionType.COMMUNICATION_CHANNEL },
	{ label: "Process Input", value: IConnectionActionType.PROCESS_INPUT },
	{ label: "Process Output", value: IConnectionActionType.PROCESS_OUTPUT },
	{ label: "Disturbance", value: IConnectionActionType.DISTURBANCE }
];

export { connectionActionTypesSelectOptions };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Connection Action Type To Select Options

const connectionActionTypeToSelectOptionMap: Record<IConnectionActionType, SelectOption> = {
	[IConnectionActionType.CONTROL_ACTION]: connectionActionTypesSelectOptions[0],
	[IConnectionActionType.FEEDBACK]: connectionActionTypesSelectOptions[1],
	[IConnectionActionType.COMMUNICATION_CHANNEL]: connectionActionTypesSelectOptions[2],
	[IConnectionActionType.PROCESS_INPUT]: connectionActionTypesSelectOptions[3],
	[IConnectionActionType.PROCESS_OUTPUT]: connectionActionTypesSelectOptions[4],
	[IConnectionActionType.DISTURBANCE]: connectionActionTypesSelectOptions[5]
};
const connectionActionTypeToSelectOption = (
	connectionActionType: IConnectionActionType
): SelectOption => {
	return connectionActionTypeToSelectOptionMap[connectionActionType];
};

export { connectionActionTypeToSelectOption };

/* - - - - - - - - - - - - - - - - - - - - - - */
