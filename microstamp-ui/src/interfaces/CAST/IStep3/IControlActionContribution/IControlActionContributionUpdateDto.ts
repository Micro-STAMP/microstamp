import { IIcaType } from "./Enums";

interface IControlActionContributionUpdateDto {
	code: string;
	controlActionName: string;
	type: IIcaType;
	description?: string;
	context?: string[];
	processModelFlaw?: string[];
	questions?: string[];
}

export type { IControlActionContributionUpdateDto };
