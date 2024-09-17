import { Select } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";

interface ComponentSelectProps {
	label?: string;
	value: SelectOption | null;
	onChange: (value: SelectOption | null) => void;
	components: SelectOption[];
	disabled?: boolean;
	required?: boolean;
	optionsPosition?: "top" | "bottom";
}
function ComponentSelect({
	label = "Component",
	value,
	components,
	onChange,
	disabled,
	required,
	optionsPosition = "top"
}: ComponentSelectProps) {
	return (
		<>
			<Select
				label={label}
				value={value}
				onChange={onChange}
				options={components}
				disabled={disabled}
				required={required}
				optionsPosition={optionsPosition}
			/>
		</>
	);
}

export default ComponentSelect;
