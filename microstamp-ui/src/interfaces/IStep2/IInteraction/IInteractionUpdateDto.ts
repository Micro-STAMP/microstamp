import { IInteractionType } from "./Enums";

interface IInteractionUpdateDto {
	name: string;
	code: string;
	interactionType: IInteractionType;
}

export type { IInteractionUpdateDto };
