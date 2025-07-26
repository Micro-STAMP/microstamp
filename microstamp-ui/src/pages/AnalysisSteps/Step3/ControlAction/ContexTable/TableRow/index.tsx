import {
	IContext,
	INotUnsafeContextReadDto,
	IUnsafeControlActionReadDto
} from "@interfaces/IStep3";
import { IUCAType, ucaTypeSelectOptions } from "@interfaces/IStep3/IUnsafeControlAction/Enums";
import styles from "@pages/AnalysisSteps/Step3/ControlAction/ContexTable/ContextTable.module.css";
import UnsafeButton from "./UnsafeButton";

interface TableRowProps {
	context: IContext;
	unsafeControlActions: IUnsafeControlActionReadDto[];
	notUnsafeContexts: INotUnsafeContextReadDto[];
	toggleModal: (context: IContext, type: IUCAType) => void;
	deleteNotUnsafeContext: (id: string) => Promise<void>;
}
function TableRow({
	context,
	unsafeControlActions,
	notUnsafeContexts,
	toggleModal,
	deleteNotUnsafeContext
}: TableRowProps) {
	return (
		<div className={styles.table_row}>
			{context.states.map(state => (
				<div key={state.id} className={styles.table_cell}>
					{state.name}
				</div>
			))}
			{ucaTypeSelectOptions.map(type => (
				<div key={type.value} className={styles.table_cell}>
					<UnsafeButton
						context={context}
						type={type.value as IUCAType}
						unsafeControlActions={unsafeControlActions}
						notUnsafeContexts={notUnsafeContexts}
						toggleModal={toggleModal}
						deleteNotUnsafeContext={deleteNotUnsafeContext}
					/>
				</div>
			))}
		</div>
	);
}

export default TableRow;
