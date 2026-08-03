import { http } from "@http/AxiosConfig";
import {
	ISystemicFactorInsertDto,
	ISystemicFactorReadDto,
	ISystemicFactorUpdateDto
} from "@interfaces/CAST/IStep4/ISystemicFactor";

const ENDPOINT = "cast-step4/systemic-factors";

const getByAnalysisId = async (analysisId: string) => {
	try {
		const res = await http.get<ISystemicFactorReadDto[]>(
			`${ENDPOINT}/analysis/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};

const createSystemicFactor = async (data: ISystemicFactorInsertDto) => {
	try {
		const res = await http.post<ISystemicFactorReadDto>(ENDPOINT, data);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating Systemic Factor.");
	}
};

const updateSystemicFactor = async (
	id: string,
	data: ISystemicFactorUpdateDto
) => {
	try {
		const res = await http.put<ISystemicFactorReadDto>(`${ENDPOINT}/${id}`, data);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating Systemic Factor.");
	}
};

const deleteSystemicFactor = async (id: string) => {
	try {
		await http.delete(`${ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting Systemic Factor.");
	}
};

export {
	getByAnalysisId,
	createSystemicFactor,
	updateSystemicFactor,
	deleteSystemicFactor
};
