import Button from "@components/Button";
import { ModalPDFPreview } from "@components/Modal";
import PageActions from "@components/PageActions";
import { getStep3PDF } from "@http/Export";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { IControlAction } from "@interfaces/IStep2";
import { useState } from "react";
import { BiExport } from "react-icons/bi";
import { useOutletContext } from "react-router-dom";
import { UCAsContainer } from "./components";

function List() {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis & Control Action

	const [analysis, controlAction] =
		useOutletContext<[analysis: IAnalysisReadDto, controlAction: IControlAction]>();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Step 3 PDF

	const [modalStep3PdfOpen, setModalStep3PdfOpen] = useState(false);
	const toggleModalStep3Pdf = () => setModalStep3PdfOpen(!modalStep3PdfOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<UCAsContainer controlAction={controlAction} />
			<PageActions>
				<Button variant="dark" icon={BiExport} onClick={toggleModalStep3Pdf}>
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

export default List;
