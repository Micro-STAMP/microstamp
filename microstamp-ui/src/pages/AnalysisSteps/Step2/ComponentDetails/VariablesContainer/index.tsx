import Container from "@components/Container";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalConfirm } from "@components/Modal";
import { ModalVariable } from "@components/Modal/ModalEntity";
import { createVariable, deleteVariable, updateVariable } from "@http/Step2/Variables";
import {
	IVariableFormData,
	IVariableInsertDto,
	IVariableReadDto,
	IVariableUpdateDto
} from "@interfaces/IStep2";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";
import VariableItem from "./VariableItem";

interface VariablesContainerProps {
	componentId: string;
	variables: IVariableReadDto[];
}
function VariablesContainer({ componentId, variables }: VariablesContainerProps) {
	const queryClient = useQueryClient();
	const [selectedVariable, setSelectedVariable] = useState<IVariableReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Variable

	const [modalCreateVariableOpen, setModalCreateVariableOpen] = useState(false);
	const toggleModalCreateVariable = () => setModalCreateVariableOpen(!modalCreateVariableOpen);

	const { mutateAsync: requestCreateVariable, isPending: isCreating } = useMutation({
		mutationFn: (variable: IVariableInsertDto) => createVariable(variable),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["component-details-page"] });
			toast.success("Variable created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateVariable = async (variableData: IVariableFormData) => {
		const variable: IVariableInsertDto = {
			name: variableData.name,
			code: variableData.code,
			componentId: componentId
		};
		await requestCreateVariable(variable);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Variable

	const [modalUpdateVariableOpen, setModalUpdateVariableOpen] = useState(false);
	const toggleModalUpdateVariable = () => setModalUpdateVariableOpen(!modalUpdateVariableOpen);

	const { mutateAsync: requestUpdateVariable, isPending: isUpdating } = useMutation({
		mutationFn: ({ id, variable }: { id: string; variable: IVariableUpdateDto }) =>
			updateVariable(id, variable),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["component-details-page"] });
			toast.success("Variable updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateVariable = async (variableData: IVariableFormData) => {
		const variable: IVariableUpdateDto = {
			name: variableData.name,
			code: variableData.code
		};
		if (selectedVariable) {
			await requestUpdateVariable({ id: selectedVariable.id, variable });
			setSelectedVariable(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Variable

	const [modalDeleteVariableOpen, setModalDeleteVariableOpen] = useState(false);
	const toggleModalDeleteVariable = () => setModalDeleteVariableOpen(!modalDeleteVariableOpen);

	const { mutateAsync: requestDeleteVariable, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteVariable(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["component-details-page"] });
			toast.success("Variable deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteVariable = async () => {
		if (selectedVariable) {
			await requestDeleteVariable(selectedVariable.id);
			toggleModalDeleteVariable();
			setSelectedVariable(null);
		}
	};

	return (
		<>
			<Container title="Variables" onClick={toggleModalCreateVariable}>
				<ListWrapper>
					{variables.map(variable => (
						<VariableItem
							key={variable.id}
							variable={variable}
							modalDeleteVariable={toggleModalDeleteVariable}
							modalUpdateVariable={toggleModalUpdateVariable}
							selectVariable={setSelectedVariable}
						/>
					))}
				</ListWrapper>
			</Container>
			<ModalVariable
				open={modalCreateVariableOpen}
				onClose={toggleModalCreateVariable}
				onSubmit={handleCreateVariable}
				isLoading={isCreating}
				title="New Variable"
				btnText="Create"
			/>
			<ModalVariable
				open={modalUpdateVariableOpen}
				onClose={toggleModalUpdateVariable}
				onSubmit={handleUpdateVariable}
				isLoading={isUpdating}
				title="Update Variable"
				btnText="Update"
				variable={selectedVariable || undefined}
			/>
			<ModalConfirm
				open={modalDeleteVariableOpen}
				onClose={toggleModalDeleteVariable}
				onConfirm={handleDeleteVariable}
				isLoading={isDeleting}
				message="Do you want to delete this variable?"
				title="Delete Variable"
				btnText="Delete"
			/>
		</>
	);
}

export default VariablesContainer;
