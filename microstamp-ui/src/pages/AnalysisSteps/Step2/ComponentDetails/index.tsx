import AnalysisHeader from "@components/AnalysisHeader";
import Loader from "@components/Loader";
import { getComponent } from "@http/Step2/Components";
import { IComponentType } from "@interfaces/IStep2";
import { useQuery } from "@tanstack/react-query";
import { Navigate, useParams } from "react-router-dom";
import ComponentConnectionsContainer from "./ComponentConnectionsContainer";
import ResponsibilitiesContainer from "./ResponsibilitiesContainer";
import VariablesContainer from "./VariablesContainer";

function ComponentDetails() {
	const { id, componentId } = useParams();
	if (!id) return <Navigate to="/analyses" />;
	if (!componentId) return <Navigate to={`/analyses/${id}/control-structure`} />;

	const {
		data: component,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["component-details-page", componentId],
		queryFn: () => getComponent(componentId)
	});

	if (isLoading) return <Loader />;
	if (isError || component === undefined) return <h1>Error</h1>;
	return (
		<>
			<AnalysisHeader analysisId={id} component={component.name} icon="step2" />
			{component.type !== IComponentType.ENVIRONMENT && (
				<ResponsibilitiesContainer analysisId={id} componentId={component.id} />
			)}
			<ComponentConnectionsContainer analysisId={id} componentId={component.id} />
			<VariablesContainer componentId={component.id} variables={component.variables} />
		</>
	);
}

export default ComponentDetails;
