import { SelectOption } from "@components/FormField/Templates";

interface IHazardFormData {
	name: string;
	code: string;
	losses: SelectOption[];
	father: SelectOption | null;
}

export type { IHazardFormData };
