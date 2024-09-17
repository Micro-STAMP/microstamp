import { ModalContainer } from "@components/Modal/Templates";
import styles from "./ModalLoading.module.css";

interface ModalLoadingProps {
	open: boolean;
	message?: string;
}
function ModalLoading({ open, message = "Loading" }: ModalLoadingProps) {
	return (
		<ModalContainer open={open} size="small">
			<div className={styles.loader_wrapper}>
				<div className={styles.spinner}></div>
				<span className={styles.message}>{message}</span>
			</div>
		</ModalContainer>
	);
}

export default ModalLoading;
