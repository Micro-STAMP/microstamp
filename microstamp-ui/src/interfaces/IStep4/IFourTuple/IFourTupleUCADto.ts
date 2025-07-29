import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { IFourTupleReadDto } from "./IFourTupleReadDto";

interface IFourTupleUCADto extends IUnsafeControlActionReadDto {
	fourTuples: IFourTupleReadDto[];
}

export type { IFourTupleUCADto };
