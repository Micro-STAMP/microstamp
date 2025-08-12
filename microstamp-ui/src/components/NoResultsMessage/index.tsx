import { MdSearchOff as NoResulIcon } from "react-icons/md";
import styles from "./NoResultsMessage.module.css";

function NoResultsMessage({ message }: { message: string }) {
	return (
		<div className={styles.no_result_message}>
			<NoResulIcon />
			<span>{message}</span>
		</div>
	);
}

export default NoResultsMessage;
