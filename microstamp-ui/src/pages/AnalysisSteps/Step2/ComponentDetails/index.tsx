import AnalysisHeader from "@components/AnalysisHeader";
import Loader from "@components/Loader";
import NoResultsMessage from "@components/NoResultsMessage";
import { getComponent } from "@http/Step2/Components";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { IComponentType } from "@interfaces/IStep2";
import { ISteps } from "@interfaces/ISteps";
import { useQuery } from "@tanstack/react-query";
import { Navigate, useOutletContext, useParams } from "react-router-dom";
import ComponentConnectionsContainer from "./ComponentConnectionsContainer";
import ResponsibilitiesContainer from "./ResponsibilitiesContainer";
import VariablesContainer from "./VariablesContainer";

function ComponentDetails() {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis

	const analysis: IAnalysisReadDto = useOutletContext();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Component

	const { componentId } = useParams();
	if (!componentId) return <Navigate to={`/analyses/${analysis.id}/control-structure`} />;

	const {
		data: component,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["component-details-page", componentId],
		queryFn: () => getComponent(componentId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || component === undefined)
		return <NoResultsMessage message="Error loading component details." />;
	return (
		<>
			<AnalysisHeader analysis={analysis} component={component.name} step={ISteps.STEP_2} />

			{component.type !== IComponentType.ENVIRONMENT && (
				<ResponsibilitiesContainer analysisId={analysis.id} componentId={component.id} />
			)}
			<VariablesContainer componentId={component.id} variables={component.variables} />
			<ComponentConnectionsContainer analysisId={analysis.id} componentId={component.id} />
		</>
	);
}

export default ComponentDetails;
