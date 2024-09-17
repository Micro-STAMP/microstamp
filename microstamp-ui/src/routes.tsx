import { RequireAuth } from "@components/Auth";
import Analyses from "@pages/Analyses";
import Analysis from "@pages/Analysis";
import { AnalysisPurpose } from "@pages/AnalysisSteps/Step1";
import { AnalysisControlStructure, ComponentDetails } from "@pages/AnalysisSteps/Step2";
import { ControlAction, UnsafeControlActions } from "@pages/AnalysisSteps/Step3";
import { Login, Logout } from "@pages/Auth";
import Home from "@pages/Home";
import Layout from "@pages/Layout";
import { BrowserRouter, Route, Routes } from "react-router-dom";

function AppRoutes() {
	return (
		<BrowserRouter>
			<Routes>
				<Route path="/" element={<Layout />}>
					<Route index element={<Home />} />

					{/* Auth Routes  */}
					<Route path="login/oauth2/code/client-server-microstamp" element={<Login />} />
					<Route path="logout" element={<Logout />} />

					{/* Authenticated Routes */}
					<Route element={<RequireAuth />}>
						<Route path="analyses">
							<Route index element={<Analyses />} />

							<Route path=":id">
								<Route index element={<Analysis />} />
								<Route path="purpose" element={<AnalysisPurpose />} />
								<Route path="control-structure">
									<Route index element={<AnalysisControlStructure />} />
									<Route
										path="component/:componentId"
										element={<ComponentDetails />}
									/>
								</Route>
								<Route path="control-action/:controlActionId">
									<Route index element={<ControlAction />} />
									<Route
										path="unsafe-control-actions"
										element={<UnsafeControlActions />}
									/>
								</Route>
							</Route>
						</Route>
					</Route>
				</Route>
			</Routes>
		</BrowserRouter>
	);
}

export default AppRoutes;
