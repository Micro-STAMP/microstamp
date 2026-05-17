import { http } from "@http/AxiosConfig";
import { 
    ISystemDescriptionInsertDto, 
    ISystemDescriptionReadDto, 
    ISystemDescriptionUpdateDto 
} from "@interfaces/CAST/ISystemDescription";

const ENDPOINT = "cast-step1/cast/system-descriptions";

const getByAnalysisId = async (analysisId: string) => {
    try {
        const res = await http.get<ISystemDescriptionReadDto[]>(`${ENDPOINT}/analysis/${analysisId}`);
        return res.data;
    } catch (err) {
        console.error(err);
        throw err;
    }
};

const createSystemDescription = async (data: ISystemDescriptionInsertDto) => {
    try {
        const res = await http.post<ISystemDescriptionReadDto>(ENDPOINT, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error creating System Description.");
    }
};

const updateSystemDescription = async (id: string, data: ISystemDescriptionUpdateDto) => {
    try {
        const res = await http.put<ISystemDescriptionReadDto>(`${ENDPOINT}/${id}`, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error updating System Description.");
    }
};

const deleteSystemDescription = async (id: string) => {
    try {
        await http.delete(`${ENDPOINT}/${id}`);
    } catch (err) {
        console.error(err);
        throw new Error("Error deleting System Description.");
    }
};

export {
    getByAnalysisId,
    createSystemDescription,
    updateSystemDescription,
    deleteSystemDescription
};