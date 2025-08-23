import { useState } from "react";
import { BiChevronUp, BiImageAlt } from "react-icons/bi";
import styles from "./ClassSection.module.css";

interface ClassSectionProps {
	title: string;
	children: React.ReactNode;
	defaultCollapsed?: boolean;
	onViewClassImage?: () => void;
}

function ClassSection({
	title,
	children,
	defaultCollapsed = false,
	onViewClassImage
}: ClassSectionProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Class Content Collapse

	const [isCollapsed, setIsCollapsed] = useState(defaultCollapsed);
	const toggleCollapse = () => {
		setIsCollapsed(!isCollapsed);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle View Class Image

	const handleOpenClassImage = (e: React.MouseEvent) => {
		e.stopPropagation();
		onViewClassImage?.();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<div className={styles.class_section}>
			<div className={styles.header} onClick={toggleCollapse}>
				<span className={styles.title}>{title}:</span>
				<div className={styles.buttons}>
					{onViewClassImage && (
						<button
							type="button"
							onClick={handleOpenClassImage}
							title="View Class Reference Image"
						>
							<BiImageAlt className={styles.image_icon} />
						</button>
					)}
					<button type="button" onClick={toggleCollapse}>
						<BiChevronUp
							className={`${styles.chevron_icon} ${
								isCollapsed ? styles.collapsed : ""
							}`}
						/>
					</button>
				</div>
			</div>
			{!isCollapsed && <div className={styles.content}>{children}</div>}
		</div>
	);
}

export default ClassSection;
