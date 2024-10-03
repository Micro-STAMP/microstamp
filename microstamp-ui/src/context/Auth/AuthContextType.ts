import { IToken } from "@interfaces/IAuth";
import { IUser } from "@interfaces/IUser";

interface AuthContextType {
	authenticated: boolean;
	user: IUser | null;
	authenticateUser: (token: IToken, user: IUser) => void;
	deauthenticateUser: () => void;
}

export type { AuthContextType };
