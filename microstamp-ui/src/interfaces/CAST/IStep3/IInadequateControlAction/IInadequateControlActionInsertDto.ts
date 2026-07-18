import { IIcaType } from "./Enums";

interface IInadequateControlActionInsertDto {
	analysisId: string;
	componentId: string;
	code: string;
	controlActionName: string;
	type: IIcaType;
	description?: string;
	context?: string;
	processModelFlaw?: string;
}

export type { IInadequateControlActionInsertDto };
