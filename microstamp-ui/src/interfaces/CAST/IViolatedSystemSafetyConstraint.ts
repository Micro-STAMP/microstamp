import { ICastHazardReadDto } from "./ICastHazard";

export interface IViolatedSystemSafetyConstraintInsertDto {
    code: string;
    name: string;
    description?: string;
    hazardIds?: string[];
    analysisId: string;
}

export interface IViolatedSystemSafetyConstraintReadDto {
    id: string;
    code: string;
    name: string;
    description?: string;
    hazards?: ICastHazardReadDto[];
}

export interface IViolatedSystemSafetyConstraintUpdateDto {
    code: string;
    name: string;
    description?: string;
    hazardIds?: string[];
}