export interface ISystemDescriptionInsertDto {
    code: string;
    description: string;
    analysisBoundary?: string; 
    analysisId: string;        
}

export interface ISystemDescriptionReadDto {
    id: string;
    code: string;
    description: string;
    analysisBoundary?: string;
}

export interface ISystemDescriptionUpdateDto {
    code: string;
    description: string;
    analysisBoundary?: string;
}