import { http } from "@http/AxiosConfig";
import {
	IHighLevelSolutionsReadDto,
	IHighLevelSolutionsUpdateDto
} from "@interfaces/IStep4New/IHighLevelSolutions";

/* - - - - - - - - - - - - - - - - - - - - - - */

const HIGH_LEVEL_SOLUTIONS_ENDPOINT = "step4new/high-level-solutions";

const getHighLevelSolutionsByUCA = async (ucaId: string) => {
	try {
		const res = await http.get<IHighLevelSolutionsReadDto[]>(
			`${HIGH_LEVEL_SOLUTIONS_ENDPOINT}/unsafe-control-action/${ucaId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const updateHighLevelSolutions = async (solutions: IHighLevelSolutionsUpdateDto, hlsId: string) => {
	try {
		const res = await http.put<IHighLevelSolutionsReadDto[]>(
			`${HIGH_LEVEL_SOLUTIONS_ENDPOINT}/${hlsId}`,
			solutions
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};

export { getHighLevelSolutionsByUCA, updateHighLevelSolutions };

/* - - - - - - - - - - - - - - - - - - - - - - */
