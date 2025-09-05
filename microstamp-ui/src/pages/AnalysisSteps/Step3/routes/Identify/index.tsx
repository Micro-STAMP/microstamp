import Button from "@components/Button";
import NoResultsMessage from "@components/NoResultsMessage";
import PageActions from "@components/PageActions";
import { IAnalysisReadDto } from "@interfaces/IAnalysis";
import { IControlAction } from "@interfaces/IStep2";
import { BiShow } from "react-icons/bi";
import { useNavigate, useOutletContext } from "react-router-dom";
import { ContextTable, RulesContainer } from "./components";

function Identify() {
	const navigate = useNavigate();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Analysis & Control Action

	const [analysis, controlAction] =
		useOutletContext<[analysis: IAnalysisReadDto, controlAction: IControlAction]>();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Validate Variables

	const sourceVariables = controlAction.connection.source.variables;
	const targetVariables = controlAction.connection.target.variables;
	const variables = sourceVariables.concat(targetVariables);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (variables.length === 0)
		return (
			<NoResultsMessage
				message={
					"Error creating context table. The components involved must have at least one variable to generate a context table."
				}
			/>
		);
	return (
		<>
			<RulesContainer analysisId={analysis.id} controlAction={controlAction} />
			<ContextTable controlAction={controlAction} analysisId={analysis.id} />

			<PageActions>
				<Button
					variant="dark"
					icon={BiShow}
					onClick={() =>
						navigate(
							`/analyses/${analysis.id}/control-action/${controlAction.id}/unsafe-control-actions`
						)
					}
				>
					Identified Unsafe Control Actions
				</Button>
			</PageActions>
		</>
	);
}

export default Identify;
