import Container from "@components/Container";
import Loader from "@components/Loader";
import { ModalConfirm } from "@components/Modal";
import { ModalComponent } from "@components/Modal/ModalEntity";
import {
	createComponent,
	deleteComponent,
	getComponents,
	updateComponent
} from "@http/Step2/Components";
import {
	IComponentBorder,
	IComponentFormData,
	IComponentInsertDto,
	IComponentReadDto,
	IComponentType,
	IComponentUpdateDto
} from "@interfaces/IStep2";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";
import ComponentsList from "./ComponentsList";

interface ComponentsContainerProps {
	analysisId: string;
}
function ComponentsContainer({ analysisId }: ComponentsContainerProps) {
	const queryClient = useQueryClient();
	const [selectedComponent, setSelectedComponent] = useState<IComponentReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Component

	const [modalCreateComponentOpen, setModalCreateComponentOpen] = useState(false);
	const toggleModalCreateComponent = () => setModalCreateComponentOpen(!modalCreateComponentOpen);

	const { mutateAsync: requestCreateComponent, isPending: isCreating } = useMutation({
		mutationFn: (component: IComponentInsertDto) => createComponent(component),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-components"] });
			queryClient.invalidateQueries({ queryKey: ["components-select-options"] });
			queryClient.invalidateQueries({
				queryKey: ["control-action-components-select-options"]
			});
			toast.success("Component created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateComponent = async (componentData: IComponentFormData) => {
		const component: IComponentInsertDto = {
			name: componentData.name,
			code: componentData.code,
			border: componentData.border!.value as IComponentBorder,
			isVisible: componentData.isVisible,
			type: componentData.type!.value as IComponentType,
			fatherId: componentData.father ? componentData.father.value : null,
			analysisId: analysisId
		};
		await requestCreateComponent(component);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Component

	const [modalUpdateComponentOpen, setModalUpdateComponentOpen] = useState(false);
	const toggleModalUpdateComponent = () => setModalUpdateComponentOpen(!modalUpdateComponentOpen);

	const { mutateAsync: requestUpdateComponent, isPending: isUpdating } = useMutation({
		mutationFn: ({ id, component }: { id: string; component: IComponentUpdateDto }) =>
			updateComponent(id, component),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-components"] });
			queryClient.invalidateQueries({ queryKey: ["components-select-options"] });
			queryClient.invalidateQueries({
				queryKey: ["control-action-components-select-options"]
			});
			toast.success("Component updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateComponent = async (componentData: IComponentFormData) => {
		const component: IComponentUpdateDto = {
			name: componentData.name,
			code: componentData.code,
			border: componentData.border!.value as IComponentBorder,
			isVisible: componentData.isVisible,
			type: componentData.type!.value as IComponentType,
			fatherId: componentData.father ? componentData.father.value : null
		};
		if (selectedComponent) {
			await requestUpdateComponent({
				id: selectedComponent.id,
				component
			});
			setSelectedComponent(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Component

	const [modalDeleteComponentOpen, setModalDeleteComponentOpen] = useState(false);
	const toggleModalDeleteComponent = () => setModalDeleteComponentOpen(!modalDeleteComponentOpen);

	const { mutateAsync: requestDeleteComponent, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteComponent(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-components"] });
			queryClient.invalidateQueries({ queryKey: ["components-select-options"] });
			queryClient.invalidateQueries({
				queryKey: ["control-action-components-select-options"]
			});
			toast.success("Component deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteComponent = async () => {
		if (selectedComponent) {
			await requestDeleteComponent(selectedComponent.id);
			toggleModalDeleteComponent();
			setSelectedComponent(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Components

	const {
		data: components,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["analysis-components", analysisId],
		queryFn: () => getComponents(analysisId)
	});

	if (isLoading) return <Loader />;
	if (isError || components === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="Components" onOptions={() => {}} onClick={toggleModalCreateComponent}>
				<ComponentsList
					components={components}
					modalDeleteComponent={toggleModalDeleteComponent}
					modalUpdateComponent={toggleModalUpdateComponent}
					selectComponent={setSelectedComponent}
				/>
			</Container>
			<ModalComponent
				open={modalCreateComponentOpen}
				onClose={toggleModalCreateComponent}
				onSubmit={handleCreateComponent}
				isLoading={isCreating}
				title="Create Component"
				btnText="Create"
				components={components}
			/>
			<ModalComponent
				open={modalUpdateComponentOpen}
				onClose={toggleModalUpdateComponent}
				onSubmit={handleUpdateComponent}
				isLoading={isUpdating}
				title="Update Component"
				btnText="Update"
				component={selectedComponent || undefined}
				components={components}
			/>
			<ModalConfirm
				open={modalDeleteComponentOpen}
				onClose={toggleModalDeleteComponent}
				onConfirm={handleDeleteComponent}
				isLoading={isDeleting}
				message="Do you want to delete this component?"
				title="Delete Component"
				btnText="Delete"
			/>
		</>
	);
}

export default ComponentsContainer;
