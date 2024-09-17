import { IConnectionActionType } from "./Enums";

interface IConnectionActionInsertDto {
	name: string;
	code: string;
	connectionActionType: IConnectionActionType;
	connectionId: string;
}

export type { IConnectionActionInsertDto };
