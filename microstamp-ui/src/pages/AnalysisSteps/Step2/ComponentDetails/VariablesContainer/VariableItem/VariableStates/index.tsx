import { ModalConfirm } from "@components/Modal";
import { deleteState } from "@http/Step2/States";
import { IStateReadDto } from "@interfaces/IStep2";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { BiX as X } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./VariableStates.module.css";

interface VariableStatesProps {
	states: IStateReadDto[];
}
function VariableStates({ states }: VariableStatesProps) {
	const queryClient = useQueryClient();
	const [selectedState, setSelectedState] = useState<IStateReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete State

	const [modalDeleteStateOpen, setModalDeleteStateOpen] = useState(false);
	const toggleModalDeleteState = () => setModalDeleteStateOpen(!modalDeleteStateOpen);

	const { mutateAsync: requestDeleteState, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteState(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["component-details-page"] });
			toast.success("State deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteState = async () => {
		if (selectedState) {
			await requestDeleteState(selectedState.id);
			toggleModalDeleteState();
			setSelectedState(null);
		}
	};

	return (
		<>
			<div className={styles.states_list}>
				{states.map(state => (
					<div className={styles.state_item} key={state.id}>
						<button
							type="button"
							onClick={() => {
								setSelectedState(state);
								toggleModalDeleteState();
							}}
						>
							<X />
						</button>
						<span>{state.name}</span>
					</div>
				))}
			</div>
			<ModalConfirm
				open={modalDeleteStateOpen}
				onClose={toggleModalDeleteState}
				onConfirm={handleDeleteState}
				isLoading={isDeleting}
				message="Do you want to delete this state?"
				title="Delete State"
				btnText="Delete"
			/>
		</>
	);
}

export default VariableStates;
