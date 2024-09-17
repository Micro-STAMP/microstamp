import { http } from "@http/AxiosConfig";
import { IUser } from "@interfaces/IUser";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Get User

const USER_ENDPOINT = "auth/users/me";

const getUser = async () => {
	try {
		const res = await http.get<IUser>(USER_ENDPOINT);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};

export { getUser };

/* - - - - - - - - - - - - - - - - - - - - - - */
