import { SelectOption } from "@components/FormField/Templates";
import { IComponentReadDto } from "@interfaces/IStep2";
import { truncateText } from "@services/text";

const componentToSelectOption = (component: IComponentReadDto): SelectOption => {
	return {
		label: `${component.code}: ${truncateText(component.name, 30)}`,
		value: component.id
	};
};
const componentsToSelectOptions = (components: IComponentReadDto[]): SelectOption[] => {
	return components.map(component => componentToSelectOption(component));
};

export { componentsToSelectOptions, componentToSelectOption };
