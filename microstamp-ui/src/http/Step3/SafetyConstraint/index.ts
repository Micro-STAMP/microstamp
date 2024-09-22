import { http } from "@http/AxiosConfig";
import { ISafetyConstraintReadDto } from "@interfaces/IStep3";

const SAFETY_CONTRAINTS_ENDPOINT = "step3/safety-constraint";

const getSafetyConstraint = async (ucaId: string) => {
	try {
		const res = await http.get<ISafetyConstraintReadDto>(
			`${SAFETY_CONTRAINTS_ENDPOINT}/uca/${ucaId}`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};

export { getSafetyConstraint };

/* - - - - - - - - - - - - - - - - - - - - - - */
