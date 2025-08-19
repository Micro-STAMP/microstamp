import { IUCAType } from "@interfaces/IStep3";

interface IRefinedScenarioTemplateDto {
	id: string;
	template: string;
	unsafeControlActionType: IUCAType | null;
}

export type { IRefinedScenarioTemplateDto };
