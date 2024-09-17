import { SelectOption } from "@components/FormField/Templates";

/* - - - - - - - - - - - - - - - - - - - - - - */

// Unsafe Control Action Type Enum

enum IUCAType {
	PROVIDED = "PROVIDED",
	NOT_PROVIDED = "NOT_PROVIDED",
	TOO_EARLY = "TOO_EARLY",
	TOO_LATE = "TOO_LATE",
	OUT_OF_ORDER = "OUT_OF_ORDER",
	STOPPED_TOO_SOON = "STOPPED_TOO_SOON",
	APPLIED_TOO_LONG = "APPLIED_TOO_LONG"
}

export { IUCAType };

/* - - - - - - - - - - - - - - - - - - - - - - */

// UCA Types Array

const IUCATypesArray = [
	"Provided",
	"Not Provided",
	"Too Early",
	"Too Late",
	"Out of Order",
	"Stopped Too Soon",
	"Applied Too Long"
];

export { IUCATypesArray };

/* - - - - - - - - - - - - - - - - - - - - - - */

// UCA Type Select Options

const ucaTypeSelectOptions: SelectOption[] = [
	{ label: "Provided", value: IUCAType.PROVIDED },
	{ label: "Not Provided", value: IUCAType.NOT_PROVIDED },
	{ label: "Too Early", value: IUCAType.TOO_EARLY },
	{ label: "Too Late", value: IUCAType.TOO_LATE },
	{ label: "Out of Order", value: IUCAType.OUT_OF_ORDER },
	{ label: "Stopped Too Soon", value: IUCAType.STOPPED_TOO_SOON },
	{ label: "Applied Too Long", value: IUCAType.APPLIED_TOO_LONG }
];

export { ucaTypeSelectOptions };

/* - - - - - - - - - - - - - - - - - - - - - - */

// UCA Type to Select Option

const ucaTypeToSelectOptionMap: Record<IUCAType, SelectOption> = {
	[IUCAType.PROVIDED]: ucaTypeSelectOptions[0],
	[IUCAType.NOT_PROVIDED]: ucaTypeSelectOptions[1],
	[IUCAType.TOO_EARLY]: ucaTypeSelectOptions[2],
	[IUCAType.TOO_LATE]: ucaTypeSelectOptions[3],
	[IUCAType.OUT_OF_ORDER]: ucaTypeSelectOptions[4],
	[IUCAType.STOPPED_TOO_SOON]: ucaTypeSelectOptions[5],
	[IUCAType.APPLIED_TOO_LONG]: ucaTypeSelectOptions[6]
};
const ucaTypeToSelectOption = (ucaType: IUCAType): SelectOption => {
	return ucaTypeToSelectOptionMap[ucaType];
};

export { ucaTypeToSelectOption };

/* - - - - - - - - - - - - - - - - - - - - - - */
