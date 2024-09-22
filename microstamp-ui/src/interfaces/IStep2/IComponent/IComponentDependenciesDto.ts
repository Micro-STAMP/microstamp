import { IConnectionReadDto } from "@interfaces/IStep2/IConnection";
import { IVariableReadDto } from "@interfaces/IStep2/IVariable";
import { IComponentReadDto } from "./IComponentReadDto";

interface IComponentDependenciesDto {
	components: IComponentReadDto[];
	connections: IConnectionReadDto[];
	variables: IVariableReadDto[];
}

export type { IComponentDependenciesDto };
