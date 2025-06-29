import { http } from "@http/AxiosConfig";
import { IComponentReadDto } from "@interfaces/IStep2";

/* - - - - - - - - - - - - - - - - - - - - - - */

const ENVIRONMENTS_ENDPOINT = "step2/environments";

const createEnvironment = async (analysisId: string) => {
	try {
		const res = await http.post<IComponentReadDto>(
			`${ENVIRONMENTS_ENDPOINT}/analysis/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating environment.");
	}
};
const deleteEnvironment = async (id: string) => {
	try {
		await http.delete(`${ENVIRONMENTS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting environment.");
	}
};

export { createEnvironment, deleteEnvironment };

/* - - - - - - - - - - - - - - - - - - - - - - */
