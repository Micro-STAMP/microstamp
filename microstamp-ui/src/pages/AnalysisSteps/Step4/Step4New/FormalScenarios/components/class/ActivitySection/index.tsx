import { getFormalActivityTitle, IFormalScenariosActivity } from "@interfaces/IStep4New/Enums";
import { useState } from "react";
import { BiChevronUp } from "react-icons/bi";
import styles from "./ActivitySection.module.css";

interface ActivitySectionProps {
	children: React.ReactNode;
	activity: IFormalScenariosActivity;
	defaultCollapsed?: boolean;
}
function ActivitySection({ activity, children, defaultCollapsed = false }: ActivitySectionProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Activity Content Collapse

	const [isCollapsed, setIsCollapsed] = useState(defaultCollapsed);
	const toggleCollapse = () => {
		setIsCollapsed(!isCollapsed);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<div className={styles.activity_section}>
			<div className={styles.header} onClick={toggleCollapse}>
				<span className={styles.title}>{getFormalActivityTitle(activity)}:</span>
				<div className={styles.buttons}>
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

export default ActivitySection;
