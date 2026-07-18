import { http } from "@http/AxiosConfig";
import {
	IInadequateControlActionInsertDto,
	IInadequateControlActionReadDto,
	IInadequateControlActionUpdateDto
} from "@interfaces/CAST/IStep3/IInadequateControlAction";

const ENDPOINT = "cast-step3/inadequate-control-actions";

const getByAnalysisId = async (analysisId: string) => {
	try {
		const res = await http.get<IInadequateControlActionReadDto[]>(
			`${ENDPOINT}/analysis/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};

const createInadequateControlAction = async (data: IInadequateControlActionInsertDto) => {
	try {
		const res = await http.post<IInadequateControlActionReadDto>(ENDPOINT, data);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating Inadequate Control Action.");
	}
};

const updateInadequateControlAction = async (
	id: string,
	data: IInadequateControlActionUpdateDto
) => {
	try {
		const res = await http.put<IInadequateControlActionReadDto>(`${ENDPOINT}/${id}`, data);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating Inadequate Control Action.");
	}
};

const deleteInadequateControlAction = async (id: string) => {
	try {
		await http.delete(`${ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting Inadequate Control Action.");
	}
};

export {
	getByAnalysisId,
	createInadequateControlAction,
	updateInadequateControlAction,
	deleteInadequateControlAction
};
