import { Select } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";

interface SystemSafetyConstraintSelectProps {
	value: SelectOption | null;
	onChange: (value: SelectOption | null) => void;
	systemSafetyConstraints: SelectOption[];
	disabled?: boolean;
	label?: string;
}
function SystemSafetyConstraintSelect({
	value,
	onChange,
	systemSafetyConstraints,
	disabled,
	label = "System Safety Constraint"
}: SystemSafetyConstraintSelectProps) {
	return (
		<>
			<Select
				label={label}
				value={value}
				options={systemSafetyConstraints}
				disabled={disabled}
				optionsPosition="top"
				onChange={onChange}
				required
			/>
		</>
	);
}

export default SystemSafetyConstraintSelect;
