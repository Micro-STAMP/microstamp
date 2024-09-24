import { ModalInputs } from "@components/Modal/Templates";
import styles from "./ContextStates.module.css";

interface ContextStatesProps {
	children: React.ReactNode;
}
function ContextStates({ children }: ContextStatesProps) {
	return (
		<>
			<div className={styles.context_states_container}>
				<span className={styles.context_title}>Context:</span>
				<ModalInputs column="double">{children}</ModalInputs>
			</div>
		</>
	);
}

export default ContextStates;
