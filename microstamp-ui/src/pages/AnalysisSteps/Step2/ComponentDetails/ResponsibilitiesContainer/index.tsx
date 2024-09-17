import DualButton from "@components/Button/DualButton";
import Container from "@components/Container";
import { ListItem as Responsibility } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalConfirm } from "@components/Modal";
import { ModalResponsibility } from "@components/Modal/ModalEntity";
import {
	createResponsibility,
	deleteResponsibility,
	getResponsibilities,
	updateResponsibility
} from "@http/Step2/Responsibilities";
import {
	IResponsibilityFormData,
	IResponsibilityInsertDto,
	IResponsibilityReadDto,
	IResponsibilityUpdateDto
} from "@interfaces/IStep2";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";

interface ResponsibilitiesContainerProps {
	analysisId: string;
	componentId: string;
}
function ResponsibilitiesContainer({ analysisId, componentId }: ResponsibilitiesContainerProps) {
	const queryClient = useQueryClient();
	const [selectedResponsibility, setSelectedResponsibility] =
		useState<IResponsibilityReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Responsibility

	const [modalCreateResponsibilityOpen, setModalCreateResponsibilityOpen] = useState(false);
	const toggleModalCreateResponsibility = () =>
		setModalCreateResponsibilityOpen(!modalCreateResponsibilityOpen);

	const { mutateAsync: requestCreateResponsibility, isPending: isCreating } = useMutation({
		mutationFn: (responsibility: IResponsibilityInsertDto) =>
			createResponsibility(responsibility),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["component-responsibilities"] });
			toast.success("Responsibility created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateResponsibility = async (responsibilityData: IResponsibilityFormData) => {
		const responsibility: IResponsibilityInsertDto = {
			responsibility: responsibilityData.responsibility,
			code: responsibilityData.code,
			systemSafetyConstraintId: responsibilityData.systemSafetyConstraint!.value,
			componentId: componentId
		};
		await requestCreateResponsibility(responsibility);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Responsibility

	const [modalUpdateResponsibilityOpen, setModalUpdateResponsibilityOpen] = useState(false);
	const toggleModalUpdateResponsibility = () =>
		setModalUpdateResponsibilityOpen(!modalUpdateResponsibilityOpen);

	const { mutateAsync: requestUpdateResponsibility, isPending: isUpdating } = useMutation({
		mutationFn: ({
			id,
			responsibility
		}: {
			id: string;
			responsibility: IResponsibilityUpdateDto;
		}) => updateResponsibility(id, responsibility),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["component-responsibilities"] });
			toast.success("Responsibility updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateResponsibility = async (responsibilityData: IResponsibilityFormData) => {
		const responsibility: IResponsibilityUpdateDto = {
			responsibility: responsibilityData.responsibility,
			code: responsibilityData.code,
			systemSafetyConstraintId: responsibilityData.systemSafetyConstraint!.value
		};
		if (selectedResponsibility) {
			await requestUpdateResponsibility({ id: selectedResponsibility.id, responsibility });
			setSelectedResponsibility(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Responsibility

	const [modalDeleteResponsibilityOpen, setModalDeleteResponsibilityOpen] = useState(false);
	const toggleModalDeleteResponsibility = () =>
		setModalDeleteResponsibilityOpen(!modalDeleteResponsibilityOpen);

	const { mutateAsync: requestDeleteResponsibility, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteResponsibility(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["component-responsibilities"] });
			toast.success("Responsibility deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteResponsibility = async () => {
		if (selectedResponsibility) {
			await requestDeleteResponsibility(selectedResponsibility.id);
			toggleModalDeleteResponsibility();
			setSelectedResponsibility(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Responsibility

	const {
		data: responsibilities,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["component-responsibilities", componentId],
		queryFn: () => getResponsibilities(componentId)
	});

	if (isLoading) return;
	if (isError || responsibilities === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="Responsibilities" onClick={toggleModalCreateResponsibility}>
				<ListWrapper>
					{responsibilities.map(responsibility => (
						<Responsibility.Root key={responsibility.id}>
							<Responsibility.Name
								code={responsibility.code}
								name={responsibility.responsibility}
								dependencies={
									responsibility.systemSafetyConstraint
										? [responsibility.systemSafetyConstraint.code]
										: []
								}
							/>
							<Responsibility.Actions>
								<DualButton
									onEdit={() => {
										setSelectedResponsibility(responsibility);
										toggleModalUpdateResponsibility();
									}}
									onDelete={() => {
										setSelectedResponsibility(responsibility);
										toggleModalDeleteResponsibility();
									}}
								/>
							</Responsibility.Actions>
						</Responsibility.Root>
					))}
				</ListWrapper>
			</Container>
			<ModalResponsibility
				analysisId={analysisId}
				open={modalCreateResponsibilityOpen}
				onClose={toggleModalCreateResponsibility}
				onSubmit={handleCreateResponsibility}
				isLoading={isCreating}
				title="New Responsibility"
				btnText="Create"
			/>
			<ModalResponsibility
				analysisId={analysisId}
				open={modalUpdateResponsibilityOpen}
				onClose={toggleModalUpdateResponsibility}
				onSubmit={handleUpdateResponsibility}
				isLoading={isUpdating}
				title="Update Responsibility"
				btnText="Update"
				responsibility={selectedResponsibility || undefined}
			/>
			<ModalConfirm
				open={modalDeleteResponsibilityOpen}
				onClose={toggleModalDeleteResponsibility}
				onConfirm={handleDeleteResponsibility}
				isLoading={isDeleting}
				message="Do you want to delete this responsibility?"
				title="Delete Responsibility"
				btnText="Delete"
			/>
		</>
	);
}

export default ResponsibilitiesContainer;
