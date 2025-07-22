import DualButton from "@components/Button/DualButton";
import IconButton from "@components/Button/IconButton";
import Container from "@components/Container";
import { ListItem as FourTuple } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import Loader from "@components/Loader";
import { ModalConfirm } from "@components/Modal";
import { ModalFourTuple } from "@components/Modal/ModalEntity";
import { ModalFourTupleDetails } from "@components/Modal/ModalEntity/ModalStep4";
import {
	createFourTuple,
	deleteFourTuple,
	getFourTuples,
	updateFourTuple
} from "@http/Step4/FourTuple";
import {
	IFourTupleFormData,
	IFourTupleInsertDto,
	IFourTupleReadDto,
	IFourTupleUpdateDto
} from "@interfaces/IStep4";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { BiInfoCircle as InfoIcon } from "react-icons/bi";
import { toast } from "sonner";

interface FourTupleContainerProps {
	analysisId: string;
}
function FourTupleContainer({ analysisId }: FourTupleContainerProps) {
	const queryClient = useQueryClient();
	const [selectedTuple, setSelectedTuple] = useState<IFourTupleReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Four Tuple Details

	const [modalFourTupleDetailsOpen, setModalFourTupleDetailsOpen] = useState(false);
	const toggleModalFourTupleDetails = () =>
		setModalFourTupleDetailsOpen(!modalFourTupleDetailsOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Four Tuple

	const [modalCreateFourTupleOpen, setModalCreateFourTupleOpen] = useState(false);
	const toggleModalCreateFourTuple = () => setModalCreateFourTupleOpen(!modalCreateFourTupleOpen);

	const { mutateAsync: requestCreateFourTuple, isPending: isCreating } = useMutation({
		mutationFn: (tuple: IFourTupleInsertDto) => createFourTuple(tuple),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["four-tuples"] });
			toast.success("Loss scenario created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateFourTuple = async (tupleData: IFourTupleFormData) => {
		const tuple: IFourTupleInsertDto = {
			...tupleData,
			unsafeControlActionIds: tupleData.unsafeControlActions.map(ca => ca.value),
			analysisId: analysisId
		};
		await requestCreateFourTuple(tuple);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Four Tuple

	const [modalUpdateFourTupleOpen, setModalUpdateFourTupleOpen] = useState(false);
	const toggleModalUpdateFourTuple = () => setModalUpdateFourTupleOpen(!modalUpdateFourTupleOpen);

	const { mutateAsync: requestUpdateFourTuple, isPending: isUpdating } = useMutation({
		mutationFn: ({ id, tuple }: { id: string; tuple: IFourTupleUpdateDto }) =>
			updateFourTuple(id, tuple),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["four-tuples"] });
			toast.success("Loss scenario updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateFourTuple = async (tupleData: IFourTupleFormData) => {
		const tuple: IFourTupleUpdateDto = {
			...tupleData,
			unsafeControlActionIds: tupleData.unsafeControlActions.map(ca => ca.value)
		};
		if (selectedTuple) {
			await requestUpdateFourTuple({ id: selectedTuple.id, tuple });
			setSelectedTuple(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Four Tuple

	const [modalDeleteFourTupleOpen, setModalDeleteFourTupleOpen] = useState(false);
	const toggleModalDeleteFourTuple = () => setModalDeleteFourTupleOpen(!modalDeleteFourTupleOpen);

	const { mutateAsync: requestDeleteFourTuple, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteFourTuple(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["four-tuples"] });
			toast.success("Loss scenario deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteFourTuple = async () => {
		if (selectedTuple) {
			await requestDeleteFourTuple(selectedTuple.id);
			toggleModalDeleteFourTuple();
			setSelectedTuple(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Four Tuples

	const {
		data: fourTuples,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["four-tuples", analysisId],
		queryFn: () => getFourTuples(analysisId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || fourTuples === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="Loss Scenarios" onClick={toggleModalCreateFourTuple}>
				<ListWrapper>
					{fourTuples.map(ft => (
						<FourTuple.Root key={ft.id}>
							<FourTuple.Name code={ft.code} name={ft.scenario} />
							<FourTuple.Actions>
								<IconButton
									icon={InfoIcon}
									onClick={() => {
										setSelectedTuple(ft);
										toggleModalFourTupleDetails();
									}}
								/>
								<DualButton
									onEdit={() => {
										setSelectedTuple(ft);
										toggleModalUpdateFourTuple();
									}}
									onDelete={() => {
										setSelectedTuple(ft);
										toggleModalDeleteFourTuple();
									}}
								/>
							</FourTuple.Actions>
						</FourTuple.Root>
					))}
				</ListWrapper>
			</Container>
			<ModalFourTuple
				analysisId={analysisId}
				open={modalCreateFourTupleOpen}
				onClose={toggleModalCreateFourTuple}
				onSubmit={handleCreateFourTuple}
				isLoading={isCreating}
				title="New Scenario"
				btnText="Create"
			/>
			{selectedTuple && (
				<>
					<ModalFourTupleDetails
						fourTuple={selectedTuple}
						open={modalFourTupleDetailsOpen}
						onClose={toggleModalFourTupleDetails}
					/>
					<ModalFourTuple
						analysisId={analysisId}
						open={modalUpdateFourTupleOpen}
						onClose={toggleModalUpdateFourTuple}
						onSubmit={handleUpdateFourTuple}
						isLoading={isUpdating}
						title="Update Scenario"
						btnText="Update"
						fourTuple={selectedTuple || undefined}
					/>
					<ModalConfirm
						open={modalDeleteFourTupleOpen}
						onClose={toggleModalDeleteFourTuple}
						onConfirm={handleDeleteFourTuple}
						isLoading={isDeleting}
						message="Do you want to delete this scenario?"
						title="Delete Scenario"
						btnText="Delete"
					/>
				</>
			)}
		</>
	);
}

export default FourTupleContainer;
