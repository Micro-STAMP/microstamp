import { IComponentBorder, IComponentType } from "./Enums";
import { IComponentDependenciesDto } from "./IComponentDependenciesDto";
import { IComponentFormData } from "./IComponentFormData";
import { IComponentInsertDto } from "./IComponentInsertDto";
import { IComponentReadDto } from "./IComponentReadDto";
import { componentToSelectOption, componentsToSelectOptions } from "./IComponentSelectOption";
import { IComponentUpdateDto } from "./IComponentUpdateDto";

export { IComponentBorder, IComponentType, componentToSelectOption, componentsToSelectOptions };
export type {
	IComponentDependenciesDto,
	IComponentFormData,
	IComponentInsertDto,
	IComponentReadDto,
	IComponentUpdateDto
};
