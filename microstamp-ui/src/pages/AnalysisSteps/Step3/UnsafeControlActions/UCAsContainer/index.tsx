import Loader from "@components/Loader";
import { ModalConfirm, ModalUpdateCode } from "@components/Modal";
import { updateSafetyConstraintCode } from "@http/Step3/SafetyConstraint";
import {
	deleteUnsafeControlAction,
	getUnsafeControlActions,
	updateUnsafeControlActionCode
} from "@http/Step3/UnsafeControlActions";
import { IControlAction } from "@interfaces/IStep2";
import { ISafetyConstraintReadDto, IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";
import UCARow from "./UCARow";
import styles from "./UCAsContainer.module.css";

interface UCAsContainerProps {
	controlAction: IControlAction;
}
function UCAsContainer({ controlAction }: UCAsContainerProps) {
	const queryClient = useQueryClient();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Unsafe Control Actions

	const {
		data: ucas,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["unsafe-control-actions", controlAction.id],
		queryFn: () => getUnsafeControlActions(controlAction.id)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Selected UCA and Associated Constraint

	const [selectedUCA, setSelectedUCA] = useState<IUnsafeControlActionReadDto | null>(null);
	const [selectedConstraint, setSelectedConstraint] = useState<ISafetyConstraintReadDto | null>(
		null
	);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Modals

	// Delete UCA
	const [modalDeleteUCAOpen, setModalDeleteUCAOpen] = useState(false);
	const toggleModalDeleteUCA = () => {
		setModalDeleteUCAOpen(!modalDeleteUCAOpen);
		setSelectedUCA(null);
	};

	// Update UCA Code
	const [modalUpdateUCAOpen, setModalUpdateUCAOpen] = useState(false);
	const toggleModalUpdateUCA = () => {
		setModalUpdateUCAOpen(!modalUpdateUCAOpen);
		setSelectedUCA(null);
	};

	// Update Constraint Code
	const [modalUpdateConstraintOpen, setModalUpdateConstraintOpen] = useState(false);
	const toggleModalUpdateConstraint = () => {
		setModalUpdateConstraintOpen(!modalUpdateConstraintOpen);
		setSelectedConstraint(null);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Unsafe Control Action

	const { mutateAsync: requestDeleteUCA, isPending: isDeleting } = useMutation({
		mutationFn: (ucaId: string) => deleteUnsafeControlAction(ucaId),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["unsafe-control-actions"] });
			queryClient.invalidateQueries({ queryKey: ["ucas-multi-select"] });
			queryClient.invalidateQueries({ queryKey: ["context-table-unsafe-control-actions"] });
			toast.success("Unsafe control action deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteUCA = async (id: string) => {
		await requestDeleteUCA(id);
		toggleModalDeleteUCA();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Unsafe Control Action Code

	const { mutateAsync: requestUpdateUCACode, isPending: isUpdatingUCA } = useMutation({
		mutationFn: ({ ucaId, code }: { ucaId: string; code: string }) =>
			updateUnsafeControlActionCode(ucaId, code),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["unsafe-control-actions"] });
			queryClient.invalidateQueries({ queryKey: ["ucas-multi-select"] });
			queryClient.invalidateQueries({ queryKey: ["context-table-unsafe-control-actions"] });
			toast.success("Unsafe control action code updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateUCACode = async (id: string, code: string) => {
		await requestUpdateUCACode({ ucaId: id, code: code });
		toggleModalUpdateUCA();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Unsafe Constraint Code

	const { mutateAsync: requestUpdateConstraintCode, isPending: isUpdatingConstraint } =
		useMutation({
			mutationFn: ({ code, constraintId }: { constraintId: string; code: string }) =>
				updateSafetyConstraintCode(constraintId, code),
			onSuccess: () => {
				queryClient.invalidateQueries({ queryKey: ["associated-safety-constraint"] });
				toast.success("Constraint code updated.");
			},
			onError: err => {
				toast.error(err.message);
			}
		});
	const handleUpdateConstraintCode = async (id: string, code: string) => {
		await requestUpdateConstraintCode({ code: code, constraintId: id });
		toggleModalUpdateConstraint();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || !ucas) return <h1>Error</h1>;
	return (
		<>
			<div className={styles.ucas_container}>
				<header className={styles.container_header}>
					<span className={styles.column_tag}>Unsafe Control Actions</span>
					<span className={styles.column_tag}>Associated Controller's Constraints</span>
				</header>
				<div className={styles.ucas_list}>
					{ucas.map(uca => (
						<UCARow
							key={uca.id}
							unsafeControlAction={uca}
							onDeleteUCA={() => {
								setSelectedUCA(uca);
								setModalDeleteUCAOpen(true);
							}}
							onUpdateUCA={() => {
								setSelectedUCA(uca);
								setModalUpdateUCAOpen(true);
							}}
							onUpdateConstraint={constraint => {
								setSelectedConstraint(constraint);
								setModalUpdateConstraintOpen(true);
							}}
						/>
					))}
				</div>
			</div>
			{selectedUCA && (
				<>
					<ModalUpdateCode
						open={modalUpdateUCAOpen}
						onClose={toggleModalUpdateUCA}
						onSubmit={code => handleUpdateUCACode(selectedUCA.id, code)}
						title="Update Unsafe Control Action Code"
						isLoading={isUpdatingUCA}
						code={selectedUCA.uca_code}
					/>
					{!selectedUCA.rule_code && (
						<ModalConfirm
							open={modalDeleteUCAOpen}
							onClose={toggleModalDeleteUCA}
							onConfirm={() => handleDeleteUCA(selectedUCA.id)}
							isLoading={isDeleting}
							message="Do you want to delete this unsafe control action?"
							title="Delete Unsafe Control Action"
							btnText="Delete"
						/>
					)}
				</>
			)}
			{selectedConstraint && (
				<ModalUpdateCode
					open={modalUpdateConstraintOpen}
					onClose={toggleModalUpdateConstraint}
					onSubmit={code => handleUpdateConstraintCode(selectedConstraint.id, code)}
					title="Update Constraint Code"
					isLoading={isUpdatingConstraint}
					code={selectedConstraint.safety_constraint_code}
				/>
			)}
		</>
	);
}

export default UCAsContainer;
