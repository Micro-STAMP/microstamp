interface IRecommendationReadDto {
	id: string;
	analysisId: string;
	description: string;
	inadequateControlActionIds: string[];
	systemicFactorIds: string[];
}

export type { IRecommendationReadDto };
