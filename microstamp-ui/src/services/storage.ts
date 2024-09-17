import { IUser } from "@interfaces/IUser";
import secureLocalStorage from "react-secure-storage";

/* - - - - - - - - - - - - - - - - - - - - - - */

// TOKEN Local Storage

const TOKEN_ID = "token";

const getTokenStorage = () => {
	const token = secureLocalStorage.getItem(TOKEN_ID);
	return token;
};
const setTokenStorage = (token: string) => {
	secureLocalStorage.setItem(TOKEN_ID, token);
};
const deleteTokenStorage = () => {
	secureLocalStorage.removeItem(TOKEN_ID);
};

export { deleteTokenStorage, getTokenStorage, setTokenStorage };

/* - - - - - - - - - - - - - - - - - - - - - - */

// REFRESH TOKEN Local Storage

const REFRESH_TOKEN_ID = "refresh_token";

const getRefreshTokenStorage = () => {
	const token = secureLocalStorage.getItem(REFRESH_TOKEN_ID);
	return token;
};
const setRefreshTokenStorage = (refresh_token: string) => {
	secureLocalStorage.setItem(REFRESH_TOKEN_ID, refresh_token);
};
const deleteRefreshTokenStorage = () => {
	secureLocalStorage.removeItem(REFRESH_TOKEN_ID);
};

export { deleteRefreshTokenStorage, getRefreshTokenStorage, setRefreshTokenStorage };

/* - - - - - - - - - - - - - - - - - - - - - - */

// USER Local Storage

const USER_ID = "user";

const getUserStorage = () => {
	const userJSON = secureLocalStorage.getItem(USER_ID) as string | null;
	const user = userJSON ? (JSON.parse(userJSON) as IUser) : null;
	return user;
};
const setUserStorage = (user: IUser) => {
	secureLocalStorage.setItem(USER_ID, JSON.stringify(user));
};
const deleteUserStorage = () => {
	secureLocalStorage.removeItem(USER_ID);
};

export { deleteUserStorage, getUserStorage, setUserStorage };

/* - - - - - - - - - - - - - - - - - - - - - - */
