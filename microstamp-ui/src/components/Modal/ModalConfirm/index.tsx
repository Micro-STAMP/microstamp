import Button from "@components/Button";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { BiCheckDouble as CheckIcon, BiX as ReturnIcon } from "react-icons/bi";
import styles from "./ModalConfirm.module.css";

interface ModalConfirmProps extends ModalProps {
	title: string;
	message: string;
	onConfirm: () => void | Promise<void>;
	isLoading?: boolean;
	btnText?: string;
}
function ModalConfirm({
	open,
	onClose,
	title,
	message,
	onConfirm,
	isLoading = false,
	btnText = "Confirm"
}: ModalConfirmProps) {
	return (
		<ModalContainer open={open} size="small">
			<ModalHeader title={title} onClose={onClose} />
			<div className={styles.modal_confirm}>{message}</div>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button onClick={onConfirm} isLoading={isLoading} size="small" icon={CheckIcon}>
					{btnText}
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalConfirm;
