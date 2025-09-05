import Button from "@components/Button";
import { SelectOption } from "@components/FormField/Templates";
import Loader from "@components/Loader";
import { OptionsList } from "@components/Modal/ModalSelectOptions/Template";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { getComponents } from "@http/Step2/Components";
import { componentsToSelectOptions } from "@interfaces/IStep2/IComponent";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiX } from "react-icons/bi";

interface ModalSelectComponentProps extends ModalProps {
	analysisId: string;
	components: SelectOption[];
	onChange: (components: SelectOption[]) => void;
	options?: SelectOption[];
	multiple?: boolean;
}
function ModalSelectComponent({
	open,
	onClose,
	analysisId,
	components,
	onChange,
	options,
	multiple = false
}: ModalSelectComponentProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Component Options

	const [componentsOptions, setComponentOptions] = useState<SelectOption[]>(options || []);
	const { data: componentsList, isLoading } = useQuery({
		queryKey: ["components-select-options", analysisId],
		queryFn: () => getComponents(analysisId),
		enabled: !options || options.length === 0
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Component to Select Options

	useEffect(() => {
		if (componentsList) {
			setComponentOptions(componentsToSelectOptions(componentsList));
		}
	}, [componentsList]);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<ModalContainer open={open} size="big">
				<ModalHeader
					title={multiple ? "Select the Components" : "Select the Component"}
					onClose={onClose}
				/>
				{isLoading ? (
					<Loader />
				) : (
					<OptionsList
						options={componentsOptions}
						selectedOptions={components}
						onChange={onChange}
						multiple={multiple}
						onSelect={onClose}
					/>
				)}
				<ModalButtons>
					<Button variant="dark" onClick={onClose} size="small" icon={BiX}>
						Close
					</Button>
				</ModalButtons>
			</ModalContainer>
		</>
	);
}

export default ModalSelectComponent;
