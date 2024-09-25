import { IInteractionType } from "./Enums";

interface IInteractionInsertDto {
	name: string;
	code: string;
	interactionType: IInteractionType;
	connectionId: string;
}

export type { IInteractionInsertDto };
