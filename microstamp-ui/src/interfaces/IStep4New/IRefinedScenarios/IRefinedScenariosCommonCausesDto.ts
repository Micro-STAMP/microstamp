import { IRefinedScenarioTemplateDto } from "./IRefinedScenarioTemplateDto";

interface IRefinedScenariosCommonCausesDto {
	id: string;
	code: string;
	cause: string;
	templates: IRefinedScenarioTemplateDto[];
}

export type { IRefinedScenariosCommonCausesDto };
