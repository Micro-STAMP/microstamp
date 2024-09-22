import { SelectOption } from "@components/FormField/Templates";
import { ISystemSafetyConstraintReadDto } from "@interfaces/IStep1";
import { truncateText } from "@services/text";

const systemSafetyConstraintToSelectOption = (
	systemSafetyConstraint: ISystemSafetyConstraintReadDto
): SelectOption => {
	return {
		label: `${systemSafetyConstraint.code}: ${truncateText(systemSafetyConstraint.name, 30)}`,
		value: systemSafetyConstraint.id
	};
};
const systemSafetyConstraintsToSelectOptions = (
	systemSafetyConstraints: ISystemSafetyConstraintReadDto[]
): SelectOption[] => {
	return systemSafetyConstraints.map(systemSafetyConstraint =>
		systemSafetyConstraintToSelectOption(systemSafetyConstraint)
	);
};

export { systemSafetyConstraintsToSelectOptions, systemSafetyConstraintToSelectOption };
