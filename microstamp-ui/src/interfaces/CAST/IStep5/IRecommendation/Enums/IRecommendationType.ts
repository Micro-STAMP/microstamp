import { SelectOption } from "@components/FormField/Templates";



enum IRecommendationType {
	SHORT_TERM_FIX = "SHORT_TERM_FIX",
	LONG_TERM_STRUCTURAL = "LONG_TERM_STRUCTURAL",
	ASSUMPTION_REEVALUATION = "ASSUMPTION_REEVALUATION"
}

export { IRecommendationType };



const recommendationTypeSelectOptions: SelectOption[] = [
	{ label: "Short-Term Fix", value: IRecommendationType.SHORT_TERM_FIX },
	{ label: "Long-Term Structural Change", value: IRecommendationType.LONG_TERM_STRUCTURAL },
	{ label: "Assumption Re-evaluation", value: IRecommendationType.ASSUMPTION_REEVALUATION }
];

export { recommendationTypeSelectOptions };



const recommendationTypeToSelectOptionMap: Record<IRecommendationType, SelectOption> = {
	[IRecommendationType.SHORT_TERM_FIX]: recommendationTypeSelectOptions[0],
	[IRecommendationType.LONG_TERM_STRUCTURAL]: recommendationTypeSelectOptions[1],
	[IRecommendationType.ASSUMPTION_REEVALUATION]: recommendationTypeSelectOptions[2]
};
const recommendationTypeToSelectOption = (type: IRecommendationType): SelectOption => {
	return recommendationTypeToSelectOptionMap[type];
};

export { recommendationTypeToSelectOption };
