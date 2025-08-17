import styles from "./ModalInputs.module.css";

interface ModalInputsProps {
	children: React.ReactNode;
	column?: "single" | "double" | "triple";
}
function ModalInputs({ children, column = "single" }: ModalInputsProps) {
	const wrapper_class = `${styles.modal_inputs_wrapper} ${styles[column]}`;
	return <div className={wrapper_class}>{children}</div>;
}

export default ModalInputs;
