export interface ISystemDescriptionInsertDto {
    description: string;
    analysisBoundary?: string;
    analysisId: string;
}

export interface ISystemDescriptionReadDto {
    id: string;
    description: string;
    analysisBoundary?: string;
}

export interface ISystemDescriptionUpdateDto {
    description: string;
    analysisBoundary?: string;
}
