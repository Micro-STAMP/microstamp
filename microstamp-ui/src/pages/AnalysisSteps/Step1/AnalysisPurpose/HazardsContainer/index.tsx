import DualButton from "@components/Button/DualButton";
import Container from "@components/Container";
import { ListItem as Hazard } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import Loader from "@components/Loader";
import { ModalConfirm } from "@components/Modal";
import { ModalHazard } from "@components/Modal/ModalEntity";
import { createHazard, deleteHazard, getHazards, updateHazard } from "@http/Step1/Hazards";
import {
	IHazardFormData,
	IHazardInsertDto,
	IHazardReadDto,
	IHazardUpdateDto
} from "@interfaces/IStep1";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";

interface HazardsContainerProps {
	analysisId: string;
}
function HazardsContainer({ analysisId }: HazardsContainerProps) {
	const queryClient = useQueryClient();
	const [selectedHazard, setSelectedHazard] = useState<IHazardReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Hazard

	const [modalCreateHazardOpen, setModalCreateHazardOpen] = useState(false);
	const toggleModalCreateHazard = () => setModalCreateHazardOpen(!modalCreateHazardOpen);

	const { mutateAsync: requestCreateHazard, isPending: isCreating } = useMutation({
		mutationFn: (hazard: IHazardInsertDto) => createHazard(hazard),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-hazards"] });
			queryClient.invalidateQueries({ queryKey: ["hazards-multi-select"] });
			queryClient.invalidateQueries({ queryKey: ["hazards-select-options"] });
			toast.success("Hazard created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateHazard = async (hazardData: IHazardFormData) => {
		const hazard: IHazardInsertDto = {
			name: hazardData.name,
			code: hazardData.code,
			lossIds: hazardData.losses.map(l => l.value),
			fatherId: hazardData.father ? hazardData.father.value : null,
			analysisId: analysisId
		};
		await requestCreateHazard(hazard);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Hazard

	const [modalUpdateHazardOpen, setModalUpdateHazardOpen] = useState(false);
	const toggleModalUpdateHazard = () => setModalUpdateHazardOpen(!modalUpdateHazardOpen);

	const { mutateAsync: requestUpdateHazard, isPending: isUpdating } = useMutation({
		mutationFn: ({ id, hazard }: { id: string; hazard: IHazardUpdateDto }) =>
			updateHazard(id, hazard),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-hazards"] });
			queryClient.invalidateQueries({ queryKey: ["hazards-multi-select"] });
			queryClient.invalidateQueries({ queryKey: ["hazards-select-options"] });
			toast.success("Hazard updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateHazard = async (hazardData: IHazardFormData) => {
		const hazard: IHazardUpdateDto = {
			name: hazardData.name,
			code: hazardData.code,
			lossIds: hazardData.losses.map(l => l.value),
			fatherId: hazardData.father ? hazardData.father.value : null
		};
		if (selectedHazard) {
			await requestUpdateHazard({ id: selectedHazard.id, hazard });
			setSelectedHazard(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Hazard

	const [modalDeleteHazardOpen, setModalDeleteHazardOpen] = useState(false);
	const toggleModalDeleteHazard = () => setModalDeleteHazardOpen(!modalDeleteHazardOpen);

	const { mutateAsync: requestDeleteHazard, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteHazard(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-hazards"] });
			queryClient.invalidateQueries({ queryKey: ["hazards-multi-select"] });
			queryClient.invalidateQueries({ queryKey: ["hazards-select-options"] });
			toast.success("Hazard deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteHazard = async () => {
		if (selectedHazard) {
			await requestDeleteHazard(selectedHazard.id);
			toggleModalDeleteHazard();
			setSelectedHazard(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Hazards

	const {
		data: hazards,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["analysis-hazards", analysisId],
		queryFn: () => getHazards(analysisId)
	});

	if (isLoading) return <Loader />;
	if (isError || hazards === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="Hazards" onClick={toggleModalCreateHazard}>
				<ListWrapper>
					{hazards.map(hazard => (
						<Hazard.Root key={hazard.id}>
							<Hazard.Name
								code={hazard.code}
								name={hazard.name}
								dependencies={
									hazard.father
										? [hazard.father.code].concat(
												hazard.losses.map(l => l.code)
										  )
										: hazard.losses.map(l => l.code)
								}
							/>
							<Hazard.Actions>
								<DualButton
									onEdit={() => {
										setSelectedHazard(hazard);
										toggleModalUpdateHazard();
									}}
									onDelete={() => {
										setSelectedHazard(hazard);
										toggleModalDeleteHazard();
									}}
								/>
							</Hazard.Actions>
						</Hazard.Root>
					))}
				</ListWrapper>
			</Container>
			<ModalHazard
				open={modalCreateHazardOpen}
				onClose={toggleModalCreateHazard}
				onSubmit={handleCreateHazard}
				isLoading={isCreating}
				title="New Hazard"
				btnText="Create"
				analysisId={analysisId}
			/>
			<ModalHazard
				open={modalUpdateHazardOpen}
				onClose={toggleModalUpdateHazard}
				onSubmit={handleUpdateHazard}
				isLoading={isUpdating}
				title="Update Hazard"
				btnText="Update"
				hazard={selectedHazard || undefined}
				analysisId={analysisId}
			/>
			<ModalConfirm
				open={modalDeleteHazardOpen}
				onClose={toggleModalDeleteHazard}
				onConfirm={handleDeleteHazard}
				isLoading={isDeleting}
				message="Do you want to delete this hazard?"
				title="Delete Hazard"
				btnText="Delete"
			/>
		</>
	);
}

export default HazardsContainer;
