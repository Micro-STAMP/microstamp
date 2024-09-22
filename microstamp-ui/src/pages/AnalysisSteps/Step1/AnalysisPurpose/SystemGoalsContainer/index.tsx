import DualButton from "@components/Button/DualButton";
import Container from "@components/Container";
import { ListItem as SystemGoal } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import Loader from "@components/Loader";
import { ModalConfirm } from "@components/Modal";
import { ModalSystemGoal } from "@components/Modal/ModalEntity";
import {
	createSystemGoal,
	deleteSystemGoal,
	getSystemGoals,
	updateSystemGoal
} from "@http/Step1/SystemGoals";
import {
	ISystemGoalFormData,
	ISystemGoalInsertDto,
	ISystemGoalReadDto,
	ISystemGoalUpdateDto
} from "@interfaces/IStep1";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";

interface SystemGoalsContainerProps {
	analysisId: string;
}
function SystemGoalsContainer({ analysisId }: SystemGoalsContainerProps) {
	const queryClient = useQueryClient();
	const [selectedSystemGoal, setSelectedSystemGoal] = useState<ISystemGoalReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create System Goal

	const [modalCreateSystemGoalOpen, setModalCreateSystemGoalOpen] = useState(false);
	const toggleModalCreateSystemGoal = () =>
		setModalCreateSystemGoalOpen(!modalCreateSystemGoalOpen);

	const { mutateAsync: requestCreateSystemGoal, isPending: isCreating } = useMutation({
		mutationFn: (systemGoal: ISystemGoalInsertDto) => createSystemGoal(systemGoal),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-system-goals"] });
			toast.success("System goal created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateSystemGoal = async (systemGoalData: ISystemGoalFormData) => {
		const systemGoal: ISystemGoalInsertDto = {
			...systemGoalData,
			analysisId: analysisId
		};
		await requestCreateSystemGoal(systemGoal);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update System Goal

	const [modalUpdateSystemGoalOpen, setModalUpdateSystemGoalOpen] = useState(false);
	const toggleModalUpdateSystemGoal = () =>
		setModalUpdateSystemGoalOpen(!modalUpdateSystemGoalOpen);

	const { mutateAsync: requestUpdateSystemGoal, isPending: isUpdating } = useMutation({
		mutationFn: ({ id, systemGoal }: { id: string; systemGoal: ISystemGoalUpdateDto }) =>
			updateSystemGoal(id, systemGoal),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-system-goals"] });
			toast.success("System goal updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateSystemGoal = async (systemGoalData: ISystemGoalFormData) => {
		const systemGoal: ISystemGoalUpdateDto = { ...systemGoalData };
		if (selectedSystemGoal) {
			await requestUpdateSystemGoal({ id: selectedSystemGoal.id, systemGoal });
			setSelectedSystemGoal(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete System Goal

	const [modalDeleteSystemGoalOpen, setModalDeleteSystemGoalOpen] = useState(false);
	const toggleModalDeleteSystemGoal = () =>
		setModalDeleteSystemGoalOpen(!modalDeleteSystemGoalOpen);

	const { mutateAsync: requestDeleteSystemGoal, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteSystemGoal(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-system-goals"] });
			toast.success("System goal deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteSystemGoal = async () => {
		if (selectedSystemGoal) {
			await requestDeleteSystemGoal(selectedSystemGoal.id);
			toggleModalDeleteSystemGoal();
			setSelectedSystemGoal(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List System Goals

	const {
		data: systemGoals,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["analysis-system-goals", analysisId],
		queryFn: () => getSystemGoals(analysisId)
	});

	if (isLoading) return <Loader />;
	if (isError || systemGoals === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="System Goals" onClick={toggleModalCreateSystemGoal}>
				<ListWrapper>
					{systemGoals.map(sg => (
						<SystemGoal.Root key={sg.id}>
							<SystemGoal.Name code={sg.code} name={sg.name} />
							<SystemGoal.Actions>
								<DualButton
									onEdit={() => {
										setSelectedSystemGoal(sg);
										toggleModalUpdateSystemGoal();
									}}
									onDelete={() => {
										setSelectedSystemGoal(sg);
										toggleModalDeleteSystemGoal();
									}}
								/>
							</SystemGoal.Actions>
						</SystemGoal.Root>
					))}
				</ListWrapper>
			</Container>
			<ModalSystemGoal
				open={modalCreateSystemGoalOpen}
				onClose={toggleModalCreateSystemGoal}
				onSubmit={handleCreateSystemGoal}
				isLoading={isCreating}
				title="New System Goal"
				btnText="Create"
			/>
			<ModalSystemGoal
				open={modalUpdateSystemGoalOpen}
				onClose={toggleModalUpdateSystemGoal}
				onSubmit={handleUpdateSystemGoal}
				isLoading={isUpdating}
				title="Update System Goal"
				btnText="Update"
				systemGoal={selectedSystemGoal || undefined}
			/>
			<ModalConfirm
				open={modalDeleteSystemGoalOpen}
				onClose={toggleModalDeleteSystemGoal}
				onConfirm={handleDeleteSystemGoal}
				isLoading={isDeleting}
				message="Do you want to delete this system goal?"
				title="Delete System Goal"
				btnText="Delete"
			/>
		</>
	);
}

export default SystemGoalsContainer;
