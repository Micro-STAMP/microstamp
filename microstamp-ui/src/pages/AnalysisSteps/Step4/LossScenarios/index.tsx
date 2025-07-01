import AnalysisHeader from "@components/AnalysisHeader";
import { Navigate, useParams } from "react-router-dom";
import FourTupleContainer from "./FourTupleContainer";

function LossScenarios() {
	const { id } = useParams();
	if (!id) return <Navigate to="/analyses" />;

	/* - - - - - - - - - - - - - - - - - - - - - - */
	return (
		<>
			<AnalysisHeader analysisId={id} icon="step4" />
			<FourTupleContainer analysisId={id} />
		</>
	);
}

export default LossScenarios;
