import { IIcaType } from "./Enums";

interface IInadequateControlActionUpdateDto {
	controlActionName: string;
	type: IIcaType;
	description?: string;
	context?: string;
	processModelFlaw?: string;
}

export type { IInadequateControlActionUpdateDto };
