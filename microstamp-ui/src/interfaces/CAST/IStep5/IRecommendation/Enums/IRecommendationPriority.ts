import { SelectOption } from "@components/FormField/Templates";



enum IRecommendationPriority {
	IMMEDIATE = "IMMEDIATE",
	SHORT_TERM = "SHORT_TERM",
	LONG_TERM = "LONG_TERM"
}

export { IRecommendationPriority };


const recommendationPrioritySelectOptions: SelectOption[] = [
	{ label: "Immediate", value: IRecommendationPriority.IMMEDIATE },
	{ label: "Short-Term Fix", value: IRecommendationPriority.SHORT_TERM },
	{ label: "Long-Term Structural Change", value: IRecommendationPriority.LONG_TERM }
];

export { recommendationPrioritySelectOptions };



const recommendationPriorityToSelectOptionMap: Record<IRecommendationPriority, SelectOption> = {
	[IRecommendationPriority.IMMEDIATE]: recommendationPrioritySelectOptions[0],
	[IRecommendationPriority.SHORT_TERM]: recommendationPrioritySelectOptions[1],
	[IRecommendationPriority.LONG_TERM]: recommendationPrioritySelectOptions[2]
};
const recommendationPriorityToSelectOption = (priority: IRecommendationPriority): SelectOption => {
	return recommendationPriorityToSelectOptionMap[priority];
};

export { recommendationPriorityToSelectOption };
