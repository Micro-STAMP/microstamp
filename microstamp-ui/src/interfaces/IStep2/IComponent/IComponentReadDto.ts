import { IResponsibilityReadDto, IVariableReadDto } from "@interfaces/IStep2";
import { IComponentBorder, IComponentType } from "./Enums";

interface IComponentReadDto {
	id: string;
	name: string;
	code: string;
	type: IComponentType;
	border: IComponentBorder;
	isVisible: boolean;
	responsibilities: IResponsibilityReadDto[];
	variables: IVariableReadDto[];
	father: IComponentReadDto | null;
}

export type { IComponentReadDto };
