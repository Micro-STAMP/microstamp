import { SelectOption } from "@components/FormField/Templates";

interface IInteractionFormData {
	name: string;
	code: string;
	interactionType: SelectOption | null;
}

export type { IInteractionFormData };
