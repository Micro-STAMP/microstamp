import styles from "./ListWrapper.module.css";

interface ListWrapperProps {
	children: React.ReactNode;
}
function ListWrapper({ children }: ListWrapperProps) {
	return (
		<>
			<div className={styles.list_wrapper}>{children}</div>
		</>
	);
}

export default ListWrapper;
