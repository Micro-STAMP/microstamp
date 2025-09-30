import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import { ModalPDFPreview } from "@components/Modal";
import PageActions from "@components/PageActions";
import { getStep4PDF } from "@http/Export";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { ISteps } from "@interfaces/ISteps";
import { useState } from "react";
import { BiExport as PdfIcon } from "react-icons/bi";
import { useOutletContext } from "react-router-dom";
import FourTupleByUCAsContainer from "./FourTupleByUCAsContainer";
import FourTupleContainer from "./FourTupleContainer";

function LossScenarios() {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis

	const analysis: IAnalysisReadDto = useOutletContext();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Step 4 PDF

	const [modalStep4PdfOpen, setModalStep4PdfOpen] = useState(false);
	const toggleModalStep4Pdf = () => setModalStep4PdfOpen(!modalStep4PdfOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	return (
		<>
			<AnalysisHeader analysis={analysis} step={ISteps.STEP_4} />

			<FourTupleContainer analysisId={analysis.id} />
			<FourTupleByUCAsContainer analysisId={analysis.id} />

			<PageActions>
				<Button variant="dark" icon={PdfIcon} onClick={toggleModalStep4Pdf}>
					Export Step 4
				</Button>
				<ModalPDFPreview
					open={modalStep4PdfOpen}
					onClose={toggleModalStep4Pdf}
					fetchPDF={getStep4PDF}
					analysisId={analysis.id}
					title={"Export Step 4"}
				/>
			</PageActions>
		</>
	);
}

export default LossScenarios;
