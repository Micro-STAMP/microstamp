import { http } from "@http/AxiosConfig";
import {
	IRecommendationInsertDto,
	IRecommendationReadDto,
	IRecommendationUpdateDto
} from "@interfaces/CAST/IStep5/IRecommendation";

const ENDPOINT = "cast-step5/recommendations";

const getByAnalysisId = async (analysisId: string) => {
	try {
		const res = await http.get<IRecommendationReadDto[]>(
			`${ENDPOINT}/analysis/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};

const createRecommendation = async (data: IRecommendationInsertDto) => {
	try {
		const res = await http.post<IRecommendationReadDto>(ENDPOINT, data);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating Recommendation.");
	}
};

const updateRecommendation = async (
	id: string,
	data: IRecommendationUpdateDto
) => {
	try {
		const res = await http.put<IRecommendationReadDto>(`${ENDPOINT}/${id}`, data);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating Recommendation.");
	}
};

const deleteRecommendation = async (id: string) => {
	try {
		await http.delete(`${ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting Recommendation.");
	}
};

export {
	getByAnalysisId,
	createRecommendation,
	updateRecommendation,
	deleteRecommendation
};
