import { http } from "@http/AxiosConfig";
import {
	IRefinedSolutionInsertDto,
	IRefinedSolutionReadDto,
	IRefinedSolutionUpdateDto
} from "@interfaces/IStep4New/IRefinedSolutions";

/* - - - - - - - - - - - - - - - - - - - - - - */

const REFINED_SOLUTIONS_ENDPOINT = "step4new/mitigations";

const getRefinedSolutionsByUCA = async (ucaId: string) => {
	try {
		const res = await http.get<IRefinedSolutionReadDto[]>(
			`${REFINED_SOLUTIONS_ENDPOINT}/unsafe-control-action/${ucaId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createRefinedSolution = async (solution: IRefinedSolutionInsertDto) => {
	try {
		const res = await http.post<IRefinedSolutionReadDto>(
			`${REFINED_SOLUTIONS_ENDPOINT}`,
			solution
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const updateRefinedSolution = async (solution: IRefinedSolutionUpdateDto, solutionId: string) => {
	try {
		const res = await http.put<IRefinedSolutionReadDto[]>(
			`${REFINED_SOLUTIONS_ENDPOINT}/${solutionId}`,
			solution
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const deleteRefinedSolution = async (id: string) => {
	try {
		await http.delete(`${REFINED_SOLUTIONS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw err;
	}
};

export {
	createRefinedSolution,
	deleteRefinedSolution,
	getRefinedSolutionsByUCA,
	updateRefinedSolution
};

/* - - - - - - - - - - - - - - - - - - - - - - */
