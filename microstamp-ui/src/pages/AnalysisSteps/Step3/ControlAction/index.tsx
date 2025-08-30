import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import Loader from "@components/Loader";
import NoResultsMessage from "@components/NoResultsMessage";
import PageActions from "@components/PageActions";
import { getControlAction } from "@http/Step2/Interactions/ControlActions";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { ISteps } from "@interfaces/ISteps";
import { useQuery } from "@tanstack/react-query";
import { BiErrorAlt as Step3Icon } from "react-icons/bi";
import { Navigate, useNavigate, useOutletContext, useParams } from "react-router-dom";
import ContextTable from "./ContexTable";
import RulesContainer from "./RulesContainer";

function ControlAction() {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis

	const analysis: IAnalysisReadDto = useOutletContext();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Control Action

	const { controlActionId } = useParams();
	if (!controlActionId) return <Navigate to={`/analyses/${analysis.id}`} />;
	const navigate = useNavigate();

	const {
		data: controlAction,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["control-action-page", controlActionId],
		queryFn: () => getControlAction(controlActionId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || controlAction === undefined)
		return <NoResultsMessage message="Error loading control action." />;
	return (
		<>
			<AnalysisHeader
				analysis={analysis}
				controlAction={controlAction.name}
				step={ISteps.STEP_3}
			/>

			<RulesContainer analysisId={analysis.id} controlAction={controlAction} />
			<ContextTable controlAction={controlAction} analysisId={analysis.id} />

			<PageActions>
				<Button
					variant="dark"
					icon={Step3Icon}
					onClick={() => navigate("unsafe-control-actions")}
				>
					See Unsafe Control Actions
				</Button>
			</PageActions>
		</>
	);
}

export default ControlAction;
