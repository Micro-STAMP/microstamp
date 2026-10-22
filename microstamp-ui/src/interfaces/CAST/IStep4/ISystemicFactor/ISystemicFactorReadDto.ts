import { ISystemicFactorCategory } from "./Enums";

interface ISystemicFactorReadDto {
	id: string;
	analysisId: string;
	category: ISystemicFactorCategory;
	description: string;
	controlActionContributionIds: string[];
}

export type { ISystemicFactorReadDto };
