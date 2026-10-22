export interface ISystemDescriptionInsertDto {
    description: string;
    analysisBoundary?: string;
    generalDescription?: string;
    analysisId: string;
}

export interface ISystemDescriptionReadDto {
    id: string;
    description: string;
    analysisBoundary?: string;
    generalDescription?: string;
}

export interface ISystemDescriptionUpdateDto {
    description: string;
    analysisBoundary?: string;
    generalDescription?: string;
}
