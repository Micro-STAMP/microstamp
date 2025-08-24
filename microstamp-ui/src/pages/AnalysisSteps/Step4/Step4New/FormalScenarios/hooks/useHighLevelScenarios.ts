import { getFormalScenariosByUCA } from "@http/Step4New/FormalScenarios";
import { useQuery } from "@tanstack/react-query";

function useHighLevelScenarios(ucaId: string) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get High Level Scenarios

	const {
		data: formalScenarios,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["high-level-scenarios", ucaId],
		queryFn: () => getFormalScenariosByUCA(ucaId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return {
		formalScenarios,
		isLoading,
		isError
	};
}

export default useHighLevelScenarios;
