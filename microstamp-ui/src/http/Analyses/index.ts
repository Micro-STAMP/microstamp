import { http } from "@http/AxiosConfig";
import { IAnalysisInsertDto, IAnalysisReadDto, IAnalysisUpdateDto } from "@interfaces/IAnalysis";

/* - - - - - - - - - - - - - - - - - - - - - - */

const ANALYSES_ENDPOINT = "auth/analyses";

const getAnalyses = async (userId: string) => {
	try {
		const res = await http.get<IAnalysisReadDto[]>(`${ANALYSES_ENDPOINT}/user/${userId}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getAnalysis = async (id: string) => {
	try {
		const res = await http.get<IAnalysisReadDto>(`${ANALYSES_ENDPOINT}/${id}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createAnalysis = async (analysis: IAnalysisInsertDto) => {
	try {
		const res = await http.post<IAnalysisReadDto>(ANALYSES_ENDPOINT, analysis);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating analysis.");
	}
};
const addAnalysisImage = async (image: File, id: string) => {
	const data = new FormData();
	data.append("file", image);
	try {
		const res = await http.request({
			url: `${ANALYSES_ENDPOINT}/${id}/image`,
			method: "POST",
			headers: {
				"Content-Type": "multipart/form-data"
			},
			data: data
		});
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error saving image.");
	}
};
const deleteAnalysisImage = async (id: string) => {
	try {
		await http.delete(`${ANALYSES_ENDPOINT}/${id}/image`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting image.");
	}
};
const updateAnalysis = async (id: string, analysis: IAnalysisUpdateDto) => {
	try {
		const res = await http.put<IAnalysisReadDto>(`${ANALYSES_ENDPOINT}/${id}`, analysis);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating analysis.");
	}
};
const deleteAnalysis = async (id: string) => {
	try {
		await http.delete(`${ANALYSES_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting analysis.");
	}
};

export {
	addAnalysisImage,
	createAnalysis,
	deleteAnalysis,
	deleteAnalysisImage,
	getAnalyses,
	getAnalysis,
	updateAnalysis
};

/* - - - - - - - - - - - - - - - - - - - - - - */
