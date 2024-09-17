import { SelectOption } from "@components/FormField/Templates";

interface IComponentFormData {
	name: string;
	code: string;
	type: SelectOption | null;
	border: SelectOption | null;
	isVisible: boolean;
	father: SelectOption | null;
}

export type { IComponentFormData };
