import { ISystemicFactorCategory } from "./Enums";

interface ISystemicFactorInsertDto {
	analysisId: string;
	category: ISystemicFactorCategory | null;
	customCategory?: string | null;
	description: string;
	controlActionContributionIds: string[];
}

export type { ISystemicFactorInsertDto };
