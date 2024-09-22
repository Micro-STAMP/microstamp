import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import { ModalPDFPreview } from "@components/Modal";
import PageActions from "@components/PageActions";
import { getStep1PDF } from "@http/Export";
import { useState } from "react";
import { BiExport as PdfIcon } from "react-icons/bi";
import { Navigate, useParams } from "react-router-dom";
import AssumptionsContainer from "./AssumptionsContainer";
import HazardsContainer from "./HazardsContainer";
import LossesContainer from "./LossesContainer";
import SystemGoalsContainer from "./SystemGoalsContainer";
import SystemSafetyConstraintsContainer from "./SystemSafetyConstraintsContainer";

function AnalysisPurpose() {
	const { id } = useParams();
	if (!id) return <Navigate to="/analyses" />;

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Step 1 PDF

	const [modalStep1PdfOpen, setModalStep1PdfOpen] = useState(false);
	const toggleModalStep1Pdf = () => setModalStep1PdfOpen(!modalStep1PdfOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<AnalysisHeader analysisId={id} icon="step1" />
			<SystemGoalsContainer analysisId={id} />
			<AssumptionsContainer analysisId={id} />
			<LossesContainer analysisId={id} />
			<HazardsContainer analysisId={id} />
			<SystemSafetyConstraintsContainer analysisId={id} />
			<PageActions>
				<Button variant="dark" icon={PdfIcon} onClick={toggleModalStep1Pdf}>
					Export Step 1
				</Button>
				<ModalPDFPreview
					open={modalStep1PdfOpen}
					onClose={toggleModalStep1Pdf}
					fetchPDF={getStep1PDF}
					analysisId={id}
					title={"Export Step 1"}
				/>
			</PageActions>
		</>
	);
}

export default AnalysisPurpose;
