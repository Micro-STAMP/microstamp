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

const uploadTimelineEventImage = async (id: string, image: File) => {
    const data = new FormData();
    data.append("file", image);
    try {
        const res = await http.request<ITimelineEventReadDto>({
            url: `${ENDPOINT}/${id}/image`,
            method: "POST",
            headers: { "Content-Type": "multipart/form-data" },
            data: data
        });
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error uploading event image.");
    }
};

const deleteTimelineEventImage = async (id: string) => {
    try {
        await http.delete(`${ENDPOINT}/${id}/image`);
    } catch (err) {
        console.error(err);
        throw new Error("Error deleting event image.");
    }
};

export {
    getByAnalysisId,
    createTimelineEvent,
    updateTimelineEvent,
    deleteTimelineEvent,
    uploadTimelineEventImage,
    deleteTimelineEventImage
};