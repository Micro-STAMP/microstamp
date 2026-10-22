import { ISystemicFactorCategory } from "./Enums";

interface ISystemicFactorReadDto {
	id: string;
	analysisId: string;
	category: ISystemicFactorCategory | null;
	customCategory?: string | null;
	description: string;
	controlActionContributionIds: string[];
}

export type { ISystemicFactorReadDto };
