import { SelectOption } from "@components/FormField/Templates";

interface IUnsafeControlActionFormData {
	states: SelectOption[];
	hazard: SelectOption | null;
	type: SelectOption | null;
}

export type { IUnsafeControlActionFormData };
