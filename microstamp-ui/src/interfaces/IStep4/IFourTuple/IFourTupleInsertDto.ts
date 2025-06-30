interface IFourTupleInsertDto {
	scenario: string;
	associatedCausalFactor: string;
	recommendation: string;
	rationale: string;
	unsafeControlActionIds: string[];
	code: string;
	analysisId: string;
}

export type { IFourTupleInsertDto };
