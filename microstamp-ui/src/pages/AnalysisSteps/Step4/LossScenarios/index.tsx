import AnalysisHeader from "@components/AnalysisHeader";
import { Navigate, useParams } from "react-router-dom";

function LossScenarios() {
	const { id } = useParams();
	if (!id) return <Navigate to="/analyses" />;

	/* - - - - - - - - - - - - - - - - - - - - - - */
	return (
		<>
			<AnalysisHeader analysisId={id} icon="step4" />
		</>
	);
}

export default LossScenarios;
