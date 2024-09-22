import { SelectOption } from "@components/FormField/Templates";
import { ILossReadDto } from "@interfaces/IStep1";
import { truncateText } from "@services/text";

const lossesToSelectOptions = (losses: ILossReadDto[]): SelectOption[] => {
	return losses.map(loss => ({
		label: `${loss.code}: ${truncateText(loss.name, 30)}`,
		value: loss.id
	}));
};

export { lossesToSelectOptions };
