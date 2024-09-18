import { http } from "@http/AxiosConfig";
import {
	IConnectionActionInsertDto,
	IConnectionActionReadDto,
	IConnectionActionUpdateDto
} from "@interfaces/IStep2";

const CONNECTION_ACTIONS_ENDPOINT = "step2/connection-actions";

const createConnectionAction = async (connectionAction: IConnectionActionInsertDto) => {
	try {
		const res = await http.post<IConnectionActionReadDto>(
			CONNECTION_ACTIONS_ENDPOINT,
			connectionAction
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating interaction.");
	}
};
const updateConnectionAction = async (id: string, connectionAction: IConnectionActionUpdateDto) => {
	try {
		const res = await http.put<IConnectionActionReadDto>(
			`${CONNECTION_ACTIONS_ENDPOINT}/${id}`,
			connectionAction
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating interaction.");
	}
};
const deleteConnectionAction = async (id: string) => {
	try {
		await http.delete(`${CONNECTION_ACTIONS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting interaction.");
	}
};

export { createConnectionAction, deleteConnectionAction, updateConnectionAction };

/* - - - - - - - - - - - - - - - - - - - - - - */
