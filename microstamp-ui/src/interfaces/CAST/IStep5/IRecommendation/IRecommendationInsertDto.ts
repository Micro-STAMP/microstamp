import { IRecommendationPriority, IRecommendationStatus, IRecommendationType, IRecommendationScopeCategory } from "./Enums";

interface IRecommendationInsertDto {
	analysisId: string;
	description: string;
	controlActionContributionIds: string[];
	systemicFactorIds: string[];
	componentId?: string;
	priority?: IRecommendationPriority;
	auditMechanism?: string;
	assignee?: string;
	dueDate?: string;
	status?: IRecommendationStatus;
	recommendationType?: IRecommendationType;
	scopeCategory?: IRecommendationScopeCategory;
	effectivenessEvidence?: string;
}

export type { IRecommendationInsertDto };
