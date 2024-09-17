const TYPES_ARRAY = [
	"Provided",
	"Not provided",
	"Too early",
	"Too late",
	"Out of order",
	"Stopped too soon",
	"Applied too long"
];

const TYPES_SELECT_OPTIONS = TYPES_ARRAY.map(type => ({
	label: type,
	value: type.toUpperCase().split(" ").join("_")
}));

export { TYPES_ARRAY, TYPES_SELECT_OPTIONS };
