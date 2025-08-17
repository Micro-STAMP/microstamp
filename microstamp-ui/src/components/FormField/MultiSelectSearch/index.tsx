import { SelectOption } from "@components/FormField/Templates";
import { truncateText } from "@services/text";
import { BiX as CloseIcon, BiSearch as SearchIcon } from "react-icons/bi";
import styles from "./MultiSelectSearch.module.css";

interface MultiSelectSearchProps {
	label: string;
	values: SelectOption[];
	onSearch: () => void;
	onChange: (values: SelectOption[]) => void;
	disabled?: boolean;
	truncate?: boolean;
}
function MultiSelectSearch({
	label,
	values,
	onSearch,
	onChange,
	disabled = false,
	truncate = false
}: MultiSelectSearchProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Remove an Option

	const removeOption = (option: SelectOption) => {
		const updatedValues = values.filter(item => item.value !== option.value);
		onChange(updatedValues);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<label className={styles.multi_select_search_label}>
			<span className={styles.name}>{label}:</span>
			<div
				className={`${styles.multi_select_search_container} ${disabled && styles.disabled}`}
			>
				<button
					type="button"
					onClick={() => !disabled && onSearch()}
					className={styles.multi_select_search}
					disabled={disabled}
				>
					<div className={styles.selected_values}>
						{values.map(value => (
							<div key={value.value} className={styles.selected_value}>
								<span>
									{truncate ? truncateText(value.label, 32) : value.label}
								</span>
								<CloseIcon
									className={styles.remove_button}
									onClick={() => removeOption(value)}
								/>
							</div>
						))}
					</div>
					<SearchIcon className={styles.icon} />
				</button>
			</div>
		</label>
	);
}

export default MultiSelectSearch;
