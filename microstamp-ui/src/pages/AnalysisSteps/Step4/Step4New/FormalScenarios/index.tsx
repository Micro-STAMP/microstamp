import AnalysisHeader from "@components/AnalysisHeader";
import Container from "@components/Container";
import Loader from "@components/Loader";
import { getUnsafeControlAction } from "@http/Step3/UnsafeControlActions";
import { useQuery } from "@tanstack/react-query";
import { Navigate, useParams, useSearchParams } from "react-router-dom";
import HighLevelScenariosContainer from "./HighLevelScenariosContainer";

function FormalScenarios() {
	const { id } = useParams();
	const [searchParams] = useSearchParams();
	const ucaId = searchParams.get("uca");
	if (!id) return <Navigate to="/analyses" />;
	if (!ucaId) return <Navigate to={`/analyses/${id}`} />;

	const {
		data: uca,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["uca-page", ucaId],
		queryFn: () => getUnsafeControlAction(ucaId)
	});

	if (isLoading) return <Loader />;
	if (isError || uca === undefined) return <h1>Error</h1>;
	return (
		<>
			<AnalysisHeader analysisId={id} uca={uca.name} icon="step4" />
			<HighLevelScenariosContainer ucaId={ucaId} />
			<Container title="4.2 Identify High-Level Solutions" justTitle>
				<></>
			</Container>
			<Container title="4.3 Identify Refined Scenarios" justTitle>
				<></>
			</Container>
			<Container title="4.4 Identify Refined Solutions" justTitle>
				<></>
			</Container>
		</>
	);
}

export default FormalScenarios;
