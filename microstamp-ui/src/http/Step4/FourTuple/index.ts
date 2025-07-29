import { http } from "@http/AxiosConfig";
import {
	IFourTupleInsertDto,
	IFourTupleReadDto,
	IFourTupleUCADto,
	IFourTupleUpdateDto
} from "@interfaces/IStep4";

/* - - - - - - - - - - - - - - - - - - - - - - */

const FOUR_TUPLES_ENDPOINT = "step4/four-tuples";

const getFourTuples = async (analysisId: string) => {
	try {
		const res = await http.get<IFourTupleReadDto[]>(
			`${FOUR_TUPLES_ENDPOINT}/analysis/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getFourTuplesByUCA = async (analysisId: string) => {
	try {
		const res = await http.get<IFourTupleUCADto[]>(
			`${FOUR_TUPLES_ENDPOINT}/analysis/${analysisId}/unsafe-control-actions`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getFourTuple = async (id: string) => {
	try {
		const res = await http.get<IFourTupleReadDto>(`${FOUR_TUPLES_ENDPOINT}/${id}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createFourTuple = async (fourTuple: IFourTupleInsertDto) => {
	try {
		const res = await http.post<IFourTupleReadDto>(FOUR_TUPLES_ENDPOINT, fourTuple);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating scenario.");
	}
};
const updateFourTuple = async (id: string, fourTuple: IFourTupleUpdateDto) => {
	try {
		const res = await http.put<IFourTupleReadDto>(`${FOUR_TUPLES_ENDPOINT}/${id}`, fourTuple);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating scenario.");
	}
};
const deleteFourTuple = async (id: string) => {
	try {
		await http.delete(`${FOUR_TUPLES_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting scenario.");
	}
};

export {
	createFourTuple,
	deleteFourTuple,
	getFourTuple,
	getFourTuples,
	getFourTuplesByUCA,
	updateFourTuple
};

/* - - - - - - - - - - - - - - - - - - - - - - */
