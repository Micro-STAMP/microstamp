import { IStateReadDto } from "@interfaces/IStep2";

interface IContext {
	id: string;
	states: IStateReadDto[];
}

export type { IContext };
