import styles from "@components/Modal/Templates/Modal.module.css";

interface ModalButtonsProps {
	children: React.ReactNode;
}
function ModalButtons({ children }: ModalButtonsProps) {
	return (
		<>
			<div className={styles.modal_buttons}>{children}</div>
		</>
	);
}

export default ModalButtons;
