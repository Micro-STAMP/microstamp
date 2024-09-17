import { IConnectionActionType } from "./Enums";

interface IConnectionActionReadDto {
	id: string;
	name: string;
	code: string;
	connectionActionType: IConnectionActionType;
}

export type { IConnectionActionReadDto };
