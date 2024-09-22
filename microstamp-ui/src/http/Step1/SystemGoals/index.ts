import { http } from "@http/AxiosConfig";
import { ISystemGoalInsertDto, ISystemGoalReadDto, ISystemGoalUpdateDto } from "@interfaces/IStep1";

/* - - - - - - - - - - - - - - - - - - - - - - */

const SYSTEM_GOALS_ENDPOINT = "step1/system-goals";

const getSystemGoals = async (analysisId: string) => {
	try {
		const res = await http.get<ISystemGoalReadDto[]>(
			`${SYSTEM_GOALS_ENDPOINT}/analysis/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getSystemGoal = async (id: string) => {
	try {
		const res = await http.get<ISystemGoalReadDto>(`${SYSTEM_GOALS_ENDPOINT}/${id}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createSystemGoal = async (systemGoal: ISystemGoalInsertDto) => {
	try {
		const res = await http.post<ISystemGoalReadDto>(SYSTEM_GOALS_ENDPOINT, systemGoal);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating system goal.");
	}
};
const updateSystemGoal = async (id: string, systemGoal: ISystemGoalUpdateDto) => {
	try {
		const res = await http.put<ISystemGoalReadDto>(
			`${SYSTEM_GOALS_ENDPOINT}/${id}`,
			systemGoal
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating system goal.");
	}
};
const deleteSystemGoal = async (id: string) => {
	try {
		await http.delete(`${SYSTEM_GOALS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting system goal.");
	}
};

export { createSystemGoal, deleteSystemGoal, getSystemGoal, getSystemGoals, updateSystemGoal };

/* - - - - - - - - - - - - - - - - - - - - - - */
