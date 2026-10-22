import { ISystemicFactorCategory } from "./Enums";

interface ISystemicFactorUpdateDto {
	category: ISystemicFactorCategory | null;
	customCategory?: string | null;
	description: string;
	controlActionContributionIds: string[];
}

export type { ISystemicFactorUpdateDto };
