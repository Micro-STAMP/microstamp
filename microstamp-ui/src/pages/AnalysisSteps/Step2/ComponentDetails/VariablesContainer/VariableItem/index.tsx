import DualButton from "@components/Button/DualButton";
import IconButton from "@components/Button/IconButton";
import { ListItem as Variable } from "@components/Container/ListItem";
import { ModalState } from "@components/Modal/ModalEntity";
import { createState } from "@http/Step2/States";
import { IStateFormData, IStateInsertDto, IVariableReadDto } from "@interfaces/IStep2";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { BiPlus as AddIcon } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./VariableItem.module.css";
import VariableStates from "./VariableStates";

interface VariableItemProps {
	variable: IVariableReadDto;
	selectVariable: (variable: IVariableReadDto) => void;
	modalUpdateVariable: () => void;
	modalDeleteVariable: () => void;
}
function VariableItem({
	variable,
	selectVariable,
	modalDeleteVariable,
	modalUpdateVariable
}: VariableItemProps) {
	const queryClient = useQueryClient();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create State

	const [modalCreateStateOpen, setModalCreateStateOpen] = useState(false);
	const toggleModalCreateState = () => setModalCreateStateOpen(!modalCreateStateOpen);

	const { mutateAsync: requestCreateState, isPending: isCreating } = useMutation({
		mutationFn: (state: IStateInsertDto) => createState(state),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["component-details-page"] });
			toast.success("State created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateState = async (stateData: IStateFormData) => {
		const state: IStateInsertDto = {
			name: stateData.name,
			code: stateData.code,
			variableId: variable.id
		};
		await requestCreateState(state);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Variable Display (Name + Values)

	const variableDisplay = (
		<div className={styles.variable_display}>
			<span>{variable.name}</span>
			<VariableStates states={variable.states} />
		</div>
	);

	return (
		<>
			<Variable.Root key={variable.id}>
				<Variable.Name code={variable.code} name={variableDisplay} />
				<Variable.Actions>
					<IconButton icon={AddIcon} onClick={toggleModalCreateState} />
					<DualButton
						onEdit={() => {
							selectVariable(variable);
							modalUpdateVariable();
						}}
						onDelete={() => {
							selectVariable(variable);
							modalDeleteVariable();
						}}
					/>
				</Variable.Actions>
			</Variable.Root>
			<ModalState
				open={modalCreateStateOpen}
				onClose={toggleModalCreateState}
				onSubmit={handleCreateState}
				isLoading={isCreating}
				title="New State"
				btnText="Create"
			/>
		</>
	);
}

export default VariableItem;
