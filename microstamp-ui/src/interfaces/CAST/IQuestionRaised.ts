export type CastStepNumber = 1 | 2 | 3 | 4 | 5;

export interface IQuestionRaisedInsertDto {
    analysisId: string;
    castStep: CastStepNumber;
    code: string;
    question: string;
}

export interface IQuestionRaisedReadDto {
    id: string;
    analysisId: string;
    castStep: CastStepNumber;
    code: string;
    question: string;
    createdAt?: string;
    updatedAt?: string;
}

export interface IQuestionRaisedUpdateDto {
    code: string;
    question: string;
}
