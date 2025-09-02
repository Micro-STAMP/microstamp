import AnalysisHeader from "@components/AnalysisHeader";
import Loader from "@components/Loader";
import { ModalSelectStep3 } from "@components/Modal";
import NoResultsMessage from "@components/NoResultsMessage";
import { getControlAction } from "@http/Step2/Interactions/ControlActions";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { ISteps } from "@interfaces/ISteps";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { Navigate, Outlet, useOutletContext, useParams } from "react-router-dom";

function UCALayout() {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis

	const analysis: IAnalysisReadDto = useOutletContext();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Control Action

	const { caId } = useParams();
	if (!caId) return <Navigate to={`/analyses/${analysis.id}`} />;

	const {
		data: controlAction,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["control-action-uca-page", caId],
		queryFn: () => getControlAction(caId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Modal Change Control Action

	const [modalSelectStep3Open, setModalSelectStep3Open] = useState(false);
	const toggleModalSelectStep3 = () => setModalSelectStep3Open(!modalSelectStep3Open);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || controlAction === undefined)
		return <NoResultsMessage message="Error loading the selected control action." />;
	return (
		<>
			<AnalysisHeader
				analysis={analysis}
				controlAction={controlAction.name}
				step={ISteps.STEP_3}
				onChangeControlAction={toggleModalSelectStep3}
			/>
			<Outlet context={[analysis, controlAction]} />
			<ModalSelectStep3
				open={modalSelectStep3Open}
				onClose={toggleModalSelectStep3}
				analysisId={analysis.id}
				isUpdate={true}
			/>
		</>
	);
}

export default UCALayout;
