import Button from "@components/Button";
import DualButton from "@components/Button/DualButton";
import Container from "@components/Container";
import { ListItem as ConnectionAction } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalConfirm } from "@components/Modal";
import { ModalConnectionAction } from "@components/Modal/ModalEntity";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import {
	createConnectionAction,
	deleteConnectionAction,
	updateConnectionAction
} from "@http/Step2/ConnectionActions";
import {
	IConnectionActionFormData,
	IConnectionActionInsertDto,
	IConnectionActionReadDto,
	IConnectionActionType,
	IConnectionActionUpdateDto,
	IConnectionReadDto
} from "@interfaces/IStep2";
import { connectionActionTypeToSelectOption } from "@interfaces/IStep2/IConnectionAction/Enums";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { BiX as CloseIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalConnectionActionsProps extends ModalProps {
	connection: IConnectionReadDto;
}
function ModalConnectionActions({ open, onClose, connection }: ModalConnectionActionsProps) {
	const queryClient = useQueryClient();
	const [selectedConnectionAction, setSelectedConnectionAction] =
		useState<IConnectionActionReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Connection Action

	const [modalCreateConnectionActionOpen, setModalCreateConnectionActionOpen] = useState(false);
	const toggleModalCreateConnectionAction = () =>
		setModalCreateConnectionActionOpen(!modalCreateConnectionActionOpen);

	const { mutateAsync: requestCreateConnectionAction, isPending: isCreating } = useMutation({
		mutationFn: (connectionAction: IConnectionActionInsertDto) =>
			createConnectionAction(connectionAction),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-connections"] });
			toast.success("Connection action created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateConnectionAction = async (
		connectionActionData: IConnectionActionFormData
	) => {
		const connectionAction: IConnectionActionInsertDto = {
			name: connectionActionData.name,
			code: connectionActionData.code,
			connectionActionType: connectionActionData.connectionActionType!
				.value as IConnectionActionType,
			connectionId: connection.id
		};
		await requestCreateConnectionAction(connectionAction);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Connection Action

	const [modalUpdateConnectionActionOpen, setModalUpdateConnectionActionOpen] = useState(false);
	const toggleModalUpdateConnectionAction = () =>
		setModalUpdateConnectionActionOpen(!modalUpdateConnectionActionOpen);

	const { mutateAsync: requestUpdateConnectionAction, isPending: isUpdating } = useMutation({
		mutationFn: ({
			id,
			connectionAction
		}: {
			id: string;
			connectionAction: IConnectionActionUpdateDto;
		}) => updateConnectionAction(id, connectionAction),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-connections"] });
			toast.success("Connection action updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateConnectionAction = async (
		connectionActionData: IConnectionActionFormData
	) => {
		const connectionAction: IConnectionActionUpdateDto = {
			name: connectionActionData.name,
			code: connectionActionData.code,
			connectionActionType: connectionActionData.connectionActionType!
				.value as IConnectionActionType
		};
		if (selectedConnectionAction) {
			await requestUpdateConnectionAction({
				id: selectedConnectionAction.id,
				connectionAction
			});
			setSelectedConnectionAction(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Connection Action

	const [modalDeleteConnectionActionOpen, setModalDeleteConnectionActionOpen] = useState(false);
	const toggleModalDeleteConnectionAction = () =>
		setModalDeleteConnectionActionOpen(!modalDeleteConnectionActionOpen);

	const { mutateAsync: requestDeleteConnectionAction, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteConnectionAction(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-connections"] });
			toast.success("Connection action deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteConnectionAction = async () => {
		if (selectedConnectionAction) {
			await requestDeleteConnectionAction(selectedConnectionAction.id);
			toggleModalDeleteConnectionAction();
			setSelectedConnectionAction(null);
		}
	};

	return (
		<>
			<ModalContainer open={open} size="large">
				<ModalHeader
					title={`${connection.source.name} → ${connection.target.name}`}
					onClose={onClose}
				/>

				<Container title="Connection Actions" onClick={toggleModalCreateConnectionAction}>
					<ListWrapper>
						{connection.connectionActions.map(connectionAction => (
							<ConnectionAction.Root key={connectionAction.id}>
								<ConnectionAction.Name
									code={
										connectionActionTypeToSelectOption(
											connectionAction.connectionActionType
										).label
									}
									name={connectionAction.name}
								/>
								<ConnectionAction.Actions>
									<DualButton
										onEdit={() => {
											setSelectedConnectionAction(connectionAction);
											toggleModalUpdateConnectionAction();
										}}
										onDelete={() => {
											setSelectedConnectionAction(connectionAction);
											toggleModalDeleteConnectionAction();
										}}
									/>
								</ConnectionAction.Actions>
							</ConnectionAction.Root>
						))}
					</ListWrapper>
				</Container>
				<ModalButtons>
					<Button variant="dark" onClick={onClose} size="small" icon={CloseIcon}>
						Close
					</Button>
				</ModalButtons>
			</ModalContainer>
			<ModalConnectionAction
				open={modalCreateConnectionActionOpen}
				onClose={toggleModalCreateConnectionAction}
				onSubmit={handleCreateConnectionAction}
				isLoading={isCreating}
				title="Create Connection Action"
				btnText="Create"
			/>
			<ModalConnectionAction
				open={modalUpdateConnectionActionOpen}
				onClose={toggleModalUpdateConnectionAction}
				onSubmit={handleUpdateConnectionAction}
				isLoading={isUpdating}
				title="Update Connection Action"
				btnText="Update"
				connectionAction={selectedConnectionAction || undefined}
			/>
			<ModalConfirm
				open={modalDeleteConnectionActionOpen}
				onClose={toggleModalDeleteConnectionAction}
				onConfirm={handleDeleteConnectionAction}
				isLoading={isDeleting}
				message="Do you want to delete this connection action?"
				title="Delete Connection Action"
				btnText="Delete"
			/>
		</>
	);
}

export default ModalConnectionActions;
