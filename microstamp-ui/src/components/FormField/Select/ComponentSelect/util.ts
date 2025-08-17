import { SelectOption } from "@components/FormField/Templates";
import { IComponentReadDto, IComponentType } from "@interfaces/IStep2";
import { truncateText } from "@services/text";

const componentToSelectOption = (component: IComponentReadDto): SelectOption => {
	return {
		label: `${component.code}: ${truncateText(component.name, 30)}`,
		value: component.id
	};
};
const componentsToSelectOptions = (
	components: IComponentReadDto[],
	environmentOption: boolean = false
): SelectOption[] => {
	if (environmentOption) {
		return components.map(component => componentToSelectOption(component));
	} else {
		return components
			.filter(component => component.type !== IComponentType.ENVIRONMENT)
			.map(component => componentToSelectOption(component));
	}
};

export { componentsToSelectOptions, componentToSelectOption };
