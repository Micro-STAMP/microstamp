import { SelectOption } from "@components/FormField/Templates";



enum IIcaType {
	NOT_PROVIDED = "NOT_PROVIDED",
	PROVIDED_UNSAFE = "PROVIDED_UNSAFE",
	TOO_EARLY_TOO_LATE_WRONG_SEQUENCE = "TOO_EARLY_TOO_LATE_WRONG_SEQUENCE",
	STOPPED_TOO_SOON_APPLIED_TOO_LONG = "STOPPED_TOO_SOON_APPLIED_TOO_LONG"
}

export { IIcaType };


const icaTypeSelectOptions: SelectOption[] = [
	{ label: "Not Provided", value: IIcaType.NOT_PROVIDED },
	{ label: "Provided Unsafely", value: IIcaType.PROVIDED_UNSAFE },
	{ label: "Too Early / Too Late / Wrong Sequence", value: IIcaType.TOO_EARLY_TOO_LATE_WRONG_SEQUENCE },
	{ label: "Stopped Too Soon / Applied Too Long", value: IIcaType.STOPPED_TOO_SOON_APPLIED_TOO_LONG }
];

export { icaTypeSelectOptions };



const icaTypeToSelectOptionMap: Record<IIcaType, SelectOption> = {
	[IIcaType.NOT_PROVIDED]: icaTypeSelectOptions[0],
	[IIcaType.PROVIDED_UNSAFE]: icaTypeSelectOptions[1],
	[IIcaType.TOO_EARLY_TOO_LATE_WRONG_SEQUENCE]: icaTypeSelectOptions[2],
	[IIcaType.STOPPED_TOO_SOON_APPLIED_TOO_LONG]: icaTypeSelectOptions[3]
};
const icaTypeToSelectOption = (icaType: IIcaType): SelectOption => {
	return icaTypeToSelectOptionMap[icaType];
};

export { icaTypeToSelectOption };

