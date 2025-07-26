interface IFourTupleReadDto {
	id: string;
	scenario: string;
	associatedCausalFactor: string;
	recommendation: string;
	rationale: string;
	unsafeControlActionIds: string[];
	code: string;
}

export type { IFourTupleReadDto };
