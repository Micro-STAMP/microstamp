import { useAuth } from "@hooks/useAuth";
import { Navigate, Outlet } from "react-router-dom";
import { toast } from "sonner";

function RequireAuth() {
	const { authenticated, user } = useAuth();
	const auth = authenticated && user;

	if (auth) {
		return <Outlet />;
	} else {
		toast.warning("Please log in before continuing.");
	}
	return <Navigate to="/" />;
}

export default RequireAuth;
