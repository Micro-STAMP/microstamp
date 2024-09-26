import { IStateReadDto } from "@interfaces/IStep2";
import { IUCAType } from "@interfaces/IStep3";

interface INotUnsafeContextReadDto {
	id: string;
	analysis_id: string;
	states: IStateReadDto[];
	type: IUCAType;
}

export type { INotUnsafeContextReadDto };
