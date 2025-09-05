import IconButton from "@components/Button/IconButton";
import { ISafetyConstraintReadDto, IUnsafeControlActionReadDto } from "@interfaces/IStep3";
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
	// * Associated Constraint

	const associatedConstraint: ISafetyConstraintReadDto = {
		id: unsafeControlAction.constraint_id,
		name: unsafeControlAction.constraintName,
		safety_constraint_code: unsafeControlAction.constraint_code,
		uca_id: unsafeControlAction.id
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

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
					>{`[${associatedConstraint.safety_constraint_code}]`}</span>{" "}
					{associatedConstraint.name}
				</div>
				<IconButton
					icon={EditIcon}
					onClick={() => onUpdateConstraint(associatedConstraint)}
				/>
			</div>
		</div>
	);
}

export default UCARow;
