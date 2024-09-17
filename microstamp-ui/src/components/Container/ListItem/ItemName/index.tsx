import styles from "@components/Container/ListItem/ListItem.module.css";

interface ItemNameProps {
	name: string | React.ReactNode;
	code?: string;
	type?: string;
	dependencies?: string[];
}
function ItemName({ name, code, type, dependencies }: ItemNameProps) {
	return (
		<div className={styles.item_display}>
			<div className={styles.item_name}>
				{code && <span className={styles.code}>[{code}]</span>}
				{type && <span className={styles.type}>[{type}]</span>}
				<span className={styles.name}>{name}</span>
			</div>
			{dependencies && (
				<div className={styles.dependencies}>
					{dependencies.map(d => (
						<span key={d} className={styles.dependencie}>
							{d}
						</span>
					))}
				</div>
			)}
		</div>
	);
}

export default ItemName;
