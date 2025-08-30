import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import Loader from "@components/Loader";
import { ModalPDFPreview } from "@components/Modal";
import NoResultsMessage from "@components/NoResultsMessage";
import PageActions from "@components/PageActions";
import { getStep3PDF } from "@http/Export";
import { getControlAction } from "@http/Step2/Interactions/ControlActions";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { ISteps } from "@interfaces/ISteps";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { BiExport as PdfIcon } from "react-icons/bi";
import { Navigate, useOutletContext, useParams } from "react-router-dom";
import UCAsContainer from "./UCAsContainer";

function UnsafeControlActions() {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis

	const analysis: IAnalysisReadDto = useOutletContext();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Control Action

	const { controlActionId } = useParams();
	if (!controlActionId) return <Navigate to={`/analyses/${analysis.id}`} />;

	const {
		data: controlAction,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["control-action-page", controlActionId],
		queryFn: () => getControlAction(controlActionId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Step 3 PDF

	const [modalStep3PdfOpen, setModalStep3PdfOpen] = useState(false);
	const toggleModalStep3Pdf = () => setModalStep3PdfOpen(!modalStep3PdfOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || controlAction === undefined)
		return <NoResultsMessage message="Error loading unsafe control actions." />;
	return (
		<>
			<AnalysisHeader
				analysis={analysis}
				controlAction={controlAction.name}
				step={ISteps.STEP_3}
			/>

			<UCAsContainer controlAction={controlAction} />

			<PageActions>
				<Button variant="dark" icon={PdfIcon} onClick={toggleModalStep3Pdf}>
					Export Step 3
				</Button>
				<ModalPDFPreview
					open={modalStep3PdfOpen}
					onClose={toggleModalStep3Pdf}
					fetchPDF={getStep3PDF}
					analysisId={analysis.id}
					title={"Export Step 3"}
				/>
			</PageActions>
		</>
	);
}

export default UnsafeControlActions;
