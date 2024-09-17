import Button from "@components/Button";
import { Checkbox, Input, Select } from "@components/FormField";
import ComponentSelect from "@components/FormField/Select/ComponentSelect";
import {
	componentsToSelectOptions,
	componentToSelectOption
} from "@components/FormField/Select/ComponentSelect/util";
import { SelectOption } from "@components/FormField/Templates";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { IComponentFormData, IComponentReadDto } from "@interfaces/IStep2";
import {
	borderSelectOptions,
	borderToSelectOption,
	componentTypeSelectOptions,
	componentTypeToSelectOption
} from "@interfaces/IStep2/IComponent/Enums";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalComponentProps extends ModalProps {
	onSubmit: (component: IComponentFormData) => Promise<void>;
	title: string;
	isLoading?: boolean;
	component?: IComponentReadDto;
	btnText?: string;
	components: IComponentReadDto[];
}
function ModalComponent({
	open,
	onClose,
	onSubmit,
	title,
	isLoading = false,
	component,
	btnText = "Confirm",
	components
}: ModalComponentProps) {
	const componentsOptions = component
		? componentsToSelectOptions(components.filter(c => c.id !== component.id))
		: componentsToSelectOptions(components);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Component Data

	const [componentData, setComponentData] = useState<IComponentFormData>({
		name: component?.name || "",
		code: component?.code || "",
		isVisible: component?.isVisible || true,
		border: component ? borderToSelectOption(component.border) : borderSelectOptions[0],
		type: component
			? componentTypeToSelectOption(component.type)
			: componentTypeSelectOptions[0],
		father: component && component.father ? componentToSelectOption(component.father) : null
	});

	useEffect(() => {
		if (component) {
			setComponentData({
				name: component.name,
				code: component.code,
				isVisible: component.isVisible,
				border: borderToSelectOption(component.border),
				type: componentTypeToSelectOption(component.type),
				father: component.father ? componentToSelectOption(component.father) : null
			});
		} else {
			setComponentData({
				name: "",
				code: "",
				isVisible: true,
				border: borderSelectOptions[0],
				type: componentTypeSelectOptions[0],
				father: null
			});
		}
	}, [component]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Component

	const handleSubmitComponent = async () => {
		if (!componentData.name || !componentData.code) {
			toast.warning("A required field is empty.");
			return;
		}
		await onSubmit(componentData);
		setComponentData({
			name: component?.name || "",
			code: component?.code || "",
			isVisible: component?.isVisible || true,
			border: component ? borderToSelectOption(component.border) : borderSelectOptions[0],
			type: component
				? componentTypeToSelectOption(component.type)
				: componentTypeSelectOptions[0],
			father: component && component.father ? componentToSelectOption(component.father) : null
		});
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<ModalContainer open={open} size="big">
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs column="double">
				<Input
					label="Name"
					value={componentData.name}
					onChange={(value: string) =>
						setComponentData({ ...componentData, name: value })
					}
					required
				/>
				<Input
					label="Code"
					value={componentData.code}
					onChange={(value: string) =>
						setComponentData({ ...componentData, code: value })
					}
					required
				/>
				<Select
					label="Type"
					options={componentTypeSelectOptions}
					onChange={(type: SelectOption | null) =>
						setComponentData({ ...componentData, type: type })
					}
					value={componentData.type}
					optionsPosition="top"
					required
				/>
				<Select
					label="Border"
					options={borderSelectOptions}
					onChange={(border: SelectOption | null) =>
						setComponentData({ ...componentData, border: border })
					}
					value={componentData.border}
					optionsPosition="top"
					required
				/>
				<ComponentSelect
					label="Father"
					onChange={(father: SelectOption | null) =>
						setComponentData({ ...componentData, father: father })
					}
					value={componentData.father}
					components={componentsOptions}
				/>
				<div style={{ width: "100%", marginTop: "1rem" }}>
					<Checkbox
						label="Is Visible?"
						checked={componentData.isVisible}
						onChange={(check: boolean) =>
							setComponentData({ ...componentData, isVisible: check })
						}
					/>
				</div>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitComponent}
					isLoading={isLoading}
					size="small"
					icon={CheckIcon}
				>
					{btnText}
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalComponent;
