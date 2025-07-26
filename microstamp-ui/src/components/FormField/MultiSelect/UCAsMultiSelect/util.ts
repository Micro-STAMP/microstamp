import { SelectOption } from "@components/FormField/Templates";
import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";

const ucasToSelectOptions = (ucas: IUnsafeControlActionReadDto[]): SelectOption[] => {
	return ucas.map((uca, index) => ({
		label: `UCA-${index + 1}: ${uca.name}`,
		value: uca.id
	}));
};

export { ucasToSelectOptions };
