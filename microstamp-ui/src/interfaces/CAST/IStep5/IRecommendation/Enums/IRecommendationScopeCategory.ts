import { SelectOption } from "@components/FormField/Templates";



enum IRecommendationScopeCategory {
	PHYSICAL_DESIGN = "PHYSICAL_DESIGN",
	SOFTWARE_AUTOMATION = "SOFTWARE_AUTOMATION",
	WORK_INSTRUCTIONS_PROCEDURES = "WORK_INSTRUCTIONS_PROCEDURES",
	MANAGEMENT_OF_CHANGE_MOC = "MANAGEMENT_OF_CHANGE_MOC",
	SAFETY_INFORMATION_SYSTEM_SIS = "SAFETY_INFORMATION_SYSTEM_SIS",
	SAFETY_CULTURE = "SAFETY_CULTURE",
	REGULATION_OVERSIGHT = "REGULATION_OVERSIGHT"
}

export { IRecommendationScopeCategory };



const recommendationScopeCategorySelectOptions: SelectOption[] = [
	{ label: "Physical Design & Equipment", value: IRecommendationScopeCategory.PHYSICAL_DESIGN },
	{ label: "Software, Algorithms & Automation", value: IRecommendationScopeCategory.SOFTWARE_AUTOMATION },
	{ label: "Work Instructions, Training & Procedures", value: IRecommendationScopeCategory.WORK_INSTRUCTIONS_PROCEDURES },
	{ label: "Management of Change (MOC)", value: IRecommendationScopeCategory.MANAGEMENT_OF_CHANGE_MOC },
	{ label: "Safety Information System (SIS)", value: IRecommendationScopeCategory.SAFETY_INFORMATION_SYSTEM_SIS },
	{ label: "Safety Culture & Management", value: IRecommendationScopeCategory.SAFETY_CULTURE },
	{ label: "Regulation & External Oversight", value: IRecommendationScopeCategory.REGULATION_OVERSIGHT }
];

export { recommendationScopeCategorySelectOptions };



const recommendationScopeCategoryToSelectOptionMap: Record<IRecommendationScopeCategory, SelectOption> = {
	[IRecommendationScopeCategory.PHYSICAL_DESIGN]: recommendationScopeCategorySelectOptions[0],
	[IRecommendationScopeCategory.SOFTWARE_AUTOMATION]: recommendationScopeCategorySelectOptions[1],
	[IRecommendationScopeCategory.WORK_INSTRUCTIONS_PROCEDURES]: recommendationScopeCategorySelectOptions[2],
	[IRecommendationScopeCategory.MANAGEMENT_OF_CHANGE_MOC]: recommendationScopeCategorySelectOptions[3],
	[IRecommendationScopeCategory.SAFETY_INFORMATION_SYSTEM_SIS]: recommendationScopeCategorySelectOptions[4],
	[IRecommendationScopeCategory.SAFETY_CULTURE]: recommendationScopeCategorySelectOptions[5],
	[IRecommendationScopeCategory.REGULATION_OVERSIGHT]: recommendationScopeCategorySelectOptions[6]
};
const recommendationScopeCategoryToSelectOption = (category: IRecommendationScopeCategory): SelectOption => {
	return recommendationScopeCategoryToSelectOptionMap[category];
};

export { recommendationScopeCategoryToSelectOption };
