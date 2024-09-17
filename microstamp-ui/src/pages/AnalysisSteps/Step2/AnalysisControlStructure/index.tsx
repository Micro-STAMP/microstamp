import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import { ModalPDFPreview } from "@components/Modal";
import PageActions from "@components/PageActions";
import { getStep2PDF } from "@http/Export";
import { useState } from "react";
import { BiExport as PdfIcon } from "react-icons/bi";
import { Navigate, useParams } from "react-router-dom";
import ComponentsContainer from "./ComponentsContainer";
import ConnectionsContainer from "./ConnectionsContainer";
import ControlStructureImage from "./ControlStructureImage";

function AnalysisControlStructure() {
	const { id } = useParams();
	if (!id) return <Navigate to="/analyses" />;

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Step 2 PDF

	const [modalStep2PdfOpen, setModalStep2PdfOpen] = useState(false);
	const toggleModalStep2Pdf = () => setModalStep2PdfOpen(!modalStep2PdfOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<AnalysisHeader analysisId={id} icon="step2" />
			<ComponentsContainer analysisId={id} />
			<ConnectionsContainer analysisId={id} />
			<ControlStructureImage analysisId={id} />
			<PageActions>
				<Button variant="dark" icon={PdfIcon} onClick={toggleModalStep2Pdf}>
					Export Step 2
				</Button>
				<ModalPDFPreview
					open={modalStep2PdfOpen}
					onClose={toggleModalStep2Pdf}
					fetchPDF={getStep2PDF}
					analysisId={id}
					title={"Export Step 2"}
				/>
			</PageActions>
		</>
	);
}

export default AnalysisControlStructure;
