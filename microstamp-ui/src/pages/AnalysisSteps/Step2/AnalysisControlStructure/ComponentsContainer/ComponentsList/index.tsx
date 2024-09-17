import ListWrapper from "@components/Container/ListWrapper";
import { IComponentReadDto, IComponentType } from "@interfaces/IStep2";
import ComponentItem from "@pages/AnalysisSteps/Step2/AnalysisControlStructure/ComponentsContainer/ComponentItem";
import styles from "./ComponentsList.module.css";

interface ComponentsListProps {
	components: IComponentReadDto[];
	selectComponent: (component: IComponentReadDto) => void;
	modalUpdateComponent: () => void;
	modalDeleteComponent: () => void;
}

function ComponentsList({
	components,
	selectComponent,
	modalDeleteComponent,
	modalUpdateComponent
}: ComponentsListProps) {
	const controllerList = components.filter(c => c.type === IComponentType.CONTROLLER);
	const controlledProcessList = components.filter(
		c => c.type === IComponentType.CONTROLLED_PROCESS
	);
	const actuatorList = components.filter(c => c.type === IComponentType.ACTUATOR);
	const sensorList = components.filter(c => c.type === IComponentType.SENSOR);

	const renderComponentList = (title: string, componentList: IComponentReadDto[]) => {
		return (
			componentList.length > 0 && (
				<div className={styles.component_section}>
					<span className={styles.title}>[{title}]</span>
					<ListWrapper>
						{componentList.map(component => (
							<ComponentItem
								key={component.id}
								component={component}
								modalDeleteComponent={modalDeleteComponent}
								modalUpdateComponent={modalUpdateComponent}
								selectComponent={selectComponent}
							/>
						))}
					</ListWrapper>
				</div>
			)
		);
	};

	return (
		<div className={styles.components_list}>
			{renderComponentList("Controllers", controllerList)}
			{renderComponentList("Controlled Processes", controlledProcessList)}
			{renderComponentList("Actuators", actuatorList)}
			{renderComponentList("Sensors", sensorList)}
		</div>
	);
}

export default ComponentsList;
