import { ISystemicFactorCategory } from "./Enums";

interface ISystemicFactorInsertDto {
	analysisId: string;
	category: ISystemicFactorCategory;
	description: string;
	controlActionContributionIds: string[];
}

export type { ISystemicFactorInsertDto };
