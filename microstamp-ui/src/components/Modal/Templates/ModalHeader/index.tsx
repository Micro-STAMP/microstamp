import styles from "@components/Modal/Templates/Modal.module.css";
import { BiX as CloseIcon } from "react-icons/bi";

interface ModalHeaderProps {
	title: string;
	onClose: () => void;
}
function ModalHeader({ title, onClose }: ModalHeaderProps) {
	return (
		<header className={styles.modal_header}>
			<span className={styles.modal_title}>{title}</span>
			<button type="button" className={styles.modal_close_button} onClick={onClose}>
				<CloseIcon />
			</button>
		</header>
	);
}

export default ModalHeader;
