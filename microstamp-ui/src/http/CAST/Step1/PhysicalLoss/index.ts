import { http } from "@http/AxiosConfig";
import { 
    IPhysicalLossAnalysisInsertDto, 
    IPhysicalLossAnalysisReadDto, 
    IPhysicalLossAnalysisUpdateDto 
} from "@interfaces/CAST/IPhysicalLossAnalysis";


const ENDPOINT = "cast-step1/cast/physical-loss-analyses";

const getByAnalysisId = async (analysisId: string) => {
    try {
        const res = await http.get<IPhysicalLossAnalysisReadDto[]>(`${ENDPOINT}/analysis/${analysisId}`);
        return res.data;
    } catch (err) {
        console.error(err);
        throw err;
    }
};

const createPhysicalLoss = async (data: IPhysicalLossAnalysisInsertDto) => {
    try {
        const res = await http.post<IPhysicalLossAnalysisReadDto>(ENDPOINT, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error creating Physical Loss Analysis.");
    }
};

const updatePhysicalLoss = async (id: string, data: IPhysicalLossAnalysisUpdateDto) => {
    try {
        const res = await http.put<IPhysicalLossAnalysisReadDto>(`${ENDPOINT}/${id}`, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error updating Physical Loss Analysis.");
    }
};

const deletePhysicalLoss = async (id: string) => {
    try {
        await http.delete(`${ENDPOINT}/${id}`);
    } catch (err) {
        console.error(err);
        throw new Error("Error deleting Physical Loss Analysis.");
    }
};

export {
    getByAnalysisId,
    createPhysicalLoss,
    updatePhysicalLoss,
    deletePhysicalLoss
};