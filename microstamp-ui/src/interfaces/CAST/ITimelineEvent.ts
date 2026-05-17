export interface ITimelineEventInsertDto {
    code: string;
    eventOrder?: number;
    eventDescription: string;
    questions?: string;
    analysisId: string;
}

export interface ITimelineEventReadDto {
    id: string;
    code: string;
    eventOrder?: number;
    eventDescription: string;
    questions?: string;
}

export interface ITimelineEventUpdateDto {
    code: string;
    eventOrder?: number;
    eventDescription: string;
    questions?: string;
}