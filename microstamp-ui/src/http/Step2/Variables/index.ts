import { http } from "@http/AxiosConfig";
import {
    IVariableInsertDto,
    IVariableReadDto,
    IVariableUpdateDto
} from "@interfaces/IStep2";

const VARIABLES_ENDPOINT = "step2/variables";

const createVariable = async (variable: IVariableInsertDto) => {
	try {
		const res = await http.post<IVariableReadDto>(
			VARIABLES_ENDPOINT,
			variable
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating variable.");
	}
};
const updateVariable = async (id: string, variable: IVariableUpdateDto) => {
	try {
		const res = await http.put<IVariableReadDto>(
			`${VARIABLES_ENDPOINT}/${id}`,
			variable
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating variable.");
	}
};
const deleteVariable = async (id: string) => {
	try {
		await http.delete(`${VARIABLES_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting variable.");
	}
};

export { createVariable, deleteVariable, updateVariable };

/* - - - - - - - - - - - - - - - - - - - - - - */
