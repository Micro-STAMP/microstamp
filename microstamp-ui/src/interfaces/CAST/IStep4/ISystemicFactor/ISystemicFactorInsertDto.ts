import { ISystemicFactorCategory } from "./Enums";

interface ISystemicFactorInsertDto {
	analysisId: string;
	category: ISystemicFactorCategory;
	description: string;
	inadequateControlActionIds: string[];
}

export type { ISystemicFactorInsertDto };
