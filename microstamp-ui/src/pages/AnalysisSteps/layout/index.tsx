import Loader from "@components/Loader";
import NoResultsMessage from "@components/NoResultsMessage";
import { getAnalysis } from "@http/Analyses";
import { useQuery } from "@tanstack/react-query";
import { Navigate, Outlet, useParams } from "react-router-dom";

function AnalysisStepsLayout() {
	const { id } = useParams();
	if (!id) return <Navigate to="/analyses" />;

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis

	const {
		data: analysis,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["analysis-layout", id],
		queryFn: () => getAnalysis(id)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || analysis === undefined)
		return <NoResultsMessage message="Error loading analysis. Try refreshing the page." />;
	return (
		<>
			<Outlet context={analysis} />
		</>
	);
}

export default AnalysisStepsLayout;
