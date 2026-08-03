import { ISystemicFactorCategory } from "./Enums";

interface ISystemicFactorUpdateDto {
	category: ISystemicFactorCategory;
	description: string;
	inadequateControlActionIds: string[];
}

export type { ISystemicFactorUpdateDto };
