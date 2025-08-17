import Button from "@components/Button";
import { ModalPDFPreview } from "@components/Modal";
import PageActions from "@components/PageActions";
import { getAnalysis } from "@http/Analyses";
import { getAnalysisPDF } from "@http/Export";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { BiExport as PdfIcon } from "react-icons/bi";
import { Navigate, useParams } from "react-router-dom";
import styles from "./Analysis.module.css";
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

	return (
		<>
			<AnalysisStepsMenu analysisId={id} />
			<div className={styles.analysis_content}>
				<AnalysisDisplay
					analysisId={id}
					analysis={analysis}
					isLoading={isLoading}
					isError={isError || analysis === undefined}
				/>
				<AnalysisImage
					analysisId={id}
					image={analysis?.image}
					isLoading={isLoading}
					isError={isError || analysis === undefined}
				/>
			</div>
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
