import { IToken } from "@interfaces/ILogin";
import {
	deleteRefreshTokenStorage,
	deleteTokenStorage,
	deleteUserStorage
} from "@services/storage";
import axios from "axios";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Send to Auth Server Login

const HOST = "http://127.0.0.1:5173";
const CLIENT_ID = import.meta.env.VITE_AUTH_CLIENT_ID;
const CLIENT_SECRET = import.meta.env.VITE_AUTH_CLIENT_SECRET;

const authServerRequest = () => {
	const auth_server = "http://127.0.0.1:9000/oauth2/authorize";
	const auth_callback = `${HOST}/login/oauth2/code/client-server-microstamp`;
	const response_type = "code";
	const scope = "openid profile";

	const targetUrl = `${auth_server}?client_id=${CLIENT_ID}&redirect_uri=${auth_callback}&response_type=${response_type}&scope=${scope}`;

	window.location.href = targetUrl;
};

/* - - - - - - - - - - - - - - - - - - - - - - */

// Change Code for Token

const tokenRequest = async (code: string) => {
	const credentials = btoa(`${CLIENT_ID}:${CLIENT_SECRET}`);
	const params = new URLSearchParams({
		grant_type: "authorization_code",
		code: code,
		redirect_uri: `${HOST}/login/oauth2/code/client-server-microstamp`
	});

	try {
		const res = await axios.post<IToken>(
			"http://localhost:9000/oauth2/token",
			params.toString(),
			{
				headers: {
					"Content-Type": "application/x-www-form-urlencoded",
					Authorization: `Basic ${credentials}`
				}
			}
		);
		return res.data;
	} catch (error) {
		console.error("Failed to exchange code for token:", error);
		throw error;
	}
};

/* - - - - - - - - - - - - - - - - - - - - - - */

// Use Refresh Token

const refreshTokenRequest = async (refresh_token: string) => {
	const credentials = btoa(`${CLIENT_ID}:${CLIENT_SECRET}`);
	const params = new URLSearchParams({
		grant_type: "refresh_token",
		refresh_token: refresh_token
	});

	try {
		const res = await axios.post<IToken>(
			"http://localhost:9000/oauth2/token",
			params.toString(),
			{
				headers: {
					"Content-Type": "application/x-www-form-urlencoded",
					Authorization: `Basic ${credentials}`
				}
			}
		);
		return res.data;
	} catch (error) {
		console.error("Failed to refresh token:", error);
		throw new Error(`Failed to refresh token: ${error}`);
	}
};

/* - - - - - - - - - - - - - - - - - - - - - - */

// Logout

const logoutRequest = () => {
	deleteTokenStorage();
	deleteRefreshTokenStorage();
	deleteUserStorage();
};

export { authServerRequest, logoutRequest, refreshTokenRequest, tokenRequest };

/* - - - - - - - - - - - - - - - - - - - - - - */
