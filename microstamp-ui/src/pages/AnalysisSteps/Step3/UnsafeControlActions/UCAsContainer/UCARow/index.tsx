import IconButton from "@components/Button/IconButton";
import Loader from "@components/Loader";
import { getSafetyConstraint } from "@http/Step3/SafetyConstraint";
import { ISafetyConstraintReadDto, IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { useQuery } from "@tanstack/react-query";
import { BiSolidTrash as DeleteIcon, BiEditAlt as EditIcon } from "react-icons/bi";
import styles from "./UCARow.module.css";

interface UCARowProps {
	unsafeControlAction: IUnsafeControlActionReadDto;
	onUpdateUCA: () => void;
	onDeleteUCA: () => void;
	onUpdateConstraint: (constraint: ISafetyConstraintReadDto) => void;
}
function UCARow({
	unsafeControlAction,
	onDeleteUCA,
	onUpdateConstraint,
	onUpdateUCA
}: UCARowProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Associated Controller Constraint

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
		<div className={styles.uca_row}>
			<div className={styles.uca}>
				<div className={styles.uca_name}>
					<span className={styles.uca_code}>{`[${unsafeControlAction.uca_code}]`}</span>{" "}
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
				<IconButton icon={EditIcon} onClick={onUpdateUCA} />
				{!unsafeControlAction.rule_code && (
					<IconButton icon={DeleteIcon} onClick={onDeleteUCA} />
				)}
			</div>
			<div className={styles.safety_constraint}>
				<div className={styles.constraint_name}>
					<span
						className={styles.sc_code}
					>{`[${constraint.safety_constraint_code}]`}</span>{" "}
					{constraint.name}
				</div>
				<IconButton icon={EditIcon} onClick={() => onUpdateConstraint(constraint)} />
			</div>
		</div>
	);
}

export default UCARow;
