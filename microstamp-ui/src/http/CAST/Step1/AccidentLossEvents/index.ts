import { http } from "@http/AxiosConfig";
import { 
    IAccidentLossEventInsertDto, 
    IAccidentLossEventReadDto, 
    IAccidentLossEventUpdateDto 
} from "@interfaces/CAST/IAccidentLossEvent";

const ENDPOINT = "cast-step1/cast/accident-loss-events";

const getByAnalysisId = async (analysisId: string) => {
    try {
        const res = await http.get<IAccidentLossEventReadDto[]>(`${ENDPOINT}/analysis/${analysisId}`);
        return res.data;
    } catch (err) {
        console.error(err);
        throw err;
    }
};

const createLossEvent = async (data: IAccidentLossEventInsertDto) => {
    try {
        const res = await http.post<IAccidentLossEventReadDto>(ENDPOINT, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error creating Accident/Loss Event.");
    }
};

const updateLossEvent = async (id: string, data: IAccidentLossEventUpdateDto) => {
    try {
        const res = await http.put<IAccidentLossEventReadDto>(`${ENDPOINT}/${id}`, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error updating Accident/Loss Event.");
    }
};

const deleteLossEvent = async (id: string) => {
    try {
        await http.delete(`${ENDPOINT}/${id}`);
    } catch (err) {
        console.error(err);
        throw new Error("Error deleting Accident/Loss Event.");
    }
};

export {
    getByAnalysisId,
    createLossEvent,
    updateLossEvent,
    deleteLossEvent
};