import { refreshTokenRequest } from "@http/Auth";
import { getTokenStorage, setTokenStorage } from "@services/storage";
import axios, { AxiosError } from "axios";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Axios Instance

const BACK_URL = import.meta.env.VITE_GATEWAY_URL;

const http = axios.create({
	baseURL: BACK_URL,
	headers: {
		Accept: "application/json",
		Content: "application/json"
	}
});

/* - - - - - - - - - - - - - - - - - - - - - - */

// Interceptors

http.interceptors.request.use(config => {
	const token = getTokenStorage();
	if (token) config.headers.Authorization = `Bearer ${token.access_token}`;
	return config;
});

interface IErrorResponse {
	status: number;
	message: string;
	cause: string;
	timestamp: string;
}
http.interceptors.response.use(
	response => response,
	async (error: AxiosError<IErrorResponse>) => {
		const originalRequest = error.config;
		const token = getTokenStorage();

		if (error.response?.status === 401 && token && originalRequest) {
			try {
				const newToken = await refreshTokenRequest(token.refresh_token);
				setTokenStorage(newToken);
				originalRequest.headers.Authorization = `Bearer ${newToken.access_token}`;

				console.log("LOG: Token Updated");

				return http(originalRequest);
			} catch (refreshError) {
				window.location.href = "/logout";
			}
		}
		return Promise.reject(error);
	}
);

export { http };

/* - - - - - - - - - - - - - - - - - - - - - - */
