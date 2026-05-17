export interface IAccidentLossEventInsertDto {
    code: string;
    name: string;
    description?: string;
    analysisId: string;
}

export interface IAccidentLossEventReadDto {
    id: string;
    code: string;
    name: string;
    description?: string;
}

export interface IAccidentLossEventUpdateDto {
    code: string;
    name: string;
    description?: string;
}