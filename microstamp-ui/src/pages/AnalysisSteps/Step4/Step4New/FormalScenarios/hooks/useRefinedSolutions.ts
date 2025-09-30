import {
	createRefinedSolution,
	deleteRefinedSolution,
	getRefinedSolutionsByUCA,
	updateRefinedSolution
} from "@http/Step4New/RefinedSolutions";
import {
	IRefinedSolutionFormData,
	IRefinedSolutionInsertDto,
	IRefinedSolutionUpdateDto
} from "@interfaces/IStep4New/IRefinedSolutions";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";

interface useRefinedSolutions {
	ucaId: string;
	onUpdateSuccess?: () => void;
	onDeleteSuccess?: () => void;
}
function useRefinedSolutions({ ucaId, onUpdateSuccess, onDeleteSuccess }: useRefinedSolutions) {
	const queryClient = useQueryClient();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Refined Solutions

	const {
		data: refinedSolutions,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["refined-solutions", ucaId],
		queryFn: () => getRefinedSolutionsByUCA(ucaId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Invalidate Refined Solutions

	const invalidateRefinedSolutions = () => {
		queryClient.invalidateQueries({ queryKey: ["refined-solutions"] });
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Refined Solution

	const { mutateAsync: requestCreateRefinedSolution, isPending: isCreating } = useMutation({
		mutationFn: (solution: IRefinedSolutionInsertDto) => createRefinedSolution(solution),
		onSuccess: () => {
			invalidateRefinedSolutions();
			toast.success("Refined solution created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateRefinedSolution = async (solutionData: IRefinedSolutionFormData) => {
		const solution: IRefinedSolutionInsertDto = {
			mitigation: solutionData.mitigation,
			refinedScenarioId: solutionData.refinedScenario!.value
		};
		await requestCreateRefinedSolution(solution);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Refined Solution

	const { mutateAsync: requestUpdateRefinedSolution, isPending: isUpdating } = useMutation({
		mutationFn: ({ solution, id }: { solution: IRefinedSolutionUpdateDto; id: string }) =>
			updateRefinedSolution(solution, id),
		onSuccess: () => {
			invalidateRefinedSolutions();
			toast.success("Refined solution updated.");
			onUpdateSuccess?.();
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateRefinedSolution = async (
		id: string,
		solutionData: IRefinedSolutionFormData
	) => {
		const solution: IRefinedSolutionUpdateDto = {
			mitigation: solutionData.mitigation
		};
		await requestUpdateRefinedSolution({ id, solution });
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Refined Solution

	const { mutateAsync: requestDeleteRefinedSolution, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteRefinedSolution(id),
		onSuccess: () => {
			invalidateRefinedSolutions();
			toast.success("Refined solution deleted.");
			onDeleteSuccess?.();
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteRefinedSolution = async (id: string) => {
		await requestDeleteRefinedSolution(id);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return {
		refinedSolutions,
		onCreateRefinedSolution: handleCreateRefinedSolution,
		onUpdateRefinedSolution: handleUpdateRefinedSolution,
		onDeleteRefinedSolution: handleDeleteRefinedSolution,
		isLoading,
		isCreating,
		isUpdating,
		isDeleting,
		isError
	};
}

export default useRefinedSolutions;
