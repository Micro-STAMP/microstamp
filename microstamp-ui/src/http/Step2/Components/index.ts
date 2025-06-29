import { http } from "@http/AxiosConfig";
import {
	IComponentInsertDto,
	IComponentReadDto,
	IComponentType,
	IComponentUpdateDto
} from "@interfaces/IStep2";
import { IComponentDependenciesDto } from "@interfaces/IStep2/IComponent";

/* - - - - - - - - - - - - - - - - - - - - - - */

const handleComponentType = (component: any): IComponentReadDto => {
	const typeEnum =
		component.type === "Controller"
			? IComponentType.CONTROLLER
			: component.type === "Actuator"
			? IComponentType.ACTUATOR
			: component.type === "Sensor"
			? IComponentType.SENSOR
			: component.type === "ControlledProcess"
			? IComponentType.CONTROLLED_PROCESS
			: IComponentType.ENVIRONMENT;
	return {
		...component,
		type: typeEnum
	};
};

/* - - - - - - - - - - - - - - - - - - - - - - */

const COMPONENTS_ENDPOINT = "step2/components";

const getComponents = async (analysisId: string) => {
	try {
		const res = await http.get<IComponentReadDto[]>(
			`${COMPONENTS_ENDPOINT}/analysis/${analysisId}`
		);
		return res.data.map(c => handleComponentType(c));
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getComponent = async (id: string) => {
	try {
		const res = await http.get<IComponentReadDto>(`${COMPONENTS_ENDPOINT}/${id}`);
		return handleComponentType(res.data);
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const getComponentDependencies = async (id: string) => {
	try {
		const res = await http.get<IComponentDependenciesDto>(
			`${COMPONENTS_ENDPOINT}/${id}/dependencies`
		);
		return res.data;
	} catch (err) {
		console.error(err);
		throw err;
	}
};
const createComponent = async (component: IComponentInsertDto) => {
	try {
		const res = await http.post<IComponentReadDto>(COMPONENTS_ENDPOINT, component);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error creating component.");
	}
};
const updateComponent = async (id: string, component: IComponentUpdateDto) => {
	try {
		const res = await http.put<IComponentReadDto>(`${COMPONENTS_ENDPOINT}/${id}`, component);
		return res.data;
	} catch (err) {
		console.error(err);
		throw new Error("Error updating component.");
	}
};
const deleteComponent = async (id: string) => {
	try {
		await http.delete(`${COMPONENTS_ENDPOINT}/${id}`);
	} catch (err) {
		console.error(err);
		throw new Error("Error deleting component.");
	}
};

export {
	createComponent,
	deleteComponent,
	getComponent,
	getComponentDependencies,
	getComponents,
	updateComponent
};

/* - - - - - - - - - - - - - - - - - - - - - - */
