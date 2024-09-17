import { AuthContext } from "@context/Auth";
import { useContext } from "react";

function useAuth() {
	const context = useContext(AuthContext);
	if (!context) {
		throw new Error("useAuth must be used within an AuthProvider");
	}
	return context;
}

export { useAuth };

