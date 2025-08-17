import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import Loader from "@components/Loader";
import NoResultsMessage from "@components/NoResultsMessage";
import PageActions from "@components/PageActions";
import { getControlAction } from "@http/Step2/Interactions/ControlActions";
import { useQuery } from "@tanstack/react-query";
import { BiErrorAlt as Step3Icon } from "react-icons/bi";
import { Navigate, useNavigate, useParams } from "react-router-dom";
import ContextTable from "./ContexTable";
import RulesContainer from "./RulesContainer";

function ControlAction() {
	const { id, controlActionId } = useParams();
	if (!id) return <Navigate to="/analyses" />;
	if (!controlActionId) return <Navigate to={`/analyses/${id}`} />;
	const navigate = useNavigate();

	const {
		data: controlAction,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["control-action-page", controlActionId],
		queryFn: () => getControlAction(controlActionId)
	});

	if (isLoading) return <Loader />;
	if (isError || controlAction === undefined)
		return <NoResultsMessage message="Error loading control action." />;
	return (
		<>
			<AnalysisHeader analysisId={id} controlAction={controlAction.name} icon="step3" />
			<RulesContainer analysisId={id} controlAction={controlAction} />
			<ContextTable controlAction={controlAction} analysisId={id} />
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
