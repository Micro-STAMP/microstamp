import { isAxiosError } from "axios";

const DUPLICATED_CODE_MESSAGE = "This code is already in use in this analysis.";

// The API answers 409 when the (code, analysis) unique constraint is violated
const castRequestError = (err: unknown, fallbackMessage: string) =>
	new Error(isAxiosError(err) && err.response?.status === 409 ? DUPLICATED_CODE_MESSAGE : fallbackMessage);

export { castRequestError };
