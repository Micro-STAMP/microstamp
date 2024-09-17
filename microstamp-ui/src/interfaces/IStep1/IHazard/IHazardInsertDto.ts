interface IHazardInsertDto {
	name: string;
	code: string;
	analysisId: string;
	lossIds: string[];
	fatherId: string | null;
}

export type { IHazardInsertDto };
