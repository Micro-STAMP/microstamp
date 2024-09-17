import { logoutRequest } from "@http/Login";
import { IToken } from "@interfaces/ILogin";
import { IUser } from "@interfaces/IUser";
import {
	getTokenStorage,
	getUserStorage,
	setRefreshTokenStorage,
	setTokenStorage,
	setUserStorage
} from "@services/storage";
import { ReactNode, useEffect, useState } from "react";
import { AuthContext } from "./AuthContext";

function AuthProvider({ children }: { children: ReactNode }) {
	const token = getTokenStorage();
	const userData = getUserStorage();

	const [authenticated, setAuthenticated] = useState<boolean>(token !== null);
	const [user, setUser] = useState<IUser | null>(userData);

	useEffect(() => {
		if (authenticated === true && userData === null) {
			deauthenticateUser();
		}
	}, [authenticated, userData, token]);

	const authenticateUser = (token: IToken, iUser: IUser) => {
		setTokenStorage(token.access_token);
		setRefreshTokenStorage(token.refresh_token);
		setAuthenticated(true);
		setUser(iUser);
		setUserStorage(iUser);
	};
	const deauthenticateUser = () => {
		setAuthenticated(false);
		setUser(null);
		logoutRequest();
	};

	return (
		<AuthContext.Provider value={{ authenticated, user, authenticateUser, deauthenticateUser }}>
			{children}
		</AuthContext.Provider>
	);
}

export default AuthProvider;
