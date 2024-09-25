import { IInteractionType } from "./Enums";

interface IInteractionReadDto {
	id: string;
	name: string;
	code: string;
	interactionType: IInteractionType;
}

export type { IInteractionReadDto };
