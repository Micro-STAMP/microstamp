import IconButton from "@components/Button/IconButton";
import Loader from "@components/Loader";
import { ModalConfirm } from "@components/Modal";
import { getSafetyConstraint } from "@http/Step3/SafetyConstraint";
import { deleteUnsafeControlAction } from "@http/Step3/UnsafeControlActions";
import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { BiSolidTrash as DeleteIcon } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./UCARow.module.css";

interface UCARowProps {
	unsafeControlAction: IUnsafeControlActionReadDto;
}
function UCARow({ unsafeControlAction }: UCARowProps) {
	const queryClient = useQueryClient();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Unsafe Control Action

	const [modalDeleteUCAOpen, setModalDeleteUCAOpen] = useState(false);
	const toggleModalDeleteUCA = () => setModalDeleteUCAOpen(!modalDeleteUCAOpen);

	const { mutateAsync: requestDeleteUCA, isPending: isDeleting } = useMutation({
		mutationFn: () => deleteUnsafeControlAction(unsafeControlAction.id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["unsafe-control-actions"] });
			queryClient.invalidateQueries({ queryKey: ["context-table-unsafe-control-actions"] });
			toast.success("Unsafe control action deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteUCA = async () => {
		await requestDeleteUCA();
		toggleModalDeleteUCA();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Associated Safety Constraint

	const {
		data: constraint,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["associated-safety-constraint", unsafeControlAction.id],
		queryFn: () => getSafetyConstraint(unsafeControlAction.id)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || !constraint) return <h1>Error</h1>;
	return (
		<>
			<div className={styles.uca_row}>
				<div className={styles.uca}>
					<div className={styles.uca_name}>
						{unsafeControlAction.name}{" "}
						<div className={styles.dependencies}>
							<span className={styles.dependencie}>
								{unsafeControlAction.hazard_code}
							</span>
							{unsafeControlAction.rule_code && (
								<span className={styles.dependencie}>
									{unsafeControlAction.rule_code}
								</span>
							)}
						</div>
					</div>
					{!unsafeControlAction.rule_code && (
						<IconButton icon={DeleteIcon} onClick={toggleModalDeleteUCA} />
					)}
				</div>
				<div className={styles.safety_constraint}>
					<div>{constraint.name}</div>
				</div>
			</div>
			{!unsafeControlAction.rule_code && (
				<ModalConfirm
					open={modalDeleteUCAOpen}
					onClose={toggleModalDeleteUCA}
					onConfirm={handleDeleteUCA}
					isLoading={isDeleting}
					message="Do you want to delete this unsafe control action?"
					title="Delete Unsafe Control Action"
					btnText="Delete"
				/>
			)}
		</>
	);
}

export default UCARow;
