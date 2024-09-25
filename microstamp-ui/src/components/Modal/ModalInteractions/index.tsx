import Button from "@components/Button";
import DualButton from "@components/Button/DualButton";
import Container from "@components/Container";
import { ListItem as Interaction } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalConfirm } from "@components/Modal";
import { ModalInteraction } from "@components/Modal/ModalEntity";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import {
	createInteraction,
	deleteInteraction,
	updateInteraction
} from "@http/Step2/Interactions";
import {
	IInteractionFormData,
	IInteractionInsertDto,
	IInteractionReadDto,
	IInteractionType,
	IInteractionUpdateDto,
	IConnectionReadDto
} from "@interfaces/IStep2";
import { interactionTypeToSelectOption } from "@interfaces/IStep2/IInteraction/Enums";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { BiX as CloseIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalInteractionsProps extends ModalProps {
	connection: IConnectionReadDto;
}
function ModalInteractions({ open, onClose, connection }: ModalInteractionsProps) {
	const queryClient = useQueryClient();
	const [selectedInteraction, setSelectedInteraction] =
		useState<IInteractionReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Interaction

	const [modalCreateInteractionOpen, setModalCreateInteractionOpen] = useState(false);
	const toggleModalCreateInteraction = () =>
		setModalCreateInteractionOpen(!modalCreateInteractionOpen);

	const { mutateAsync: requestCreateInteraction, isPending: isCreating } = useMutation({
		mutationFn: (interaction: IInteractionInsertDto) =>
			createInteraction(interaction),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-connections"] });
			toast.success("Interaction created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateInteraction = async (
		interactionData: IInteractionFormData
	) => {
		const interaction: IInteractionInsertDto = {
			name: interactionData.name,
			code: interactionData.code,
			interactionType: interactionData.interactionType!
				.value as IInteractionType,
			connectionId: connection.id
		};
		await requestCreateInteraction(interaction);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Interaction

	const [modalUpdateInteractionOpen, setModalUpdateInteractionOpen] = useState(false);
	const toggleModalUpdateInteraction = () =>
		setModalUpdateInteractionOpen(!modalUpdateInteractionOpen);

	const { mutateAsync: requestUpdateInteraction, isPending: isUpdating } = useMutation({
		mutationFn: ({
			id,
			interaction
		}: {
			id: string;
			interaction: IInteractionUpdateDto;
		}) => updateInteraction(id, interaction),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-connections"] });
			toast.success("Interaction updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateInteraction = async (
		interactionData: IInteractionFormData
	) => {
		const interaction: IInteractionUpdateDto = {
			name: interactionData.name,
			code: interactionData.code,
			interactionType: interactionData.interactionType!
				.value as IInteractionType
		};
		if (selectedInteraction) {
			await requestUpdateInteraction({
				id: selectedInteraction.id,
				interaction
			});
			setSelectedInteraction(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Interaction

	const [modalDeleteInteractionOpen, setModalDeleteInteractionOpen] = useState(false);
	const toggleModalDeleteInteraction = () =>
		setModalDeleteInteractionOpen(!modalDeleteInteractionOpen);

	const { mutateAsync: requestDeleteInteraction, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteInteraction(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-connections"] });
			toast.success("Interaction deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteInteraction = async () => {
		if (selectedInteraction) {
			await requestDeleteInteraction(selectedInteraction.id);
			toggleModalDeleteInteraction();
			setSelectedInteraction(null);
		}
	};

	return (
		<>
			<ModalContainer open={open} size="large">
				<ModalHeader
					title={`${connection.source.name} → ${connection.target.name}`}
					onClose={onClose}
				/>

				<Container title="Interactions" onClick={toggleModalCreateInteraction}>
					<ListWrapper>
						{connection.interactions.map(interaction => (
							<Interaction.Root key={interaction.id}>
								<Interaction.Name
									code={
										interactionTypeToSelectOption(
											interaction.interactionType
										).label
									}
									name={interaction.name}
								/>
								<Interaction.Actions>
									<DualButton
										onEdit={() => {
											setSelectedInteraction(interaction);
											toggleModalUpdateInteraction();
										}}
										onDelete={() => {
											setSelectedInteraction(interaction);
											toggleModalDeleteInteraction();
										}}
									/>
								</Interaction.Actions>
							</Interaction.Root>
						))}
					</ListWrapper>
				</Container>
				<ModalButtons>
					<Button variant="dark" onClick={onClose} size="small" icon={CloseIcon}>
						Close
					</Button>
				</ModalButtons>
			</ModalContainer>
			<ModalInteraction
				open={modalCreateInteractionOpen}
				onClose={toggleModalCreateInteraction}
				onSubmit={handleCreateInteraction}
				isLoading={isCreating}
				title="Create Interaction"
				btnText="Create"
			/>
			<ModalInteraction
				open={modalUpdateInteractionOpen}
				onClose={toggleModalUpdateInteraction}
				onSubmit={handleUpdateInteraction}
				isLoading={isUpdating}
				title="Update Interaction"
				btnText="Update"
				interaction={selectedInteraction || undefined}
			/>
			<ModalConfirm
				open={modalDeleteInteractionOpen}
				onClose={toggleModalDeleteInteraction}
				onConfirm={handleDeleteInteraction}
				isLoading={isDeleting}
				message="Do you want to delete this interaction?"
				title="Delete Interaction"
				btnText="Delete"
			/>
		</>
	);
}

export default ModalInteractions;
