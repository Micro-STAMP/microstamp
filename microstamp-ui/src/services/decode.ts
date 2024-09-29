import { ITokenPayload } from "@interfaces/IAuth";
import { IUser } from "@interfaces/IUser";
import { jwtDecode } from "jwt-decode";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Get User By Auth Token

const decodeUserFromToken = (token: string): IUser | null => {
	try {
		const decodedToken: ITokenPayload = jwtDecode(token);
		const user: IUser = {
			username: decodedToken.sub,
			id: decodedToken.userId
		};
		return user;
	} catch (error) {
		console.error("Error decoding token:", error);
		return null;
	}
};

export { decodeUserFromToken };

/* - - - - - - - - - - - - - - - - - - - - - - */
