import { ISystemicFactorCategory } from "./Enums";

interface ISystemicFactorUpdateDto {
	category: ISystemicFactorCategory;
	description: string;
	controlActionContributionIds: string[];
}

export type { ISystemicFactorUpdateDto };
