import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";

interface IFourTupleReadDto {
	id: string;
	scenario: string;
	associatedCausalFactor: string;
	recommendation: string;
	rationale: string;
	unsafeControlActions: IUnsafeControlActionReadDto[];
	code: string;
}

export type { IFourTupleReadDto };
