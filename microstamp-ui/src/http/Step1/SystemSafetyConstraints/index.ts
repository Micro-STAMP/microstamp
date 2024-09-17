import { http } from "@http/AxiosConfig";
import {
	ISystemSafetyConstraintInsertDto,
	ISystemSafetyConstraintReadDto,
	ISystemSafetyConstraintUpdateDto
} from "@interfaces/IStep1";

/* - - - - - - - - - - - - - - - - - - - - - - */

const SYSTEM_SAFETY_CONSTRAINTS_ENDPOINT = "step1/system-safety-constraints";

const getSystemSafetyConstraints = async (analysisId: string) => {
	try {
		const res = await http.get<ISystemSafetyConstraintReadDto[]>(
			`${SYSTEM_SAFETY_CONSTRAINTS_ENDPOINT}/analysis/${analysisId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getSystemSafetyConstraint = async (id: string) => {
	try {
		const res = await http.get<ISystemSafetyConstraintReadDto>(
			`${SYSTEM_SAFETY_CONSTRAINTS_ENDPOINT}/${id}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createSystemSafetyConstraint = async (
	systemSafetyConstraint: ISystemSafetyConstraintInsertDto
) => {
	try {
		const res = await http.post<ISystemSafetyConstraintReadDto>(
			SYSTEM_SAFETY_CONSTRAINTS_ENDPOINT,
			systemSafetyConstraint
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating system safety constraint.");
	}
};
const updateSystemSafetyConstraint = async (
	id: string,
	systemSafetyConstraint: ISystemSafetyConstraintUpdateDto
) => {
	try {
		const res = await http.put<ISystemSafetyConstraintReadDto>(
			`${SYSTEM_SAFETY_CONSTRAINTS_ENDPOINT}/${id}`,
			systemSafetyConstraint
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating system safety constraint.");
	}
};
const deleteSystemSafetyConstraint = async (id: string) => {
	try {
		await http.delete(`${SYSTEM_SAFETY_CONSTRAINTS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting system safety constraint.");
	}
};

export {
	createSystemSafetyConstraint,
	deleteSystemSafetyConstraint,
	getSystemSafetyConstraint,
	getSystemSafetyConstraints,
	updateSystemSafetyConstraint
};

/* - - - - - - - - - - - - - - - - - - - - - - */
