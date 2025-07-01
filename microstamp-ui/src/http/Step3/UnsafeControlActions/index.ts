import { http } from "@http/AxiosConfig";
import { IUnsafeControlActionInsertDto, IUnsafeControlActionReadDto } from "@interfaces/IStep3";

/* - - - - - - - - - - - - - - - - - - - - - - */

const UCA_ENDPOINT = "step3/unsafe-control-action";

const getUnsafeControlActions = async (controlActionId: string) => {
	try {
		const res = await http.get<IUnsafeControlActionReadDto[]>(
			`${UCA_ENDPOINT}/control-action/${controlActionId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getUnsafeControlActionsByAnalysis = async (analysisId: string) => {
	try {
		const res = await http.get<IUnsafeControlActionReadDto[]>(`${UCA_ENDPOINT}`);
		return res.data.filter(uca => uca.analysis_id === analysisId);
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getUnsafeControlAction = async (id: string) => {
	try {
		const res = await http.get<IUnsafeControlActionReadDto>(`${UCA_ENDPOINT}/${id}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createUnsafeControlAction = async (uca: IUnsafeControlActionInsertDto) => {
	try {
		const res = await http.post<IUnsafeControlActionReadDto>(UCA_ENDPOINT, uca);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating unsafe control action.");
	}
};
const createUnsafeControlActionsByRule = async (ruleId: string) => {
	try {
		const res = await http.post<IUnsafeControlActionReadDto>(`${UCA_ENDPOINT}/rule/${ruleId}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating unsafe control actions by rule.");
	}
};
const deleteUnsafeControlAction = async (id: string) => {
	try {
		await http.delete(`${UCA_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting unsafe control action.");
	}
};

export {
	createUnsafeControlAction,
	createUnsafeControlActionsByRule,
	deleteUnsafeControlAction,
	getUnsafeControlAction,
	getUnsafeControlActions,
	getUnsafeControlActionsByAnalysis
};

/* - - - - - - - - - - - - - - - - - - - - - - */
