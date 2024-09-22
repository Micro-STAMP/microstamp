import { http } from "@http/AxiosConfig";
import { IAssumptionInsertDto, IAssumptionReadDto, IAssumptionUpdateDto } from "@interfaces/IStep1";

/* - - - - - - - - - - - - - - - - - - - - - - */

const ASSUMPTIONS_ENDPOINT = "step1/assumptions";

const getAssumptions = async (analysisId: string) => {
	try {
		const res = await http.get<IAssumptionReadDto[]>(
			`${ASSUMPTIONS_ENDPOINT}/analysis/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getAssumption = async (id: string) => {
	try {
		const res = await http.get<IAssumptionReadDto>(`${ASSUMPTIONS_ENDPOINT}/${id}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createAssumption = async (assumption: IAssumptionInsertDto) => {
	try {
		const res = await http.post<IAssumptionReadDto>(ASSUMPTIONS_ENDPOINT, assumption);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating assumption.");
	}
};
const updateAssumption = async (id: string, assumption: IAssumptionUpdateDto) => {
	try {
		const res = await http.put<IAssumptionReadDto>(`${ASSUMPTIONS_ENDPOINT}/${id}`, assumption);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating assumption.");
	}
};
const deleteAssumption = async (id: string) => {
	try {
		await http.delete(`${ASSUMPTIONS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting assumption.");
	}
};

export { createAssumption, deleteAssumption, getAssumption, getAssumptions, updateAssumption };

/* - - - - - - - - - - - - - - - - - - - - - - */
