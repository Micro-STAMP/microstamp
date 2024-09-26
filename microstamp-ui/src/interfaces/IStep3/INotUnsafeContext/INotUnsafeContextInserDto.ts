import { IUCAType } from "@interfaces/IStep3";

interface INotUnsafeContextInsertDto {
	statesIds: string[];
	type: IUCAType;
	controlActionId: string;
	analysisId: string;
}

export type { INotUnsafeContextInsertDto };
