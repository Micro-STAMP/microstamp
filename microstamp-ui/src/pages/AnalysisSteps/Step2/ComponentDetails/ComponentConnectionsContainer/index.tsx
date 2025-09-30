import Container from "@components/Container";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalConfirm } from "@components/Modal";
import { ModalConnection } from "@components/Modal/ModalEntity";
import { getComponentDependencies } from "@http/Step2/Components";
import { createConnection, deleteConnection, updateConnection } from "@http/Step2/Connections";
import {
	IConnectionFormData,
	IConnectionInsertDto,
	IConnectionReadDto,
	IConnectionUpdateDto
} from "@interfaces/IStep2";
import { IConnectionStyle } from "@interfaces/IStep2/IConnection";
import ConnectionItem from "@pages/AnalysisSteps/Step2/AnalysisControlStructure/ConnectionsContainer/ConnectionItem";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";

interface ComponentConnectionsContainerProps {
	analysisId: string;
	componentId: string;
}
function ComponentConnectionsContainer({
	analysisId,
	componentId
}: ComponentConnectionsContainerProps) {
	const queryClient = useQueryClient();
	const [selectedConnection, setSelectedConnection] = useState<IConnectionReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Component Connection

	const [modalCreateConnectionOpen, setModalCreateConnectionOpen] = useState(false);
	const toggleModalCreateConnection = () =>
		setModalCreateConnectionOpen(!modalCreateConnectionOpen);

	const { mutateAsync: requestCreateConnection, isPending: isCreating } = useMutation({
		mutationFn: (connection: IConnectionInsertDto) => createConnection(connection),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-connections"] });
			queryClient.invalidateQueries({ queryKey: ["component-dependencies-connections"] });
			toast.success("Connection created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateConnection = async (connectionData: IConnectionFormData) => {
		const connection: IConnectionInsertDto = {
			code: connectionData.code,
			style: connectionData.style!.value as IConnectionStyle,
			sourceId: connectionData.source!.value,
			targetId: connectionData.target!.value,
			analysisId: analysisId
		};
		await requestCreateConnection(connection);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Component Connection

	const [modalUpdateConnectionOpen, setModalUpdateConnectionOpen] = useState(false);
	const toggleModalUpdateConnection = () =>
		setModalUpdateConnectionOpen(!modalUpdateConnectionOpen);

	const { mutateAsync: requestUpdateConnection, isPending: isUpdating } = useMutation({
		mutationFn: ({ id, connection }: { id: string; connection: IConnectionUpdateDto }) =>
			updateConnection(id, connection),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-connections"] });
			queryClient.invalidateQueries({ queryKey: ["component-dependencies-connections"] });
			toast.success("Connection updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateConnection = async (connectionData: IConnectionFormData) => {
		const connection: IConnectionUpdateDto = {
			code: connectionData.code,
			style: connectionData.style!.value as IConnectionStyle,
			sourceId: connectionData.source!.value,
			targetId: connectionData.target!.value
		};
		if (selectedConnection) {
			await requestUpdateConnection({ id: selectedConnection.id, connection });
			setSelectedConnection(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Component Connection

	const [modalDeleteConnectionOpen, setModalDeleteConnectionOpen] = useState(false);
	const toggleModalDeleteConnection = () =>
		setModalDeleteConnectionOpen(!modalDeleteConnectionOpen);

	const { mutateAsync: requestDeleteConnection, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteConnection(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-connections"] });
			queryClient.invalidateQueries({ queryKey: ["component-dependencies-connections"] });
			toast.success("Connection deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteConnection = async () => {
		if (selectedConnection) {
			await requestDeleteConnection(selectedConnection.id);
			toggleModalDeleteConnection();
			setSelectedConnection(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Component Connections

	const {
		data: componentDependencies,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["component-dependencies-connections", componentId],
		queryFn: () => getComponentDependencies(componentId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<Container
				title="Connections"
				onClick={toggleModalCreateConnection}
				isLoading={isLoading}
				isError={isError || componentDependencies === undefined}
			>
				<ListWrapper>
					{componentDependencies &&
						componentDependencies.connections.map(connection => (
							<ConnectionItem
								key={connection.id}
								connection={connection}
								modalDeleteConnection={toggleModalDeleteConnection}
								modalUpdateConnection={toggleModalUpdateConnection}
								selectConnection={setSelectedConnection}
							/>
						))}
				</ListWrapper>
			</Container>
			<ModalConnection
				analysisId={analysisId}
				open={modalCreateConnectionOpen}
				onClose={toggleModalCreateConnection}
				onSubmit={handleCreateConnection}
				isLoading={isCreating}
				title="New Connection"
				btnText="Create"
			/>
			<ModalConnection
				analysisId={analysisId}
				open={modalUpdateConnectionOpen}
				onClose={toggleModalUpdateConnection}
				onSubmit={handleUpdateConnection}
				isLoading={isUpdating}
				title="Update Connection"
				btnText="Update"
				connection={selectedConnection || undefined}
			/>
			<ModalConfirm
				open={modalDeleteConnectionOpen}
				onClose={toggleModalDeleteConnection}
				onConfirm={handleDeleteConnection}
				isLoading={isDeleting}
				message="Do you want to delete this connection?"
				title="Delete Connection"
				btnText="Delete"
			/>
		</>
	);
}

export default ComponentConnectionsContainer;
