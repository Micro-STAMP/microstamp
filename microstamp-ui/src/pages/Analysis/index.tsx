import Button from "@components/Button";
import Loader from "@components/Loader";
import { ModalPDFPreview } from "@components/Modal";
import PageActions from "@components/PageActions";
import { getAnalysis } from "@http/Analyses";
import { getAnalysisPDF } from "@http/Export";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { BiExport as PdfIcon } from "react-icons/bi";
import { Navigate, useParams } from "react-router-dom";
import AnalysisDisplay from "./AnalysisDisplay";
import AnalysisImage from "./AnalysisImage";
import AnalysisStepsMenu from "./AnalysisStepsMenu";

function Analysis() {
	const { id } = useParams();
	if (!id) return <Navigate to="/analyses" />;

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis PDF

	const [modalAnalysisPdfOpen, setModalAnalysisPdfOpen] = useState(false);
	const toggleModalAnalysisPdf = () => setModalAnalysisPdfOpen(!modalAnalysisPdfOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis

	const {
		data: analysis,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["analysis-display", id],
		queryFn: () => getAnalysis(id)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || analysis === undefined) return <h1>Error</h1>;
	return (
		<>
			<AnalysisStepsMenu analysisId={id} />
			<AnalysisDisplay analysis={analysis} />
			<AnalysisImage analysisId={analysis.id} image={analysis.image} />
			<PageActions>
				<Button variant="dark" icon={PdfIcon} onClick={toggleModalAnalysisPdf}>
					Export Analysis
				</Button>
				<ModalPDFPreview
					open={modalAnalysisPdfOpen}
					onClose={toggleModalAnalysisPdf}
					fetchPDF={getAnalysisPDF}
					analysisId={id}
					title={"Export Analysis"}
				/>
			</PageActions>
		</>
	);
}

export default Analysis;
