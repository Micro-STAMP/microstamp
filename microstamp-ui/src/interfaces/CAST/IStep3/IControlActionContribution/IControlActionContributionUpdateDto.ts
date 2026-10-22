import { IIcaType } from "./Enums";

interface IControlActionContributionUpdateDto {
	controlActionName: string;
	type: IIcaType;
	description?: string;
	context?: string[];
	processModelFlaw?: string[];
	questions?: string[];
}

export type { IControlActionContributionUpdateDto };
