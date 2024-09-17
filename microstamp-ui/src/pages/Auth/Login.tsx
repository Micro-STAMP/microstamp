import { ModalLoading } from "@components/Modal";
import { useAuth } from "@hooks/useAuth";
import { tokenRequest } from "@http/Login";
import { decodeUserFromToken } from "@services/decode";
import { useMutation } from "@tanstack/react-query";
import { useEffect } from "react";
import { Navigate, useNavigate, useSearchParams } from "react-router-dom";
import { toast } from "sonner";

function Login() {
	const { authenticated, authenticateUser, deauthenticateUser } = useAuth();
	if (authenticated) return <Navigate to="/analyses" />;

	const navigate = useNavigate();
	const [searchParams] = useSearchParams();

	const { mutateAsync: requestToken, isPending: isAuthenticating } = useMutation({
		mutationFn: (code: string) => tokenRequest(code),
		onSuccess: token => {
			const user = decodeUserFromToken(token.access_token);
			if (user) {
				authenticateUser(token, user);
				navigate("/analyses");
			} else {
				deauthenticateUser();
			}
		},
		onError: () => {
			toast.error("Login failed");
			navigate("/");
			deauthenticateUser();
		}
	});

	useEffect(() => {
		const code = searchParams.get("code");
		if (code) {
			requestToken(code);
		} else {
			toast.error("No authorization code found.");
			navigate("/");
		}
	}, [requestToken]);

	return (
		<>
			<ModalLoading open={isAuthenticating} message="Authenticating" />
		</>
	);
}

export default Login;
