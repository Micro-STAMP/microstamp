import { IComponentReadDto, IConnectionActionReadDto } from "@interfaces/IStep2";
import { IConnectionStyle } from "./Enums";

interface IConnectionReadDto {
	id: string;
	code: string;
	source: IComponentReadDto;
	target: IComponentReadDto;
	style: IConnectionStyle;
	connectionActions: IConnectionActionReadDto[];
}

export type { IConnectionReadDto };
