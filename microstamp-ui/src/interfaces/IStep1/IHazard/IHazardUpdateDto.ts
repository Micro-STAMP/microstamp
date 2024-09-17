interface IHazardUpdateDto {
	name: string;
	code: string;
	lossIds: string[];
	fatherId: string | null;
}

export type { IHazardUpdateDto };
