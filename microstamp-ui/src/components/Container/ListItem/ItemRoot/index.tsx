import styles from "@components/Container/ListItem/ListItem.module.css";

interface ItemRootProps {
	children: React.ReactNode;
}
function ItemRoot({ children }: ItemRootProps) {
	return <div className={styles.list_item}>{children}</div>;
}

export default ItemRoot;
