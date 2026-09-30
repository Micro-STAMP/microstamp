import { IRecommendationPriority } from "./Enums";

interface IRecommendationUpdateDto {
	description: string;
	inadequateControlActionIds: string[];
	systemicFactorIds: string[];
	componentId?: string;
	priority?: IRecommendationPriority;
	auditMechanism?: string;
}

export type { IRecommendationUpdateDto };
