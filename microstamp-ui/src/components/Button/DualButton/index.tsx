import IconButton from "@components/Button/IconButton";
import { BiSolidTrash as DeleteIcon, BiEditAlt as EditIcon } from "react-icons/bi";
import styles from "./DualButton.module.css";

interface DualButtonProps {
	onEdit: () => void;
	onDelete: () => void;
}
function DualButton({ onEdit, onDelete }: DualButtonProps) {
	return (
		<div className={styles.dual_button_container}>
			<IconButton onClick={onEdit} icon={EditIcon} />
			<IconButton onClick={onDelete} icon={DeleteIcon} />
		</div>
	);
}

export default DualButton;
