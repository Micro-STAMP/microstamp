import { http } from "@http/AxiosConfig";
import { IConnectionInsertDto, IConnectionReadDto, IConnectionUpdateDto } from "@interfaces/IStep2";

/* - - - - - - - - - - - - - - - - - - - - - - */

const CONNECTIONS_ENDPOINT = "step2/connections";

const getConnections = async (analysisId: string) => {
	try {
		const res = await http.get<IConnectionReadDto[]>(
			`${CONNECTIONS_ENDPOINT}/analysis/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getConnection = async (id: string) => {
	try {
		const res = await http.get<IConnectionReadDto>(`${CONNECTIONS_ENDPOINT}/${id}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createConnection = async (connection: IConnectionInsertDto) => {
	try {
		const res = await http.post<IConnectionReadDto>(CONNECTIONS_ENDPOINT, connection);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating connection.");
	}
};
const updateConnection = async (id: string, connection: IConnectionUpdateDto) => {
	try {
		const res = await http.put<IConnectionReadDto>(`${CONNECTIONS_ENDPOINT}/${id}`, connection);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating connection.");
	}
};
const deleteConnection = async (id: string) => {
	try {
		await http.delete(`${CONNECTIONS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting connection.");
	}
};

export { createConnection, deleteConnection, getConnection, getConnections, updateConnection };

/* - - - - - - - - - - - - - - - - - - - - - - */
