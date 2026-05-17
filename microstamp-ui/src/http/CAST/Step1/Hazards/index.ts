import { http } from "@http/AxiosConfig";
import { 
    ICastHazardInsertDto, 
    ICastHazardReadDto, 
    ICastHazardUpdateDto 
} from "@interfaces/CAST/ICastHazard";

const ENDPOINT = "cast-step1/cast/hazards";

const getByAnalysisId = async (analysisId: string) => {
    try {
        const res = await http.get<ICastHazardReadDto[]>(`${ENDPOINT}/analysis/${analysisId}`);
        return res.data;
    } catch (err) {
        console.error(err);
        throw err;
    }
};

const createHazard = async (data: ICastHazardInsertDto) => {
    try {
        const res = await http.post<ICastHazardReadDto>(ENDPOINT, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error creating Hazard.");
    }
};

const updateHazard = async (id: string, data: ICastHazardUpdateDto) => {
    try {
        const res = await http.put<ICastHazardReadDto>(`${ENDPOINT}/${id}`, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error updating Hazard.");
    }
};

const deleteHazard = async (id: string) => {
    try {
        await http.delete(`${ENDPOINT}/${id}`);
    } catch (err) {
        console.error(err);
        throw new Error("Error deleting Hazard.");
    }
};

export {
    getByAnalysisId,
    createHazard,
    updateHazard,
    deleteHazard
};