import { BiPlusMedical as PlusIcon } from "react-icons/bi";
import styles from "./Container.module.css";

interface ContainerProps {
	title: string;
	children: React.ReactNode;
	justTitle?: boolean;
	onClick?: () => void;
}
function Container({ title, justTitle = false, onClick, children }: ContainerProps) {
	return (
		<div className={styles.container}>
			<header className={styles.header}>
				<span className={styles.title}>{title}</span>
				{!justTitle && (
					<button type="button" className={styles.button} onClick={onClick}>
						<PlusIcon className={styles.icon} />
					</button>
				)}
			</header>
			<div className={styles.content}>{children}</div>
		</div>
	);
}

export default Container;
