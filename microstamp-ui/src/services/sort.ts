import { IStateReadDto, IVariableReadDto } from "@interfaces/IStep2";
import { IContextTableReadDto } from "@interfaces/IStep3";

const sortContextStatesByVariable = (
	contextTable: IContextTableReadDto,
	variables: IVariableReadDto[]
) => {
	const variableOrder = variables.map(variable => variable.id);
	const compareStatesByVariable = (a: IStateReadDto, b: IStateReadDto) => {
		const indexA = variableOrder.indexOf(a.variable.id);
		const indexB = variableOrder.indexOf(b.variable.id);
		return indexA - indexB;
	};
	const sortedContexts = contextTable.contexts.map(context => ({
		...context,
		states: [...context.states].sort(compareStatesByVariable)
	}));
	return {
		...contextTable,
		contexts: sortedContexts
	};
};

export { sortContextStatesByVariable };
