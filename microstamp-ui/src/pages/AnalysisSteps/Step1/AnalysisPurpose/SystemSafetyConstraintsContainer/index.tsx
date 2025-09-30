import DualButton from "@components/Button/DualButton";
import Container from "@components/Container";
import { ListItem as SystemSafetyConstraint } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalConfirm } from "@components/Modal";
import { ModalSystemSafetyConstraint } from "@components/Modal/ModalEntity";
import {
	createSystemSafetyConstraint,
	deleteSystemSafetyConstraint,
	getSystemSafetyConstraints,
	updateSystemSafetyConstraint
} from "@http/Step1/SystemSafetyConstraints";
import {
	ISystemSafetyConstraintFormData,
	ISystemSafetyConstraintInsertDto,
	ISystemSafetyConstraintReadDto,
	ISystemSafetyConstraintUpdateDto
} from "@interfaces/IStep1";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";

interface SystemSafetyConstraintsContainerProps {
	analysisId: string;
}
function SystemSafetyConstraintsContainer({ analysisId }: SystemSafetyConstraintsContainerProps) {
	const queryClient = useQueryClient();
	const [selectedSystemSafetyConstraint, setSelectedSystemSafetyConstraint] =
		useState<ISystemSafetyConstraintReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create System Safety Constraint

	const [modalCreateSystemSafetyConstraintOpen, setModalCreateSystemSafetyConstraintOpen] =
		useState(false);
	const toggleModalCreateSystemSafetyConstraint = () =>
		setModalCreateSystemSafetyConstraintOpen(!modalCreateSystemSafetyConstraintOpen);

	const { mutateAsync: requestCreateSystemSafetyConstraint, isPending: isCreating } = useMutation(
		{
			mutationFn: (ssc: ISystemSafetyConstraintInsertDto) =>
				createSystemSafetyConstraint(ssc),
			onSuccess: () => {
				queryClient.invalidateQueries({ queryKey: ["analysis-system-safety-constraints"] });
				toast.success("System safety constraint created.");
			},
			onError: err => {
				toast.error(err.message);
			}
		}
	);
	const handleCreateSystemSafetyConstraint = async (sscData: ISystemSafetyConstraintFormData) => {
		const ssc: ISystemSafetyConstraintInsertDto = {
			name: sscData.name,
			code: sscData.code,
			hazardsId: sscData.hazards.map(h => h.value),
			analysisId: analysisId
		};
		await requestCreateSystemSafetyConstraint(ssc);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update System Safety Constraint

	const [modalUpdateSystemSafetyConstraintOpen, setModalUpdateSystemSafetyConstraintOpen] =
		useState(false);
	const toggleModalUpdateSystemSafetyConstraint = () =>
		setModalUpdateSystemSafetyConstraintOpen(!modalUpdateSystemSafetyConstraintOpen);

	const { mutateAsync: requestUpdateSystemSafetyConstraint, isPending: isUpdating } = useMutation(
		{
			mutationFn: ({ id, ssc }: { id: string; ssc: ISystemSafetyConstraintUpdateDto }) =>
				updateSystemSafetyConstraint(id, ssc),
			onSuccess: () => {
				queryClient.invalidateQueries({ queryKey: ["analysis-system-safety-constraints"] });
				toast.success("System safety constraint updated.");
			},
			onError: err => {
				toast.error(err.message);
			}
		}
	);
	const handleUpdateSystemSafetyConstraint = async (sscData: ISystemSafetyConstraintFormData) => {
		const ssc: ISystemSafetyConstraintUpdateDto = {
			name: sscData.name,
			code: sscData.code,
			hazardsId: sscData.hazards.map(h => h.value)
		};
		if (selectedSystemSafetyConstraint) {
			await requestUpdateSystemSafetyConstraint({
				id: selectedSystemSafetyConstraint.id,
				ssc
			});
			setSelectedSystemSafetyConstraint(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete System Safety Constraint

	const [modalDeleteSystemSafetyConstraintOpen, setModalDeleteSystemSafetyConstraintOpen] =
		useState(false);
	const toggleModalDeleteSystemSafetyConstraint = () =>
		setModalDeleteSystemSafetyConstraintOpen(!modalDeleteSystemSafetyConstraintOpen);

	const { mutateAsync: requestDeleteSystemSafetyConstraint, isPending: isDeleting } = useMutation(
		{
			mutationFn: (id: string) => deleteSystemSafetyConstraint(id),
			onSuccess: () => {
				queryClient.invalidateQueries({ queryKey: ["analysis-system-safety-constraints"] });
				toast.success("System safety constraint deleted.");
			},
			onError: err => {
				toast.error(err.message);
			}
		}
	);
	const handleDeleteSystemSafetyConstraint = async () => {
		if (selectedSystemSafetyConstraint) {
			await requestDeleteSystemSafetyConstraint(selectedSystemSafetyConstraint.id);
			toggleModalDeleteSystemSafetyConstraint();
			setSelectedSystemSafetyConstraint(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List System Safety Constraints

	const {
		data: systemSafetyConstraints,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["analysis-system-safety-constraints", analysisId],
		queryFn: () => getSystemSafetyConstraints(analysisId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<Container
				title="System Safety Constraints"
				onClick={toggleModalCreateSystemSafetyConstraint}
				isLoading={isLoading}
				isError={isError || systemSafetyConstraints === undefined}
			>
				<ListWrapper>
					{systemSafetyConstraints &&
						systemSafetyConstraints.map(ssc => (
							<SystemSafetyConstraint.Root key={ssc.id}>
								<SystemSafetyConstraint.Name
									code={ssc.code}
									name={ssc.name}
									dependencies={ssc.hazards.map(h => h.code)}
								/>
								<SystemSafetyConstraint.Actions>
									<DualButton
										onEdit={() => {
											setSelectedSystemSafetyConstraint(ssc);
											toggleModalUpdateSystemSafetyConstraint();
										}}
										onDelete={() => {
											setSelectedSystemSafetyConstraint(ssc);
											toggleModalDeleteSystemSafetyConstraint();
										}}
									/>
								</SystemSafetyConstraint.Actions>
							</SystemSafetyConstraint.Root>
						))}
				</ListWrapper>
			</Container>
			<ModalSystemSafetyConstraint
				open={modalCreateSystemSafetyConstraintOpen}
				onClose={toggleModalCreateSystemSafetyConstraint}
				onSubmit={handleCreateSystemSafetyConstraint}
				isLoading={isCreating}
				title="New System Safety Constraint"
				btnText="Create"
				analysisId={analysisId}
			/>
			<ModalSystemSafetyConstraint
				open={modalUpdateSystemSafetyConstraintOpen}
				onClose={toggleModalUpdateSystemSafetyConstraint}
				onSubmit={handleUpdateSystemSafetyConstraint}
				isLoading={isUpdating}
				title="Update System Safety Constraint"
				btnText="Update"
				systemSafetyConstraint={selectedSystemSafetyConstraint || undefined}
				analysisId={analysisId}
			/>
			<ModalConfirm
				open={modalDeleteSystemSafetyConstraintOpen}
				onClose={toggleModalDeleteSystemSafetyConstraint}
				onConfirm={handleDeleteSystemSafetyConstraint}
				isLoading={isDeleting}
				message="Do you want to delete this system safety constraint?"
				title="Delete System Safety Constraint"
				btnText="Delete"
			/>
		</>
	);
}

export default SystemSafetyConstraintsContainer;
