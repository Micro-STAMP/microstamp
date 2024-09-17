import { Overlay } from "@components/Modal/Templates";
import styles from "@components/Modal/Templates/Modal.module.css";

interface ModalContainerProps {
	children: React.ReactNode;
	open: boolean;
	size?: "small" | "normal" | "big" | "large";
}
function ModalContainer({ children, open, size = "normal" }: ModalContainerProps) {
	const modal_class = `${styles.modal_container} ${styles[size]}`;
	return (
		<>
			{open && (
				<Overlay>
					<dialog className={modal_class} open={open}>
						{children}
					</dialog>
				</Overlay>
			)}
		</>
	);
}

export default ModalContainer;
