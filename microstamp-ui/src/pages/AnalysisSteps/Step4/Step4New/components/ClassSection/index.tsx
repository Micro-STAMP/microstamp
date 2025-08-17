import { useState } from "react";
import { BiChevronUp, BiSolidCircle } from "react-icons/bi";
import styles from "./ClassSection.module.css";

interface ClassSectionProps {
	title: string;
	children: React.ReactNode;
	defaultCollapsed?: boolean;
}

function ClassSection({ title, children, defaultCollapsed = false }: ClassSectionProps) {
	const [isCollapsed, setIsCollapsed] = useState(defaultCollapsed);

	const toggleCollapse = () => {
		setIsCollapsed(!isCollapsed);
	};

	return (
		<div className={styles.class_section}>
			<div className={styles.header} onClick={toggleCollapse}>
				<BiSolidCircle className={styles.circle_icon} />
				<span className={styles.title}>{title}:</span>
				<BiChevronUp
					className={`${styles.chevron_icon} ${isCollapsed ? styles.collapsed : ""}`}
				/>
			</div>
			{!isCollapsed && <div className={styles.content}>{children}</div>}
		</div>
	);
}

export default ClassSection;
