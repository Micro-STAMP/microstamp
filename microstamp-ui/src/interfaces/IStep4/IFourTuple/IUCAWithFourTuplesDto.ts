import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { IFourTupleReadDto } from "./IFourTupleReadDto";

interface IUCAWithFourTuplesDto extends IUnsafeControlActionReadDto {
	fourTuples: IFourTupleReadDto[];
}

export type { IUCAWithFourTuplesDto };
