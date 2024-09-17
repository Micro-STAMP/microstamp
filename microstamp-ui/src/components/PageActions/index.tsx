import styles from "./PageActions.module.css";

interface PageActionsProps {
	children: React.ReactNode;
}
function PageActions({ children }: PageActionsProps) {
	return <div className={styles.page_actions}>{children}</div>;
}

export default PageActions;
