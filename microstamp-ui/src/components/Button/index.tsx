import { IconType } from "react-icons";
import styles from "./Button.module.css";

type ButtonVariant = "main" | "dark";
type ButtonSize = "normal" | "small";
type ButtonType = "submit" | "reset" | "button" | undefined;
interface ButtonProps {
	children: React.ReactNode;
	type?: ButtonType;
	variant?: ButtonVariant;
	size?: ButtonSize;
	onClick?: () => void;
	icon?: IconType;
	iconPosition?: "left" | "right";
	isLoading?: boolean;
	disabled?: boolean;
}
function Button({
	onClick,
	children,
	variant = "main",
	size = "normal",
	icon: Icon,
	iconPosition = "left",
	isLoading = false,
	type = "button",
	disabled = false
}: ButtonProps) {
	const buttonClass = `${styles.button} ${styles[variant]} ${styles[size]} ${
		isLoading && styles.isLoading
	}`;
	return (
		<>
			<button
				type={type}
				className={buttonClass}
				onClick={onClick}
				disabled={isLoading || disabled}
			>
				{iconPosition === "left" && Icon && <Icon className={styles.icon} />}
				{!isLoading ? children : "Loading..."}
				{iconPosition === "right" && Icon && <Icon className={styles.icon} />}
			</button>
		</>
	);
}

export default Button;
