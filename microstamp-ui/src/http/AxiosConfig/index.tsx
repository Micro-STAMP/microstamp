import { refreshTokenRequest } from "@http/Login";
import {
	getRefreshTokenStorage,
	getTokenStorage,
	setRefreshTokenStorage,
	setTokenStorage
} from "@services/storage";
import axios, { AxiosError } from "axios";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Axios Instance

const BACK_URL = import.meta.env.VITE_BACK_URL;

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
	if (token) config.headers.Authorization = `Bearer ${token}`;
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
		const refreshToken = getRefreshTokenStorage();

		if (
			error.response?.status === 401 &&
			token &&
			typeof refreshToken === "string" &&
			originalRequest
		) {
			try {
				const newToken = await refreshTokenRequest(refreshToken);
				setTokenStorage(newToken.access_token);
				setRefreshTokenStorage(newToken.refresh_token);
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
