export interface ITimelineEventInsertDto {
    code: string;
    eventOrder?: number;
    eventDescription: string;
    questions?: string[];
    timeLabel?: string;
    exactDateTime?: string;
    analysisId: string;
}

export interface ITimelineEventReadDto {
    id: string;
    code: string;
    eventOrder?: number;
    eventDescription: string;
    questions?: string[];
    timeLabel?: string;
    exactDateTime?: string;
    imageBase64?: string;
}

export interface ITimelineEventUpdateDto {
    code: string;
    eventOrder?: number;
    eventDescription: string;
    questions?: string[];
    timeLabel?: string;
    exactDateTime?: string;
}