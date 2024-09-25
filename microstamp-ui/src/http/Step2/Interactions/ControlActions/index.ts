import { http } from "@http/AxiosConfig";
import { IInteractionType, IConnectionReadDto, IControlAction } from "@interfaces/IStep2";

const CONNECTIONS_ENDPOINT = "step2/connections";
const CONTROL_ACTIONS_ENDPOINT = "step2/control-actions";

const getControlActions = async (analysisId: string) => {
	try {
		const res = await http.get<IConnectionReadDto[]>(
			`${CONNECTIONS_ENDPOINT}/analysis/${analysisId}`
		);
		const connections = res.data;
		const controlActions: IControlAction[] = connections.flatMap(connection =>
			connection.interactions
				.filter(
					action => action.interactionType === IInteractionType.CONTROL_ACTION
				)
				.map(action => ({
					id: action.id,
					name: action.name,
					code: action.code,
					connection: connection
				}))
		);

		return controlActions;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getControlAction = async (id: string) => {
	try {
		const res = await http.get<IControlAction>(`${CONTROL_ACTIONS_ENDPOINT}/${id}`);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};

export { getControlAction, getControlActions };
