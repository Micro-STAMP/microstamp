import { http } from "@http/AxiosConfig";
import { INotUnsafeContextInsertDto, INotUnsafeContextReadDto } from "@interfaces/IStep3";

const NOT_UNSAFE_CONTEXT_ENDPOINT = "step3/not-uca-contexts";

/* - - - - - - - - - - - - - - - - - - - - - - */

const getNotUnsafeContexts = async (analysisId: string) => {
	try {
		const res = await http.get<INotUnsafeContextReadDto[]>(
			`${NOT_UNSAFE_CONTEXT_ENDPOINT}/control-actions/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createNotUnsafeContext = async (notUnsafeContext: INotUnsafeContextInsertDto) => {
	try {
		const res = await http.post<INotUnsafeContextReadDto>(
			NOT_UNSAFE_CONTEXT_ENDPOINT,
			notUnsafeContext
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating not unsafe context.");
	}
};
const deleteNotUnsafeContext = async (id: string) => {
	try {
		await http.delete(`${NOT_UNSAFE_CONTEXT_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting not unsafe context.");
	}
};

export { createNotUnsafeContext, deleteNotUnsafeContext, getNotUnsafeContexts };

/* - - - - - - - - - - - - - - - - - - - - - - */
