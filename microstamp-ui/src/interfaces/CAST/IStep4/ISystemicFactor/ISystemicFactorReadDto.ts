import { ISystemicFactorCategory } from "./Enums";

interface ISystemicFactorReadDto {
	id: string;
	analysisId: string;
	category: ISystemicFactorCategory;
	description: string;
	inadequateControlActionIds: string[];
}

export type { ISystemicFactorReadDto };
