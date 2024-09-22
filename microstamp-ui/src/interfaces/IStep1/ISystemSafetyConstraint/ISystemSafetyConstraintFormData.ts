import { SelectOption } from "@components/FormField/Templates";

interface ISystemSafetyConstraintFormData {
	name: string;
	code: string;
	hazards: SelectOption[];
}

export type { ISystemSafetyConstraintFormData };
