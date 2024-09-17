import { SelectOption } from "@components/FormField/Templates";

interface IConnectionActionFormData {
	name: string;
	code: string;
	connectionActionType: SelectOption | null;
}

export type { IConnectionActionFormData };
