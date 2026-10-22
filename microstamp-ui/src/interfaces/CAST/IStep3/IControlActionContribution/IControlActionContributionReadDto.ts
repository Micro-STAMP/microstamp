import { IIcaType } from "./Enums";

interface IControlActionContributionReadDto {
	id: string;
	analysisId: string;
	componentId: string;
	code: string;
	controlActionName: string;
	type: IIcaType;
	description?: string;
	context?: string;
	processModelFlaw?: string;
	questions?: string;
}

export type { IControlActionContributionReadDto };
