import { RequireAuth } from "@components/Auth";
import Analyses from "@pages/Analyses";
import Analysis from "@pages/Analysis";
import { AnalysisPurpose } from "@pages/AnalysisSteps/Step1";
import { AnalysisControlStructure, ComponentDetails } from "@pages/AnalysisSteps/Step2";
import { IdentifyUCAs, ListUCAs, UCALayout } from "@pages/AnalysisSteps/Step3";
import { LossScenarios } from "@pages/AnalysisSteps/Step4";
import { Login, Logout } from "@pages/Auth";
import Home from "@pages/Home";
import Layout from "@pages/Layout";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { FormalScenarios } from "./pages/AnalysisSteps/Step4/Step4New";
import AnalysisStepsLayout from "./pages/AnalysisSteps/layout";

// imports do CAST 
import CastStepOne from "./pages/AnalysisSteps/CAST/Step1";
import CastStepTwo from "@pages/AnalysisSteps/CAST/Step2";
import CastComponentDetails from "./pages/AnalysisSteps/CAST/Step2/CastComponentDetails";

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

								{/* CAST */}
                                <Route path="cast">
                                    <Route path="step1" element={< CastStepOne />} />
									<Route path="step2" element={<CastStepTwo />} />

									<Route path="step2/component/:componentId" element={<CastComponentDetails />} />
                                </Route>

								<Route element={<AnalysisStepsLayout />}>
									{/* STEP 1 */}
									<Route path="purpose" element={<AnalysisPurpose />} />

									{/* STEP 2 */}
									<Route path="control-structure">
										<Route index element={<AnalysisControlStructure />} />
										<Route
											path="component/:componentId"
											element={<ComponentDetails />}
										/>
									</Route>

									{/* STEP 3 */}
									<Route
										path="control-action/:caId/unsafe-control-actions"
										element={<UCALayout />}
									>
										<Route index element={<ListUCAs />} />
										<Route path="identify" element={<IdentifyUCAs />} />
									</Route>

									{/* STEP 4 HANDBOOK */}
									<Route path="loss-scenarios">
										<Route index element={<LossScenarios />} />
									</Route>

									{/* STEP 4 NEW */}
									<Route path="formal-scenarios">
										<Route index element={<FormalScenarios />} />
									</Route>
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
