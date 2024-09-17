import DualButton from "@components/Button/DualButton";
import Container from "@components/Container";
import { ListItem as Loss } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import Loader from "@components/Loader";
import { ModalConfirm } from "@components/Modal";
import { ModalLoss } from "@components/Modal/ModalEntity";
import { createLoss, deleteLoss, getLosses, updateLoss } from "@http/Step1/Losses";
import { ILossFormData, ILossInsertDto, ILossReadDto, ILossUpdateDto } from "@interfaces/IStep1";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";

interface LossesContainerProps {
	analysisId: string;
}
function LossesContainer({ analysisId }: LossesContainerProps) {
	const queryClient = useQueryClient();
	const [selectedLoss, setSelectedLoss] = useState<ILossReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Loss

	const [modalCreateLossOpen, setModalCreateLossOpen] = useState(false);
	const toggleModalCreateLoss = () => setModalCreateLossOpen(!modalCreateLossOpen);

	const { mutateAsync: requestCreateLoss, isPending: isCreating } = useMutation({
		mutationFn: (loss: ILossInsertDto) => createLoss(loss),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-losses"] });
			queryClient.invalidateQueries({ queryKey: ["losses-multi-select"] });
			toast.success("Loss created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateLoss = async (lossData: ILossFormData) => {
		const loss: ILossInsertDto = {
			...lossData,
			analysisId: analysisId
		};
		await requestCreateLoss(loss);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Loss

	const [modalUpdateLossOpen, setModalUpdateLossOpen] = useState(false);
	const toggleModalUpdateLoss = () => setModalUpdateLossOpen(!modalUpdateLossOpen);

	const { mutateAsync: requestUpdateLoss, isPending: isUpdating } = useMutation({
		mutationFn: ({ id, loss }: { id: string; loss: ILossUpdateDto }) => updateLoss(id, loss),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-losses"] });
			queryClient.invalidateQueries({ queryKey: ["losses-multi-select"] });
			toast.success("Loss updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateLoss = async (lossData: ILossFormData) => {
		const loss: ILossUpdateDto = { ...lossData };
		if (selectedLoss) {
			await requestUpdateLoss({ id: selectedLoss.id, loss });
			setSelectedLoss(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Loss

	const [modalDeleteLossOpen, setModalDeleteLossOpen] = useState(false);
	const toggleModalDeleteLoss = () => setModalDeleteLossOpen(!modalDeleteLossOpen);

	const { mutateAsync: requestDeleteLoss, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteLoss(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-losses"] });
			queryClient.invalidateQueries({ queryKey: ["losses-multi-select"] });
			toast.success("Loss deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteLoss = async () => {
		if (selectedLoss) {
			await requestDeleteLoss(selectedLoss.id);
			toggleModalDeleteLoss();
			setSelectedLoss(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Losses

	const {
		data: losses,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["analysis-losses", analysisId],
		queryFn: () => getLosses(analysisId)
	});

	if (isLoading) return <Loader />;
	if (isError || losses === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="Losses" onClick={toggleModalCreateLoss}>
				<ListWrapper>
					{losses.map(loss => (
						<Loss.Root key={loss.id}>
							<Loss.Name code={loss.code} name={loss.name} />
							<Loss.Actions>
								<DualButton
									onEdit={() => {
										setSelectedLoss(loss);
										toggleModalUpdateLoss();
									}}
									onDelete={() => {
										setSelectedLoss(loss);
										toggleModalDeleteLoss();
									}}
								/>
							</Loss.Actions>
						</Loss.Root>
					))}
				</ListWrapper>
			</Container>
			<ModalLoss
				open={modalCreateLossOpen}
				onClose={toggleModalCreateLoss}
				onSubmit={handleCreateLoss}
				isLoading={isCreating}
				title="New Loss"
				btnText="Create"
			/>
			<ModalLoss
				open={modalUpdateLossOpen}
				onClose={toggleModalUpdateLoss}
				onSubmit={handleUpdateLoss}
				isLoading={isUpdating}
				title="Update Loss"
				btnText="Update"
				loss={selectedLoss || undefined}
			/>
			<ModalConfirm
				open={modalDeleteLossOpen}
				onClose={toggleModalDeleteLoss}
				onConfirm={handleDeleteLoss}
				isLoading={isDeleting}
				message="Do you want to delete this loss?"
				title="Delete Loss"
				btnText="Delete"
			/>
		</>
	);
}

export default LossesContainer;
