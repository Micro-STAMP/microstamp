import AppRoutes from "@/routes";
import Toaster from "@components/Toaster";
import { AuthProvider } from "@context/Auth";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const queryClient = new QueryClient();

function App() {
	return (
		<QueryClientProvider client={queryClient}>
			<AuthProvider>
				<AppRoutes />
				<Toaster />
			</AuthProvider>
		</QueryClientProvider>
	);
}

export default App;
