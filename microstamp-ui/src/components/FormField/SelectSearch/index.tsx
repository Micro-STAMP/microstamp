import { SelectOption } from "@components/FormField/Templates";
import { BiSearch as SearchIcon } from "react-icons/bi";
import styles from "./SelectSearch.module.css";

interface SelectProps {
	label: string;
	value: SelectOption | null;
	onSearch: () => void;
	disabled?: boolean;
}
function Select({ label, value, onSearch, disabled = false }: SelectProps) {
	return (
		<label className={styles.select_search_label}>
			<span className={styles.name}>{label}:</span>
			<div className={`${styles.select_search_container} ${disabled && styles.disabled}`}>
				<button
					type="button"
					onClick={() => !disabled && onSearch()}
					className={styles.select_search}
					disabled={disabled}
				>
					{value ? (
						<span>{value.label}</span>
					) : (
						<span style={{ color: "var(--color-dark)" }}>None</span>
					)}

					<SearchIcon className={styles.icon} />
				</button>
			</div>
		</label>
	);
}

export default Select;
