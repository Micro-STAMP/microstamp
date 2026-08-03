import { SelectOption } from "@components/FormField/Templates";



enum ISystemicFactorCategory {
	COMMUNICATION_AND_COORDINATION = "COMMUNICATION_AND_COORDINATION",
	SAFETY_INFORMATION_SYSTEM = "SAFETY_INFORMATION_SYSTEM",
	SAFETY_MANAGEMENT_SYSTEM_DESIGN = "SAFETY_MANAGEMENT_SYSTEM_DESIGN",
	SAFETY_CULTURE = "SAFETY_CULTURE",
	CHANGES_AND_DYNAMICS_OVER_TIME = "CHANGES_AND_DYNAMICS_OVER_TIME",
	ECONOMIC_AND_ENVIRONMENTAL_FACTORS = "ECONOMIC_AND_ENVIRONMENTAL_FACTORS"
}

export { ISystemicFactorCategory };


const systemicFactorCategorySelectOptions: SelectOption[] = [
	{ label: "Communication and Coordination", value: ISystemicFactorCategory.COMMUNICATION_AND_COORDINATION },
	{ label: "Safety Information System", value: ISystemicFactorCategory.SAFETY_INFORMATION_SYSTEM },
	{ label: "Design of the Safety Management System", value: ISystemicFactorCategory.SAFETY_MANAGEMENT_SYSTEM_DESIGN },
	{ label: "Safety Culture", value: ISystemicFactorCategory.SAFETY_CULTURE },
	{ label: "Changes and Dynamics over Time", value: ISystemicFactorCategory.CHANGES_AND_DYNAMICS_OVER_TIME },
	{ label: "Economic and Environmental Factors", value: ISystemicFactorCategory.ECONOMIC_AND_ENVIRONMENTAL_FACTORS }
];

export { systemicFactorCategorySelectOptions };



const systemicFactorCategoryToSelectOptionMap: Record<ISystemicFactorCategory, SelectOption> = {
	[ISystemicFactorCategory.COMMUNICATION_AND_COORDINATION]: systemicFactorCategorySelectOptions[0],
	[ISystemicFactorCategory.SAFETY_INFORMATION_SYSTEM]: systemicFactorCategorySelectOptions[1],
	[ISystemicFactorCategory.SAFETY_MANAGEMENT_SYSTEM_DESIGN]: systemicFactorCategorySelectOptions[2],
	[ISystemicFactorCategory.SAFETY_CULTURE]: systemicFactorCategorySelectOptions[3],
	[ISystemicFactorCategory.CHANGES_AND_DYNAMICS_OVER_TIME]: systemicFactorCategorySelectOptions[4],
	[ISystemicFactorCategory.ECONOMIC_AND_ENVIRONMENTAL_FACTORS]: systemicFactorCategorySelectOptions[5]
};
const systemicFactorCategoryToSelectOption = (category: ISystemicFactorCategory): SelectOption => {
	return systemicFactorCategoryToSelectOptionMap[category];
};

export { systemicFactorCategoryToSelectOption };
