import { IConnectionStyle } from "./Enums";

interface IConnectionInsertDto {
	code: string;
	sourceId: string;
	targetId: string;
	style: IConnectionStyle;
	analysisId: string;
}

export type { IConnectionInsertDto };
