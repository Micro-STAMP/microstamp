import { Select } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";

interface SystemSafetyConstraintSelectProps {
	value: SelectOption | null;
	onChange: (value: SelectOption | null) => void;
	systemSafetyConstraints: SelectOption[];
	disabled?: boolean;
}
function SystemSafetyConstraintSelect({
	value,
	onChange,
	systemSafetyConstraints,
	disabled
}: SystemSafetyConstraintSelectProps) {
	return (
		<>
			<Select
				label="System Safety Constraint"
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
