import { SelectOption } from "@components/FormField/Templates";

interface IConnectionFormData {
	code: string;
	source: SelectOption | null;
	target: SelectOption | null;
	style: SelectOption | null;
}

export type { IConnectionFormData };
