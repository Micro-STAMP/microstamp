import { IIcaType } from "./Enums";

interface IInadequateControlActionReadDto {
	id: string;
	analysisId: string;
	componentId: string;
	code: string;
	controlActionName: string;
	type: IIcaType;
	description?: string;
	context?: string;
	processModelFlaw?: string;
}

export type { IInadequateControlActionReadDto };
