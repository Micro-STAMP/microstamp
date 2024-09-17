import { useAuth } from "@hooks/useAuth";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const Logout = () => {
	const navigate = useNavigate();
	const { deauthenticateUser } = useAuth();

	useEffect(() => {
		deauthenticateUser();
		console.log("Session expired.");
		navigate("/");
	}, []);

	return null;
};

export default Logout;
