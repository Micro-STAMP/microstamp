import { http } from "@http/AxiosConfig";
import {
	IInteractionInsertDto,
	IInteractionReadDto,
	IInteractionUpdateDto
} from "@interfaces/IStep2";

const INTERACTIONS_ENDPOINT = "step2/interactions";

const createInteraction = async (interaction: IInteractionInsertDto) => {
	try {
		const res = await http.post<IInteractionReadDto>(
			INTERACTIONS_ENDPOINT,
			interaction
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating interaction.");
	}
};
const updateInteraction = async (id: string, interaction: IInteractionUpdateDto) => {
	try {
		const res = await http.put<IInteractionReadDto>(
			`${INTERACTIONS_ENDPOINT}/${id}`,
			interaction
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating interaction.");
	}
};
const deleteInteraction = async (id: string) => {
	try {
		await http.delete(`${INTERACTIONS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting interaction.");
	}
};

export { createInteraction, deleteInteraction, updateInteraction };

/* - - - - - - - - - - - - - - - - - - - - - - */
