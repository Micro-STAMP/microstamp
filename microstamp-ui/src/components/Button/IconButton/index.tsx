import { IconType } from "react-icons";
import { Link } from "react-router-dom";
import styles from "./IconButton.module.css";

interface IconButtonProps {
	onClick?: () => void;
	to?: string;
	icon: IconType;
}
function IconButton({ onClick, to, icon: Icon }: IconButtonProps) {
	if (to) {
		<Link to={to}>
			<button type="button" onClick={onClick} className={styles.icon_button}>
				<Icon />
			</button>
		</Link>;
	}
	return (
		<button type="button" onClick={onClick} className={styles.icon_button}>
			<Icon />
		</button>
	);
}

export default IconButton;
