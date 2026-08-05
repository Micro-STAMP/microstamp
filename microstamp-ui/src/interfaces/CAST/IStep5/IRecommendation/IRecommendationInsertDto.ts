interface IRecommendationInsertDto {
	analysisId: string;
	description: string;
	inadequateControlActionIds: string[];
	systemicFactorIds: string[];
}

export type { IRecommendationInsertDto };
