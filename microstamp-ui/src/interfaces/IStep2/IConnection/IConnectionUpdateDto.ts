import { IConnectionStyle } from "./Enums";

interface IConnectionUpdateDto {
	code: string;
	sourceId: string;
	targetId: string;
	style: IConnectionStyle;
}

export type { IConnectionUpdateDto };
