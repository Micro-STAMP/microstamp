import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import { ModalPDFPreview } from "@components/Modal";
import PageActions from "@components/PageActions";
import { getStep1PDF } from "@http/Export";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { ISteps } from "@interfaces/ISteps";
import { useState } from "react";
import { BiExport as PdfIcon } from "react-icons/bi";
import { useOutletContext } from "react-router-dom";
import AssumptionsContainer from "./AssumptionsContainer";
import HazardsContainer from "./HazardsContainer";
import LossesContainer from "./LossesContainer";
import SystemGoalsContainer from "./SystemGoalsContainer";
import SystemSafetyConstraintsContainer from "./SystemSafetyConstraintsContainer";

function AnalysisPurpose() {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis

	const analysis: IAnalysisReadDto = useOutletContext();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Step 1 PDF

	const [modalStep1PdfOpen, setModalStep1PdfOpen] = useState(false);
	const toggleModalStep1Pdf = () => setModalStep1PdfOpen(!modalStep1PdfOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<AnalysisHeader analysis={analysis} step={ISteps.STEP_1} />

			<SystemGoalsContainer analysisId={analysis.id} />
			<AssumptionsContainer analysisId={analysis.id} />
			<LossesContainer analysisId={analysis.id} />
			<HazardsContainer analysisId={analysis.id} />
			<SystemSafetyConstraintsContainer analysisId={analysis.id} />

			<PageActions>
				<Button variant="dark" icon={PdfIcon} onClick={toggleModalStep1Pdf}>
					Export Step 1
				</Button>
				<ModalPDFPreview
					open={modalStep1PdfOpen}
					onClose={toggleModalStep1Pdf}
					fetchPDF={getStep1PDF}
					analysisId={analysis.id}
					title={"Export Step 1"}
				/>
			</PageActions>
		</>
	);
}

export default AnalysisPurpose;
