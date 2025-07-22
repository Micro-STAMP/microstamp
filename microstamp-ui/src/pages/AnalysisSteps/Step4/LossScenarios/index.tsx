import AnalysisHeader from "@components/AnalysisHeader";
import Button from "@components/Button";
import PageActions from "@components/PageActions";
import { BiExport as PdfIcon } from "react-icons/bi";
import { Navigate, useParams } from "react-router-dom";
import FourTupleByUCAsContainer from "./FourTupleByUCAsContainer";
import FourTupleContainer from "./FourTupleContainer";

function LossScenarios() {
	const { id } = useParams();
	if (!id) return <Navigate to="/analyses" />;

	/* - - - - - - - - - - - - - - - - - - - - - - */
	return (
		<>
			<AnalysisHeader analysisId={id} icon="step4" />
			<FourTupleContainer analysisId={id} />
			<FourTupleByUCAsContainer analysisId={id} />
			<PageActions>
				<Button variant="dark" icon={PdfIcon}>
					Export Step 4
				</Button>
			</PageActions>
		</>
	);
}

export default LossScenarios;
