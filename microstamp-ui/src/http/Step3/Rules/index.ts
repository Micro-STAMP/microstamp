import { http } from "@http/AxiosConfig";
import { IRuleInsertDto, IRuleReadDto } from "@interfaces/IStep3";

const RULES_ENDPOINT = "step3/rule";

const getRules = async (controlActionId: string) => {
	try {
		const res = await http.get<IRuleReadDto[]>(
			`${RULES_ENDPOINT}/control-action/${controlActionId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createRule = async (rule: IRuleInsertDto) => {
	try {
		const res = await http.post<IRuleReadDto>(RULES_ENDPOINT, rule);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating rule.");
	}
};
const deleteRule = async (id: string) => {
	try {
		await http.delete(`${RULES_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting rule.");
	}
};

export { createRule, deleteRule, getRules };

/* - - - - - - - - - - - - - - - - - - - - - - */
