import { IComponentReadDto, IInteractionReadDto } from "@interfaces/IStep2";
import { IConnectionStyle } from "./Enums";

interface IConnectionReadDto {
	id: string;
	code: string;
	source: IComponentReadDto;
	target: IComponentReadDto;
	style: IConnectionStyle;
	interactions: IInteractionReadDto[];
}

export type { IConnectionReadDto };
