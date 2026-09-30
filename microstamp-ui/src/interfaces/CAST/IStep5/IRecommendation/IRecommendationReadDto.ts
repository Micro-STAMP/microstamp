import { IRecommendationPriority } from "./Enums";

interface IRecommendationReadDto {
	id: string;
	analysisId: string;
	description: string;
	inadequateControlActionIds: string[];
	systemicFactorIds: string[];
	componentId?: string;
	priority?: IRecommendationPriority;
	auditMechanism?: string;
}

export type { IRecommendationReadDto };
