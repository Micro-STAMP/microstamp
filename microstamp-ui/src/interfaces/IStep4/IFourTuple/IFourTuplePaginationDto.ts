import { IFourTupleReadDto } from "./IFourTupleReadDto";

interface IFourTuplePaginationDto {
	totalPages: number;
	currentPage: number;
	fourTuples: IFourTupleReadDto[];
}

export type { IFourTuplePaginationDto };
