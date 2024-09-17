import { IComponentBorder, IComponentType } from "./Enums";

interface IComponentInsertDto {
	name: string;
	code: string;
	analysisId: string;
	type: IComponentType;
	border: IComponentBorder;
	isVisible: boolean;
	fatherId: string | null;
}

export type { IComponentInsertDto };
