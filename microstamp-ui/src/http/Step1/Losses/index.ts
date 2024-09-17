import { http } from "@http/AxiosConfig";
import { ILossInsertDto, ILossReadDto, ILossUpdateDto } from "@interfaces/IStep1";

/* - - - - - - - - - - - - - - - - - - - - - - */

const LOSSES_ENDPOINT = "step1/losses";

const getLosses = async (analysisId: string) => {
	try {
		const res = await http.get<ILossReadDto[]>(`${LOSSES_ENDPOINT}/analysis/${analysisId}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getLoss = async (id: string) => {
	try {
		const res = await http.get<ILossReadDto>(`${LOSSES_ENDPOINT}/${id}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createLoss = async (loss: ILossInsertDto) => {
	try {
		const res = await http.post<ILossReadDto>(LOSSES_ENDPOINT, loss);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating loss.");
	}
};
const updateLoss = async (id: string, loss: ILossUpdateDto) => {
	try {
		const res = await http.put<ILossReadDto>(`${LOSSES_ENDPOINT}/${id}`, loss);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating loss.");
	}
};
const deleteLoss = async (id: string) => {
	try {
		await http.delete(`${LOSSES_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting loss.");
	}
};

export { createLoss, deleteLoss, getLoss, getLosses, updateLoss };

/* - - - - - - - - - - - - - - - - - - - - - - */
