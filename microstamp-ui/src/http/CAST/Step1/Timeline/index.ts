import { http } from "@http/AxiosConfig";
import { 
    ITimelineEventInsertDto, 
    ITimelineEventReadDto, 
    ITimelineEventUpdateDto 
} from "@interfaces/CAST/ITimelineEvent";

const ENDPOINT = "cast-step1/cast/timeline-events";

const getByAnalysisId = async (analysisId: string) => {
    try {
        const res = await http.get<ITimelineEventReadDto[]>(`${ENDPOINT}/analysis/${analysisId}`);
        return res.data;
    } catch (err) {
        console.error(err);
        throw err;
    }
};

const createTimelineEvent = async (data: ITimelineEventInsertDto) => {
    try {
        const res = await http.post<ITimelineEventReadDto>(ENDPOINT, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error creating Timeline Event.");
    }
};

const updateTimelineEvent = async (id: string, data: ITimelineEventUpdateDto) => {
    try {
        const res = await http.put<ITimelineEventReadDto>(`${ENDPOINT}/${id}`, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error updating Timeline Event.");
    }
};

const deleteTimelineEvent = async (id: string) => {
    try {
        await http.delete(`${ENDPOINT}/${id}`);
    } catch (err) {
        console.error(err);
        throw new Error("Error deleting Timeline Event.");
    }
};

export {
    getByAnalysisId,
    createTimelineEvent,
    updateTimelineEvent,
    deleteTimelineEvent
};