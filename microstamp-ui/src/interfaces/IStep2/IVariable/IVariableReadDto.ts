import { IStateReadDto } from "@interfaces/IStep2";

interface IVariableReadDto {
	id: string;
	name: string;
	code: string;
	states: IStateReadDto[];
}

export type { IVariableReadDto };
