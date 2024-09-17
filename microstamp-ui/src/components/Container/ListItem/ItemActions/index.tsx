import styles from "@components/Container/ListItem/ListItem.module.css";

interface ItemActionsProps {
	children: React.ReactNode;
}
function ItemActions({ children }: ItemActionsProps) {
	return (
		<>
			<div className={styles.item_actions}>{children}</div>
		</>
	);
}

export default ItemActions;
