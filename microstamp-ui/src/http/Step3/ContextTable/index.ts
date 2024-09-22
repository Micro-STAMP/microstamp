import { http } from "@http/AxiosConfig";
import { IVariableReadDto } from "@interfaces/IStep2";
import { IContextTableInsertDto, IContextTableReadDto } from "@interfaces/IStep3";
import { sortContextStatesByVariable } from "@services/sort";
import { AxiosError } from "axios";

const CONTEXT_TABLES_ENDPOINT = "step3/context-table";

/* - - - - - - - - - - - - - - - - - - - - - - */

const createContextTable = async (contextTable: IContextTableInsertDto) => {
	try {
		const res = await http.post<IContextTableReadDto>(CONTEXT_TABLES_ENDPOINT, contextTable);
		return res.data;
	} catch (err) {
		const error = err as AxiosError<any>;
		console.error(err);
		throw error;
	}
};
const getContextTable = async (
	controlActionId: string,
	page: number,
	variables: IVariableReadDto[]
) => {
	try {
		const res = await http.get<IContextTableReadDto>(
			`${CONTEXT_TABLES_ENDPOINT}/control-action/${controlActionId}`,
			{
				params: {
					page: page,
					size: 15
				}
			}
		);
		return sortContextStatesByVariable(res.data, variables);
	} catch (err) {
		console.error(err);
		throw err;
	}
};

export { createContextTable, getContextTable };

/* - - - - - - - - - - - - - - - - - - - - - - */
