import { SelectOption } from "@components/FormField/Templates";



enum IRecommendationStatus {
	PROPOSED = "PROPOSED",
	IN_PROGRESS = "IN_PROGRESS",
	IMPLEMENTED = "IMPLEMENTED",
	EFFECTIVE = "EFFECTIVE"
}

export { IRecommendationStatus };



const recommendationStatusSelectOptions: SelectOption[] = [
	{ label: "Proposed", value: IRecommendationStatus.PROPOSED },
	{ label: "In Progress", value: IRecommendationStatus.IN_PROGRESS },
	{ label: "Implemented", value: IRecommendationStatus.IMPLEMENTED },
	{ label: "Effective (Audited)", value: IRecommendationStatus.EFFECTIVE }
];

export { recommendationStatusSelectOptions };



const recommendationStatusToSelectOptionMap: Record<IRecommendationStatus, SelectOption> = {
	[IRecommendationStatus.PROPOSED]: recommendationStatusSelectOptions[0],
	[IRecommendationStatus.IN_PROGRESS]: recommendationStatusSelectOptions[1],
	[IRecommendationStatus.IMPLEMENTED]: recommendationStatusSelectOptions[2],
	[IRecommendationStatus.EFFECTIVE]: recommendationStatusSelectOptions[3]
};
const recommendationStatusToSelectOption = (status: IRecommendationStatus): SelectOption => {
	return recommendationStatusToSelectOptionMap[status];
};

export { recommendationStatusToSelectOption };
