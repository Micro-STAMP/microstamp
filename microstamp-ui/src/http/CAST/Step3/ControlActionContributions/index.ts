import { http } from "@http/AxiosConfig";
import {
	IControlActionContributionInsertDto,
	IControlActionContributionReadDto,
	IControlActionContributionUpdateDto
} from "@interfaces/CAST/IStep3/IControlActionContribution";

const ENDPOINT = "cast-step3/control-action-contributions";

const getByAnalysisId = async (analysisId: string) => {
	try {
		const res = await http.get<IControlActionContributionReadDto[]>(
			`${ENDPOINT}/analysis/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};

const createControlActionContribution = async (data: IControlActionContributionInsertDto) => {
	try {
		const res = await http.post<IControlActionContributionReadDto>(ENDPOINT, data);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating Control Action Contribution.");
	}
};

const updateControlActionContribution = async (
	id: string,
	data: IControlActionContributionUpdateDto
) => {
	try {
		const res = await http.put<IControlActionContributionReadDto>(`${ENDPOINT}/${id}`, data);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating Control Action Contribution.");
	}
};

const deleteControlActionContribution = async (id: string) => {
	try {
		await http.delete(`${ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting Control Action Contribution.");
	}
};

export {
	getByAnalysisId,
	createControlActionContribution,
	updateControlActionContribution,
	deleteControlActionContribution
};
