import { SelectOption } from "@components/FormField/Templates";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Component Type Enum

enum IComponentType {
	CONTROLLER = "CONTROLLER",
	CONTROLLED_PROCESS = "CONTROLLED_PROCESS",
	ACTUATOR = "ACTUATOR",
	SENSOR = "SENSOR",
	ENVIRONMENT = "ENVIRONMENT"
}

export { IComponentType };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Component Border Select Options

const componentTypeSelectOptions: SelectOption[] = [
	{ label: "Controller", value: IComponentType.CONTROLLER },
	{ label: "Controlled Process", value: IComponentType.CONTROLLED_PROCESS },
	{ label: "Actuator", value: IComponentType.ACTUATOR },
	{ label: "Sensor", value: IComponentType.SENSOR },
	{ label: "Environment", value: IComponentType.ENVIRONMENT }
];

export { componentTypeSelectOptions };

/* - - - - - - - - - - - - - - - - - - - - - - */

// Type to Select Option

const componentTypeToSelectOptionMap: Record<IComponentType, SelectOption> = {
	[IComponentType.CONTROLLER]: componentTypeSelectOptions[0],
	[IComponentType.CONTROLLED_PROCESS]: componentTypeSelectOptions[1],
	[IComponentType.ACTUATOR]: componentTypeSelectOptions[2],
	[IComponentType.SENSOR]: componentTypeSelectOptions[3],
	[IComponentType.ENVIRONMENT]: componentTypeSelectOptions[4]
};
const componentTypeToSelectOption = (componentType: IComponentType): SelectOption => {
	return componentTypeToSelectOptionMap[componentType];
};

export { componentTypeToSelectOption };

/* - - - - - - - - - - - - - - - - - - - - - - */
