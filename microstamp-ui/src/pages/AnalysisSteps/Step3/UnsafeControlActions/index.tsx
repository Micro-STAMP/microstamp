import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import Loader from "@components/Loader";
import { ModalPDFPreview } from "@components/Modal";
import PageActions from "@components/PageActions";
import { getStep3PDF } from "@http/Export";
import { getControlAction } from "@http/Step2/Interactions/ControlActions";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { BiExport as PdfIcon } from "react-icons/bi";
import { Navigate, useParams } from "react-router-dom";
import UCAsContainer from "./UCAsContainer";

function UnsafeControlActions() {
	const { id, controlActionId } = useParams();
	if (!id) return <Navigate to="/analyses" />;
	if (!controlActionId) return <Navigate to={`/analyses/${id}`} />;

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Control Action

	const {
		data: controlAction,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["control-action-unsafe-page", controlActionId],
		queryFn: () => getControlAction(controlActionId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Step 3 PDF

	const [modalStep3PdfOpen, setModalStep3PdfOpen] = useState(false);
	const toggleModalStep3Pdf = () => setModalStep3PdfOpen(!modalStep3PdfOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || controlAction === undefined) return <h1>Error</h1>;
	return (
		<>
			<AnalysisHeader analysisId={id} controlAction={controlAction.name} icon="step3" />
			<UCAsContainer controlAction={controlAction} />
			<PageActions>
				<Button variant="dark" icon={PdfIcon} onClick={toggleModalStep3Pdf}>
					Export Step 3
				</Button>
				<ModalPDFPreview
					open={modalStep3PdfOpen}
					onClose={toggleModalStep3Pdf}
					fetchPDF={getStep3PDF}
					analysisId={id}
					title={"Export Step 3"}
				/>
			</PageActions>
		</>
	);
}

export default UnsafeControlActions;
