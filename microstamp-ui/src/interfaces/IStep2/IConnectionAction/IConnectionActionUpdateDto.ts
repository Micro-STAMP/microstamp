import { IConnectionActionType } from "./Enums";

interface IConnectionActionUpdateDto {
	name: string;
	code: string;
	connectionActionType: IConnectionActionType;
}

export type { IConnectionActionUpdateDto };
