import { SelectOption } from "@components/FormField/Templates";

interface IFourTupleFormData {
	scenario: string;
	associatedCausalFactor: string;
	recommendation: string;
	rationale: string;
	unsafeControlActions: SelectOption[];
	code: string;
	analysisId: string;
}

export type { IFourTupleFormData };
