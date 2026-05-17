import { http } from "@http/AxiosConfig";
import { 
    IViolatedSystemSafetyConstraintInsertDto, 
    IViolatedSystemSafetyConstraintReadDto, 
    IViolatedSystemSafetyConstraintUpdateDto 
} from "@interfaces/CAST/IViolatedSystemSafetyConstraint";

const ENDPOINT = "cast-step1/cast/violated-system-safety-constraints";

const getByAnalysisId = async (analysisId: string) => {
    try {
        const res = await http.get<IViolatedSystemSafetyConstraintReadDto[]>(`${ENDPOINT}/analysis/${analysisId}`);
        return res.data;
    } catch (err) {
        console.error(err);
        throw err;
    }
};

const createConstraint = async (data: IViolatedSystemSafetyConstraintInsertDto) => {
    try {
        const res = await http.post<IViolatedSystemSafetyConstraintReadDto>(ENDPOINT, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error creating Constraint.");
    }
};

const updateConstraint = async (id: string, data: IViolatedSystemSafetyConstraintUpdateDto) => {
    try {
        const res = await http.put<IViolatedSystemSafetyConstraintReadDto>(`${ENDPOINT}/${id}`, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw new Error("Error updating Constraint.");
    }
};

const deleteConstraint = async (id: string) => {
    try {
        await http.delete(`${ENDPOINT}/${id}`);
    } catch (err) {
        console.error(err);
        throw new Error("Error deleting Constraint.");
    }
};

export {
    getByAnalysisId,
    createConstraint,
    updateConstraint,
    deleteConstraint
};