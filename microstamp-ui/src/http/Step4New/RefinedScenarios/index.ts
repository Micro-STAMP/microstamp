import { http } from "@http/AxiosConfig";
import {
	IRefinedScenarioInsertDto,
	IRefinedScenarioReadDto,
	IRefinedScenariosCommonCausesDto,
	IRefinedScenarioUpdateDto
} from "@interfaces/IStep4New/IRefinedScenarios";

/* - - - - - - - - - - - - - - - - - - - - - - */

const REFINED_SCENARIOS_ENDPOINT = "step4new/refined-scenarios";

const getRefinedScenariosByUCA = async (ucaId: string) => {
	try {
		const res = await http.get<IRefinedScenarioReadDto[]>(
			`${REFINED_SCENARIOS_ENDPOINT}/unsafe_control_action_id/${ucaId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getRefinedScenariosCommonCausesByUCA = async (ucaId: string) => {
	try {
		const res = await http.get<IRefinedScenariosCommonCausesDto[]>(
			`${REFINED_SCENARIOS_ENDPOINT}/templates/unsafe-control-action/${ucaId}/apply/common-cause`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createRefinedScenario = async (scenario: IRefinedScenarioInsertDto) => {
	try {
		const res = await http.post<IRefinedScenarioReadDto>(
			`${REFINED_SCENARIOS_ENDPOINT}`,
			scenario
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const updateRefinedScenario = async (id: string, scenario: IRefinedScenarioUpdateDto) => {
	try {
		const res = await http.put<IRefinedScenarioReadDto>(
			`${REFINED_SCENARIOS_ENDPOINT}/${id}`,
			scenario
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const deleteRefinedScenario = async (id: string) => {
	try {
		await http.delete(`${REFINED_SCENARIOS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw err;
	}
};

export {
	createRefinedScenario,
	deleteRefinedScenario,
	getRefinedScenariosByUCA,
	getRefinedScenariosCommonCausesByUCA,
	updateRefinedScenario
};

/* - - - - - - - - - - - - - - - - - - - - - - */
