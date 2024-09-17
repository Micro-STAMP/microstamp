import { http } from "@http/AxiosConfig";
import { IStateInsertDto, IStateReadDto, IStateUpdateDto } from "@interfaces/IStep2";

const STATES_ENDPOINT = "step2/states";

const createState = async (state: IStateInsertDto) => {
	try {
		const res = await http.post<IStateReadDto>(STATES_ENDPOINT, state);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating state.");
	}
};
const updateState = async (id: string, state: IStateUpdateDto) => {
	try {
		const res = await http.put<IStateReadDto>(`${STATES_ENDPOINT}/${id}`, state);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating state.");
	}
};
const deleteState = async (id: string) => {
	try {
		await http.delete(`${STATES_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting state.");
	}
};

export { createState, deleteState, updateState };

/* - - - - - - - - - - - - - - - - - - - - - - */
