import { IToken } from "@interfaces/IAuth";
import { deleteTokenStorage, deleteUserStorage, getTokenStorage } from "@services/storage";
import axios from "axios";

/* - - - - - - - - - - - - - - - - - - - - - - */

const HOST = "http://127.0.0.1:5173";
const CLIENT_ID = import.meta.env.VITE_AUTH_CLIENT_ID;
const CLIENT_SECRET = import.meta.env.VITE_AUTH_CLIENT_SECRET;

/* - - - - - - - - - - - - - - - - - - - - - - */

// Send to Auth Server Login

const authServerRequest = () => {
	const auth_server = "http://127.0.0.1:9000/oauth2/authorize";
	const auth_callback = `${HOST}/login/oauth2/code/client-server-microstamp`;
	const response_type = "code";
	const scope = "openid profile";

	const targetUrl = `${auth_server}?client_id=${CLIENT_ID}&redirect_uri=${auth_callback}&response_type=${response_type}&scope=${scope}`;

	window.location.href = targetUrl;
};

/* - - - - - - - - - - - - - - - - - - - - - - */

// Send to Auth Register User

const authRegisterRequest = () => {
	const register_auth_server = "http://127.0.0.1:9000/registration";
	const auth_callback = `${HOST}/login/oauth2/code/client-server-microstamp`;
	const response_type = "code";
	const scope = "openid profile";

	const targetUrl = `${register_auth_server}?client_id=${CLIENT_ID}&redirect_uri=${auth_callback}&response_type=${response_type}&scope=${scope}`;

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
	const token = getTokenStorage();
	if (!token) return;

	const logout_server = "http://127.0.0.1:9000/logout";
	const post_logout_redirect_uri = `${HOST}/`;

	const logoutUrl = `${logout_server}?id_token_hint=${token.id_token}&post_logout_redirect_uri=${post_logout_redirect_uri}`;

	deleteTokenStorage();
	deleteUserStorage();

	window.location.href = logoutUrl;
};

export { authRegisterRequest, authServerRequest, logoutRequest, refreshTokenRequest, tokenRequest };

/* - - - - - - - - - - - - - - - - - - - - - - */
