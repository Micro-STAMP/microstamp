import DualButton from "@components/Button/DualButton";
import Container from "@components/Container";
import { ListItem as Assumption } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import Loader from "@components/Loader";
import { ModalConfirm } from "@components/Modal";
import { ModalAssumption } from "@components/Modal/ModalEntity";
import {
	createAssumption,
	deleteAssumption,
	getAssumptions,
	updateAssumption
} from "@http/Step1/Assumptions";
import {
	IAssumptionFormData,
	IAssumptionInsertDto,
	IAssumptionReadDto,
	IAssumptionUpdateDto
} from "@interfaces/IStep1";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";

interface AssumptionsContainerProps {
	analysisId: string;
}
function AssumptionsContainer({ analysisId }: AssumptionsContainerProps) {
	const queryClient = useQueryClient();
	const [selectedAssumption, setSelectedAssumption] = useState<IAssumptionReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Assumption

	const [modalCreateAssumptionOpen, setModalCreateAssumptionOpen] = useState(false);
	const toggleModalCreateAssumption = () =>
		setModalCreateAssumptionOpen(!modalCreateAssumptionOpen);

	const { mutateAsync: requestCreateAssumption, isPending: isCreating } = useMutation({
		mutationFn: (assumption: IAssumptionInsertDto) => createAssumption(assumption),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-assumptions"] });
			toast.success("Assumption created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateAssumption = async (assumptionData: IAssumptionFormData) => {
		const assumption: IAssumptionInsertDto = {
			...assumptionData,
			analysisId: analysisId
		};
		await requestCreateAssumption(assumption);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Assumption

	const [modalUpdateAssumptionOpen, setModalUpdateAssumptionOpen] = useState(false);
	const toggleModalUpdateAssumption = () =>
		setModalUpdateAssumptionOpen(!modalUpdateAssumptionOpen);

	const { mutateAsync: requestUpdateAssumption, isPending: isUpdating } = useMutation({
		mutationFn: ({ id, assumption }: { id: string; assumption: IAssumptionUpdateDto }) =>
			updateAssumption(id, assumption),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-assumptions"] });
			toast.success("Assumption updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateAssumption = async (assumptionData: IAssumptionFormData) => {
		const assumption: IAssumptionUpdateDto = { ...assumptionData };
		if (selectedAssumption) {
			await requestUpdateAssumption({ id: selectedAssumption.id, assumption });
			setSelectedAssumption(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Assumption

	const [modalDeleteAssumptionOpen, setModalDeleteAssumptionOpen] = useState(false);
	const toggleModalDeleteAssumption = () =>
		setModalDeleteAssumptionOpen(!modalDeleteAssumptionOpen);

	const { mutateAsync: requestDeleteAssumption, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteAssumption(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-assumptions"] });
			toast.success("Assumption deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteAssumption = async () => {
		if (selectedAssumption) {
			await requestDeleteAssumption(selectedAssumption.id);
			toggleModalDeleteAssumption();
			setSelectedAssumption(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Assumptions

	const {
		data: assumptions,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["analysis-assumptions", analysisId],
		queryFn: () => getAssumptions(analysisId)
	});

	if (isLoading) return <Loader />;
	if (isError || assumptions === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="Assumptions" onClick={toggleModalCreateAssumption}>
				<ListWrapper>
					{assumptions.map(assumption => (
						<Assumption.Root key={assumption.id}>
							<Assumption.Name code={assumption.code} name={assumption.name} />
							<Assumption.Actions>
								<DualButton
									onEdit={() => {
										setSelectedAssumption(assumption);
										toggleModalUpdateAssumption();
									}}
									onDelete={() => {
										setSelectedAssumption(assumption);
										toggleModalDeleteAssumption();
									}}
								/>
							</Assumption.Actions>
						</Assumption.Root>
					))}
				</ListWrapper>
			</Container>
			<ModalAssumption
				open={modalCreateAssumptionOpen}
				onClose={toggleModalCreateAssumption}
				onSubmit={handleCreateAssumption}
				isLoading={isCreating}
				title="New Assumption"
				btnText="Create"
			/>
			<ModalAssumption
				open={modalUpdateAssumptionOpen}
				onClose={toggleModalUpdateAssumption}
				onSubmit={handleUpdateAssumption}
				isLoading={isUpdating}
				title="Update Assumption"
				btnText="Update"
				assumption={selectedAssumption || undefined}
			/>
			<ModalConfirm
				open={modalDeleteAssumptionOpen}
				onClose={toggleModalDeleteAssumption}
				onConfirm={handleDeleteAssumption}
				isLoading={isDeleting}
				message="Do you want to delete this assumption?"
				title="Delete Assumption"
				btnText="Delete"
			/>
		</>
	);
}

export default AssumptionsContainer;
