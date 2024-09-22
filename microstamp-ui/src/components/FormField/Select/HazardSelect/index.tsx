import { Select } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";

interface HazardSelectProps {
	label?: string;
	value: SelectOption | null;
	onChange: (value: SelectOption | null) => void;
	hazards: SelectOption[];
	disabled?: boolean;
	required?: boolean;
	optionsPosition?: "top" | "bottom";
}
function HazardSelect({
	label = "Hazard",
	value,
	hazards,
	onChange,
	disabled,
	required,
	optionsPosition = "top"
}: HazardSelectProps) {
	return (
		<>
			<Select
				label={label}
				value={value}
				onChange={onChange}
				options={hazards}
				required={required}
				disabled={disabled}
				optionsPosition={optionsPosition}
			/>
		</>
	);
}

export default HazardSelect;
