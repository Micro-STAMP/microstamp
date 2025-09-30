import { http } from "@http/AxiosConfig";
import { IFormalScenariosReadDto } from "@interfaces/IStep4New/IFormalScenarios";

/* - - - - - - - - - - - - - - - - - - - - - - */

const FORMAL_SCENARIOS_ENDPOINT = "step4new/formal-scenarios";

const getFormalScenariosByUCA = async (ucaId: string) => {
	try {
		const res = await http.get<IFormalScenariosReadDto>(
			`${FORMAL_SCENARIOS_ENDPOINT}/unsafe-control-action/${ucaId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};

export { getFormalScenariosByUCA };

/* - - - - - - - - - - - - - - - - - - - - - - */
