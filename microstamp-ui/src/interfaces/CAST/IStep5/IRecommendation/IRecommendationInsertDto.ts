import { IRecommendationPriority } from "./Enums";

interface IRecommendationInsertDto {
	analysisId: string;
	description: string;
	inadequateControlActionIds: string[];
	systemicFactorIds: string[];
	componentId?: string;
	priority?: IRecommendationPriority;
	auditMechanism?: string;
}

export type { IRecommendationInsertDto };
