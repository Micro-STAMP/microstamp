import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import Loader from "@components/Loader";
import { ModalPDFPreview, ModalSelectStep4 } from "@components/Modal";
import NoResultsMessage from "@components/NoResultsMessage";
import PageActions from "@components/PageActions";
import { getStep4NewPDF } from "@http/Export";
import { getUnsafeControlAction } from "@http/Step3/UnsafeControlActions";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { ISteps } from "@interfaces/ISteps";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { BiExport as PdfIcon } from "react-icons/bi";
import { Navigate, useOutletContext, useSearchParams } from "react-router-dom";
import {
	useHighLevelScenarios,
	useHighLevelSolutions,
	useRefinedScenarios,
	useRefinedSolutions
} from "./hooks";
import { FormalScenariosByActivity, FormalScenariosByClass } from "./views";

function FormalScenarios() {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis

	const analysis: IAnalysisReadDto = useOutletContext();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get UCA and View

	const [searchParams] = useSearchParams();
	const ucaId = searchParams.get("uca");
	if (!ucaId) return <Navigate to={`/analyses/${analysis.id}`} />;

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Formal Scenarios View

	type FormalScenariosViewType = "class" | "activity";
	const [currentView, setCurrentView] = useState<FormalScenariosViewType>("class");

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
	// * Handle Change UCA Modal

	const [modalSelectStep4Open, setModalSelectStep4Open] = useState(false);
	const toggleModalSelectStep4 = () => setModalSelectStep4Open(!modalSelectStep4Open);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get New Step 4 PDF

	const [modalNewStep4PdfOpen, setModalNewStep4PdfOpen] = useState(false);
	const toggleModalNewStep4Pdf = () => setModalNewStep4PdfOpen(!modalNewStep4PdfOpen);

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
	} = useHighLevelSolutions(ucaId, formalScenarios);

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
			<AnalysisHeader
				analysis={analysis}
				uca={uca.name}
				step={ISteps.STEP_4}
				onChangeUCA={toggleModalSelectStep4}
				formalScenarioView={currentView}
				onChangeView={setCurrentView}
			/>

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
				<Button variant="dark" icon={PdfIcon} onClick={toggleModalNewStep4Pdf}>
					Export Formal Step 4
				</Button>
				<ModalPDFPreview
					open={modalNewStep4PdfOpen}
					onClose={toggleModalNewStep4Pdf}
					fetchPDF={getStep4NewPDF}
					analysisId={analysis.id}
					title={"Export Formal Step 4"}
				/>
			</PageActions>

			<ModalSelectStep4
				analysisId={analysis.id}
				open={modalSelectStep4Open}
				onClose={toggleModalSelectStep4}
				isUpdate
			/>
		</>
	);
}

export default FormalScenarios;
