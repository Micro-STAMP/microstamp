import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import Loader from "@components/Loader";
import NoResultsMessage from "@components/NoResultsMessage";
import PageActions from "@components/PageActions";
import { getUnsafeControlAction } from "@http/Step3/UnsafeControlActions";
import { getFormalScenariosByUCA } from "@http/Step4New/FormalScenarios";
import { useQuery } from "@tanstack/react-query";
import { BiExport as ExportIcon } from "react-icons/bi";
import { Navigate, useParams, useSearchParams } from "react-router-dom";
import HighLevelScenariosContainer from "./HighLevelScenariosContainer";
import HighLevelSolutionsContainer from "./HighLevelSolutionsContainer";
import RefinedScenariosContainer from "./RefinedScenariosContainer";
import RefinedSolutionsContainer from "./RefinedSolutionsContainer";

function FormalScenarios() {
	const { id } = useParams();
	const [searchParams] = useSearchParams();
	const ucaId = searchParams.get("uca");
	if (!id) return <Navigate to="/analyses" />;
	if (!ucaId) return <Navigate to={`/analyses/${id}`} />;

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
	// * Handle Get High Level Scenarios

	const {
		data: formalScenarios,
		isLoading: isLoadingFormalScenarios,
		isError: isErrorFormalScenarios
	} = useQuery({
		queryKey: ["formal-scenarios", ucaId],
		queryFn: () => getFormalScenariosByUCA(ucaId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingUCA) return <Loader />;
	if (isErrorUCA || uca === undefined)
		return <NoResultsMessage message="Error loading formal scenarios." />;
	return (
		<>
			<AnalysisHeader analysisId={id} uca={uca.name} icon="step4" />
			<HighLevelScenariosContainer
				formalScenarios={formalScenarios}
				isLoading={isLoadingFormalScenarios}
				isError={isErrorFormalScenarios}
			/>
			<HighLevelSolutionsContainer formalScenarios={formalScenarios} ucaId={ucaId} />
			<RefinedScenariosContainer />
			<RefinedSolutionsContainer />
			<PageActions>
				<Button variant="dark" icon={ExportIcon}>
					Export New Step 4
				</Button>
			</PageActions>
		</>
	);
}

export default FormalScenarios;
