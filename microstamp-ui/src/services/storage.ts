import { IToken } from "@interfaces/IAuth";
import { IUser } from "@interfaces/IUser";
import secureLocalStorage from "react-secure-storage";

/* - - - - - - - - - - - - - - - - - - - - - - */

// TOKEN Local Storage

const TOKEN_ID = "token";

const getTokenStorage = (): IToken | null => {
	const tokenJSON = secureLocalStorage.getItem(TOKEN_ID) as string | null;
	const token = tokenJSON ? (JSON.parse(tokenJSON) as IToken) : null;
	return token;
};
const setTokenStorage = (token: IToken) => {
	secureLocalStorage.setItem(TOKEN_ID, JSON.stringify(token));
};
const deleteTokenStorage = () => {
	secureLocalStorage.removeItem(TOKEN_ID);
};

export { deleteTokenStorage, getTokenStorage, setTokenStorage };

/* - - - - - - - - - - - - - - - - - - - - - - */

// USER Local Storage

const USER_ID = "user";

const getUserStorage = (): IUser | null => {
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
