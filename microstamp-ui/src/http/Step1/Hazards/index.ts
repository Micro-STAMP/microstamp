import { http } from "@http/AxiosConfig";
import { IHazardInsertDto, IHazardReadDto, IHazardUpdateDto } from "@interfaces/IStep1";

/* - - - - - - - - - - - - - - - - - - - - - - */

const HAZARDS_ENDPOINT = "step1/hazards";

const getHazards = async (analysisId: string) => {
	try {
		const res = await http.get<IHazardReadDto[]>(`${HAZARDS_ENDPOINT}/analysis/${analysisId}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getHazard = async (id: string) => {
	try {
		const res = await http.get<IHazardReadDto>(`${HAZARDS_ENDPOINT}/${id}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createHazard = async (hazard: IHazardInsertDto) => {
	try {
		const res = await http.post<IHazardReadDto>(HAZARDS_ENDPOINT, hazard);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating hazard.");
	}
};
const updateHazard = async (id: string, hazard: IHazardUpdateDto) => {
	try {
		const res = await http.put<IHazardReadDto>(`${HAZARDS_ENDPOINT}/${id}`, hazard);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating hazard.");
	}
};
const deleteHazard = async (id: string) => {
	try {
		await http.delete(`${HAZARDS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting hazard.");
	}
};

export { createHazard, deleteHazard, getHazard, getHazards, updateHazard };

/* - - - - - - - - - - - - - - - - - - - - - - */
