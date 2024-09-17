import { SelectOption } from "@components/FormField/Templates";

interface IResponsibilityFormData {
	responsibility: string;
	code: string;
	systemSafetyConstraint: SelectOption | null;
}

export type { IResponsibilityFormData };
