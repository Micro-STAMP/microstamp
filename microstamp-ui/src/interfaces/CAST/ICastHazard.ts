import { IAccidentLossEventReadDto } from "./IAccidentLossEvent";

export interface ICastHazardInsertDto {
    code: string;
    name: string;
    description?: string;
    accidentLossEventIds?: string[];
    analysisId: string;
}

export interface ICastHazardReadDto {
    id: string;
    code: string;
    name: string;
    description?: string;
    accidentLossEvents?: IAccidentLossEventReadDto[];
}

export interface ICastHazardUpdateDto {
    code: string;
    name: string;
    description?: string;
    accidentLossEventIds?: string[];
}