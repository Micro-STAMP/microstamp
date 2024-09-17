import { IComponentBorder, IComponentType } from "./Enums";

interface IComponentUpdateDto {
	name: string;
	code: string;
	type: IComponentType;
	border: IComponentBorder;
	isVisible: boolean;
	fatherId: string | null;
}

export type { IComponentUpdateDto };
