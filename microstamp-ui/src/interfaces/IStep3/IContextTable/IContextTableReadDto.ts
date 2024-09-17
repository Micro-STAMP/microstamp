import { IContext } from "./IContext";

interface IContextTableReadDto {
	id: string;
	source_id: string;
	target_id: string;
	contexts: IContext[];
	totalPages: number;
	currentPage: number;
}

export type { IContextTableReadDto };
