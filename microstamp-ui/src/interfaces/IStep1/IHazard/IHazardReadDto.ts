import { ILossReadDto } from "@interfaces/IStep1";

interface IHazardReadDto {
	id: string;
	name: string;
	code: string;
	losses: ILossReadDto[];
	father: IHazardReadDto | null;
}

export type { IHazardReadDto };

