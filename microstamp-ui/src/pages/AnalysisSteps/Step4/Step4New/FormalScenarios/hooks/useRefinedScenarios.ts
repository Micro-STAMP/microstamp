import {
	createRefinedScenario,
	deleteRefinedScenario,
	getRefinedScenariosByUCA,
	updateRefinedScenario
} from "@http/Step4New/RefinedScenarios";
import {
	IRefinedScenarioFormData,
	IRefinedScenarioInsertDto,
	IRefinedScenarioUpdateDto
} from "@interfaces/IStep4New/IRefinedScenarios";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";

interface useRefinedScenariosProps {
	ucaId: string;
	onUpdateSuccess?: () => void;
	onDeleteSuccess?: () => void;
}
function useRefinedScenarios({
	ucaId,
	onUpdateSuccess,
	onDeleteSuccess
}: useRefinedScenariosProps) {
	const queryClient = useQueryClient();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Refined Scenarios

	const {
		data: refinedScenarios,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["refined-scenarios", ucaId],
		queryFn: () => getRefinedScenariosByUCA(ucaId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Invalidate Refined Scenario

	const invalidateRefinedScenarios = () => {
		queryClient.invalidateQueries({ queryKey: ["refined-scenarios"] });
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Refined Scenario

	const { mutateAsync: requestCreateRefinedScenario, isPending: isCreating } = useMutation({
		mutationFn: (scenario: IRefinedScenarioInsertDto) => createRefinedScenario(scenario),
		onSuccess: () => {
			invalidateRefinedScenarios();
			toast.success("Refined scenario created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateRefinedScenario = async (scenarioData: IRefinedScenarioFormData) => {
		const scenario: IRefinedScenarioInsertDto = {
			commonCauseId: scenarioData.commonCauseId,
			formalScenarioClassId: scenarioData.formalScenarioClassId,
			refinedScenario: scenarioData.refinedScenario,
			unsafeControlActionId: ucaId
		};
		await requestCreateRefinedScenario(scenario);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Refined Scenario

	const { mutateAsync: requestUpdateRefinedScenario, isPending: isUpdating } = useMutation({
		mutationFn: ({ scenario, id }: { scenario: IRefinedScenarioUpdateDto; id: string }) =>
			updateRefinedScenario(id, scenario),
		onSuccess: () => {
			invalidateRefinedScenarios();
			toast.success("Refined scenario updated.");
			onUpdateSuccess?.();
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateRefinedScenario = async (
		id: string,
		scenarioData: IRefinedScenarioFormData
	) => {
		const scenario: IRefinedScenarioUpdateDto = {
			commonCauseId: scenarioData.commonCauseId,
			formalScenarioClassId: scenarioData.formalScenarioClassId,
			refinedScenario: scenarioData.refinedScenario
		};
		await requestUpdateRefinedScenario({ id, scenario });
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Refined Scenario

	const { mutateAsync: requestDeleteRefinedScenario, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteRefinedScenario(id),
		onSuccess: () => {
			invalidateRefinedScenarios();
			toast.success("Refined scenario deleted.");
			onDeleteSuccess?.();
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteRefinedScenario = async (id: string) => {
		await requestDeleteRefinedScenario(id);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return {
		refinedScenarios,
		onCreateRefinedScenario: handleCreateRefinedScenario,
		onUpdateRefinedScenario: handleUpdateRefinedScenario,
		onDeleteRefinedScenario: handleDeleteRefinedScenario,
		isLoading,
		isCreating,
		isUpdating,
		isDeleting,
		isError
	};
}

export default useRefinedScenarios;
