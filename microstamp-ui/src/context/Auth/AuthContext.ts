import { createContext } from "react";
import { AuthContextType } from "./AuthContextType";

const AuthContext = createContext<AuthContextType>({
	authenticated: false,
	user: null,
	authenticateUser: () => {},
	deauthenticateUser: () => {}
});
AuthContext.displayName = "Authentication Context";

export { AuthContext };
