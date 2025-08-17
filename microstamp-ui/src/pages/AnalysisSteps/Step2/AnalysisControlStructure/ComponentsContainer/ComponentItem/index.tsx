import DualButton from "@components/Button/DualButton";
import IconButton from "@components/Button/IconButton";
import { ListItem as Component } from "@components/Container/ListItem";
import { IComponentReadDto, IComponentType } from "@interfaces/IStep2";
import { BiInfoCircle as InfoIcon } from "react-icons/bi";
import { useNavigate } from "react-router-dom";

interface ComponentItemProps {
	component: IComponentReadDto;
	selectComponent: (component: IComponentReadDto) => void;
	modalUpdateComponent: () => void;
	modalDeleteComponent: () => void;
}
function ComponentItem({
	component,
	selectComponent,
	modalDeleteComponent,
	modalUpdateComponent
}: ComponentItemProps) {
	const navigate = useNavigate();
	return (
		<Component.Root key={component.id}>
			<Component.Name
				code={component.code}
				name={component.name}
				dependencies={component.father ? [component.father.code] : []}
			/>
			<Component.Actions>
				<IconButton
					icon={InfoIcon}
					onClick={() => {
						navigate(`component/${component.id}`);
					}}
				/>
				{component.type !== IComponentType.ENVIRONMENT && (
					<DualButton
						onEdit={() => {
							selectComponent(component);
							modalUpdateComponent();
						}}
						onDelete={() => {
							selectComponent(component);
							modalDeleteComponent();
						}}
					/>
				)}
			</Component.Actions>
		</Component.Root>
	);
}

export default ComponentItem;
