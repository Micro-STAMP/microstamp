import { http } from "@http/AxiosConfig";
import {
	IResponsibilityInsertDto,
	IResponsibilityReadDto,
	IResponsibilityUpdateDto
} from "@interfaces/IStep2";

const RESPONSIBILITIES_ENDPOINT = "step2/responsibilities";

const getResponsibilities = async (componentId: string) => {
	try {
		const res = await http.get<IResponsibilityReadDto[]>(
			`${RESPONSIBILITIES_ENDPOINT}/component/${componentId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createResponsibility = async (responsibility: IResponsibilityInsertDto) => {
	try {
		const res = await http.post<IResponsibilityReadDto>(
			RESPONSIBILITIES_ENDPOINT,
			responsibility
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating responsibility.");
	}
};
const updateResponsibility = async (id: string, responsibility: IResponsibilityUpdateDto) => {
	try {
		const res = await http.put<IResponsibilityReadDto>(
			`${RESPONSIBILITIES_ENDPOINT}/${id}`,
			responsibility
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating responsibility.");
	}
};
const deleteResponsibility = async (id: string) => {
	try {
		await http.delete(`${RESPONSIBILITIES_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting responsibility.");
	}
};

export { createResponsibility, deleteResponsibility, getResponsibilities, updateResponsibility };

/* - - - - - - - - - - - - - - - - - - - - - - */
