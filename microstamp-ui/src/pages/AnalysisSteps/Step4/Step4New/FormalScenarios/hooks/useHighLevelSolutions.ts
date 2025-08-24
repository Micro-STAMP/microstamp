import {
	getHighLevelSolutionsByUCA,
	updateHighLevelSolutions
} from "@http/Step4New/HighLevelSolutions";
import {
	IHighLevelSolutionsFormData,
	IHighLevelSolutionsUpdateDto
} from "@interfaces/IStep4New/IHighLevelSolutions";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";

function useHighLevelSolutions(ucaId: string) {
	const queryClient = useQueryClient();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get High Level Solutions

	const {
		data: highLevelSolutions,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["high-level-solutions", ucaId],
		queryFn: () => getHighLevelSolutionsByUCA(ucaId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Invalidate High Level Solutions

	const invalidateHighLevelSolutions = () => {
		queryClient.invalidateQueries({ queryKey: ["high-level-solutions"] });
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update High Level Solutions

	const { mutateAsync: requestUpdateHighLevelSolutions, isPending } = useMutation({
		mutationFn: ({
			hlsData,
			hlsId
		}: {
			hlsData: IHighLevelSolutionsUpdateDto;
			hlsId: string;
		}) => updateHighLevelSolutions(hlsData, hlsId),
		onSuccess: () => {
			invalidateHighLevelSolutions();
			toast.success("High Level Solutions updated successfully.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateHighLevelSolutions = async (
		hlsData: IHighLevelSolutionsFormData,
		hlsId: string
	) => {
		const updatedSolutions: IHighLevelSolutionsUpdateDto = {
			controllerBehavior: hlsData.controllerBehavior,
			otherSolutions: hlsData.otherSolutions,
			processBehavior: hlsData.processBehavior
		};
		await requestUpdateHighLevelSolutions({
			hlsData: updatedSolutions,
			hlsId
		});
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return {
		highLevelSolutions,
		onUpdateHighLevelSolution: handleUpdateHighLevelSolutions,
		isLoading,
		isUpdating: isPending,
		isError
	};
}

export default useHighLevelSolutions;
