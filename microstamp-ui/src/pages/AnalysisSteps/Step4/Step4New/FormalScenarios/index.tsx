import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import Loader from "@components/Loader";
import NoResultsMessage from "@components/NoResultsMessage";
import PageActions from "@components/PageActions";
import { getUnsafeControlAction } from "@http/Step3/UnsafeControlActions";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiExport as ExportIcon } from "react-icons/bi";
import { Navigate, useParams, useSearchParams } from "react-router-dom";
import styles from "./FormalScenarios.module.css";
import useHighLevelScenarios from "./hooks/useHighLevelScenarios";
import useHighLevelSolutions from "./hooks/useHighLevelSolutions";
import useRefinedScenarios from "./hooks/useRefinedScenarios";
import useRefinedSolutions from "./hooks/useRefinedSolutions";
import { FormalScenariosByActivity, FormalScenariosByClass } from "./views";

function FormalScenarios() {
	const { id } = useParams();
	const [searchParams] = useSearchParams();
	const ucaId = searchParams.get("uca");
	const view = searchParams.get("view");

	if (!id) return <Navigate to="/analyses" />;
	if (!ucaId) return <Navigate to={`/analyses/${id}`} />;

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Formal Scenarios View

	type FormalScenariosViewType = "class" | "activity";
	const [currentView, setCurrentView] = useState<FormalScenariosViewType>(
		view && (view === "class" || view === "activity") ? view : "class"
	);

	useEffect(() => {
		const view = searchParams.get("view");
		if (view && (view === "class" || view === "activity")) {
			setCurrentView(view as FormalScenariosViewType);
		}
	}, [searchParams]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get UCA

	const {
		data: uca,
		isLoading: isLoadingUCA,
		isError: isErrorUCA
	} = useQuery({
		queryKey: ["uca-page", ucaId],
		queryFn: () => getUnsafeControlAction(ucaId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Formal Scenarios Entities

	// 4.1 High Level Scenarios
	const {
		formalScenarios,
		isLoading: isLoadingHighLevelScenarios,
		isError: isErrorHighLevelScenarios
	} = useHighLevelScenarios(ucaId);

	// 4.2 High Level Solutions
	const {
		highLevelSolutions,
		isLoading: isLoadingHighLevelSolutions,
		isError: isErrorHighLevelSolutions
	} = useHighLevelSolutions(ucaId);

	// 4.3 Refined Scenarios
	const {
		refinedScenarios,
		isLoading: isLoadingRefinedScenarios,
		isError: isErrorRefinedScenarios
	} = useRefinedScenarios({ ucaId });

	// 4.4 Refined Solutions
	const {
		refinedSolutions,
		isLoading: isLoadingRefinedSolutions,
		isError: isErrorRefinedSolutions
	} = useRefinedSolutions({ ucaId });

	/* - - - - - - - - - - - - - - - - - - - - - - */

	const isLoadingView =
		isLoadingHighLevelScenarios ||
		isLoadingHighLevelSolutions ||
		isLoadingRefinedScenarios ||
		isLoadingRefinedSolutions;
	const isErrorView =
		isErrorHighLevelScenarios ||
		isErrorHighLevelSolutions ||
		isErrorRefinedScenarios ||
		isErrorRefinedSolutions ||
		formalScenarios === undefined ||
		highLevelSolutions === undefined ||
		refinedSolutions === undefined;

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingUCA) return <Loader />;
	if (isErrorUCA || uca === undefined)
		return <NoResultsMessage message="Error loading formal scenarios." />;
	return (
		<>
			<AnalysisHeader analysisId={id} uca={uca.name} icon="step4" />
			<div className={styles.subheader}>
				<button
					className={`${styles.toggle_button} ${
						currentView === "class" ? styles.active : ""
					}`}
					onClick={() => setCurrentView("class")}
					type="button"
				>
					Identify Formal Scenarios by Class
				</button>
				<button
					className={`${styles.toggle_button} ${
						currentView === "activity" ? styles.active : ""
					}`}
					onClick={() => setCurrentView("activity")}
					type="button"
				>
					Identify Formal Scenarios by Activity
				</button>
			</div>
			{currentView === "class" && (
				<FormalScenariosByClass
					uca={uca}
					formalScenarios={formalScenarios}
					highLevelSolutions={highLevelSolutions}
					refinedScenarios={refinedScenarios}
					refinedSolutions={refinedSolutions}
					isLoading={isLoadingView}
					isError={isErrorView}
				/>
			)}
			{currentView === "activity" && (
				<FormalScenariosByActivity
					uca={uca}
					formalScenarios={formalScenarios}
					highLevelSolutions={highLevelSolutions}
					refinedScenarios={refinedScenarios}
					refinedSolutions={refinedSolutions}
					isLoading={isLoadingView}
					isError={isErrorView}
				/>
			)}
			<PageActions>
				<Button variant="dark" icon={ExportIcon}>
					Export New Step 4
				</Button>
			</PageActions>
		</>
	);
}

export default FormalScenarios;
