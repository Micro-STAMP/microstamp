import { SelectOption } from "@components/FormField/Templates";
import { useState } from "react";
import {
	BiChevronDown as ArrowDown,
	BiChevronUp as ArrowUp,
	BiX as CloseIcon
} from "react-icons/bi";
import styles from "./MultiSelect.module.css";

interface MultiSelectProps {
	label: string;
	options: SelectOption[];
	values: SelectOption[];
	onChange: (values: SelectOption[]) => void;
	optionsPosition?: "top" | "bottom";
	disabled?: boolean;
}
function MultiSelect({
	label,
	options,
	values,
	onChange,
	optionsPosition = "bottom",
	disabled = false
}: MultiSelectProps) {
	const [isOpen, setIsOpen] = useState(false);

	const selectOption = (option: SelectOption) => {
		const isSelected = values.some(value => value.value === option.value);
		if (!isSelected) {
			onChange([...values, option]);
		}
	};
	const removeOption = (option: SelectOption) => {
		const updatedValues = values.filter(item => item.value !== option.value);
		onChange(updatedValues);
	};

	const options_class = `${styles.options} ${
		optionsPosition === "top" ? styles.options_top : ""
	}`;
	const multi_select_class = `${styles.multi_select} ${
		isOpen ? (optionsPosition === "top" ? styles.open_top : styles.open_bottom) : ""
	}`;
	return (
		<label className={styles.multi_select_label}>
			<span className={styles.name}>{label}:</span>
			<button
				type="button"
				onClick={() => setIsOpen(!isOpen)}
				className={multi_select_class}
				disabled={disabled}
			>
				<div className={styles.selected_values}>
					{values.map(value => (
						<div key={value.value} className={styles.selected_value}>
							<span>{value.label}</span>
							<CloseIcon
								className={styles.remove_button}
								onClick={() => removeOption(value)}
							/>
						</div>
					))}
				</div>
				{isOpen ? (
					<ArrowUp className={styles.icon} />
				) : (
					<ArrowDown className={styles.icon} />
				)}
			</button>
			{isOpen && !disabled && (
				<ul className={options_class}>
					{options.map(option => (
						<li
							className={`${styles.option} ${
								values.some(value => value.value === option.value)
									? styles.selected
									: ""
							}`}
							key={option.value}
							onClick={() => selectOption(option)}
						>
							<span>{option.label}</span>
						</li>
					))}
				</ul>
			)}
		</label>
	);
}

export default MultiSelect;
