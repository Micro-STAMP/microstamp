import { SelectOption } from "@components/FormField/Templates";
import NoResultsMessage from "@components/NoResultsMessage";
import { BiCircle, BiSolidCheckCircle } from "react-icons/bi";
import styles from "./OptionsList.module.css";

interface ModalSelectOptionsProps {
	options: SelectOption[];
	onChange: (value: SelectOption[]) => void;
	selectedOptions: SelectOption[];
	onSelect?: () => void;
	multiple?: boolean;
}
function OptionsList({
	options,
	onChange,
	onSelect,
	selectedOptions,
	multiple = false
}: ModalSelectOptionsProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Select Option

	const handleSelectOption = (option: SelectOption) => {
		if (multiple) {
			const isSelected = selectedOptions.some(selected => selected.value === option.value);
			if (isSelected) {
				onChange(selectedOptions.filter(selected => selected.value !== option.value));
			} else {
				onChange([...selectedOptions, option]);
			}
		} else {
			onChange([option]);
			onSelect?.();
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<div className={styles.options_list}>
			{options.length > 0 ? (
				options.map(option => {
					const isSelected = selectedOptions.some(
						selected => selected.value === option.value
					);
					return (
						<div
							key={option.value}
							className={`${styles.option_item} ${isSelected ? styles.selected : ""}`}
							onClick={() => handleSelectOption(option)}
						>
							<div className={styles.option_label}>{option.label}</div>
							{isSelected ? (
								<BiSolidCheckCircle
									className={`${styles.check_icon} ${styles.filled}`}
								/>
							) : (
								<BiCircle className={styles.check_icon} />
							)}
						</div>
					);
				})
			) : (
				<NoResultsMessage message="No options available." />
			)}
		</div>
	);
}

export default OptionsList;
