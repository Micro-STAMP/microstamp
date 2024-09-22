import { IVariableReadDto } from "@interfaces/IStep2";
import { IUCATypesArray } from "@interfaces/IStep3/IUnsafeControlAction/Enums";
import styles from "@pages/AnalysisSteps/Step3/ControlAction/ContexTable/ContextTable.module.css";

interface TableHeaderProps {
	variables: IVariableReadDto[];
}
function TableHeader({ variables }: TableHeaderProps) {
	return (
		<div className={styles.table_header}>
			{variables.map(v => (
				<div key={v.id} className={styles.table_cell}>
					{v.name}
				</div>
			))}
			{IUCATypesArray.map(type => (
				<div key={type} className={styles.table_cell}>
					{type}
				</div>
			))}
		</div>
	);
}

export default TableHeader;
