import { http } from "@http/AxiosConfig";
import { castRequestError } from "@utils/castHttpError";
import {
    IQuestionRaisedInsertDto,
    IQuestionRaisedReadDto,
    IQuestionRaisedUpdateDto
} from "@interfaces/CAST/IQuestionRaised";

const ENDPOINT = "cast-step1/cast/questions-raised";

const getByAnalysisId = async (analysisId: string) => {
    try {
        const res = await http.get<IQuestionRaisedReadDto[]>(`${ENDPOINT}/analysis/${analysisId}`);
        return res.data;
    } catch (err) {
        console.error(err);
        throw err;
    }
};

const createQuestionRaised = async (data: IQuestionRaisedInsertDto) => {
    try {
        const res = await http.post<IQuestionRaisedReadDto>(ENDPOINT, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw castRequestError(err, "Error creating Question Raised.");
    }
};

const updateQuestionRaised = async (id: string, data: IQuestionRaisedUpdateDto) => {
    try {
        const res = await http.put<IQuestionRaisedReadDto>(`${ENDPOINT}/${id}`, data);
        return res.data;
    } catch (err) {
        console.error(err);
        throw castRequestError(err, "Error updating Question Raised.");
    }
};

const deleteQuestionRaised = async (id: string) => {
    try {
        await http.delete(`${ENDPOINT}/${id}`);
    } catch (err) {
        console.error(err);
        throw new Error("Error deleting Question Raised.");
    }
};

export {
    getByAnalysisId,
    createQuestionRaised,
    updateQuestionRaised,
    deleteQuestionRaised
};
