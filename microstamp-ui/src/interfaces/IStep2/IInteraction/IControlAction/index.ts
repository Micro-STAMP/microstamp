import { IConnectionReadDto } from "@interfaces/IStep2";

interface IControlAction {
	id: string;
	name: string;
	code: string;
	connection: IConnectionReadDto;
}

export type { IControlAction };
