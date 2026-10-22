import { IIcaType } from "./Enums";

interface IControlActionContributionInsertDto {
	analysisId: string;
	componentId: string;
	code: string;
	controlActionName: string;
	type: IIcaType;
	description?: string;
	context?: string[];
	processModelFlaw?: string[];
	questions?: string[];
}

export type { IControlActionContributionInsertDto };
