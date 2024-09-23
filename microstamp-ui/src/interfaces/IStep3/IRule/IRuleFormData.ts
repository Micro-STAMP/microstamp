import { SelectOption } from "@components/FormField/Templates";

interface IRuleFormData {
	name: string;
	code: string;
	states: SelectOption[];
	types: SelectOption[];
	hazard: SelectOption | null;
}

export type { IRuleFormData };
