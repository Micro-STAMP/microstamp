import Button from "@components/Button";
import { Input, Select } from "@components/FormField";
import ComponentSelect from "@components/FormField/Select/ComponentSelect";
import {
	componentsToSelectOptions,
	componentToSelectOption
} from "@components/FormField/Select/ComponentSelect/util";
import { SelectOption } from "@components/FormField/Templates";
import Loader from "@components/Loader";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import NoResultsMessage from "@components/NoResultsMessage";
import { getComponents } from "@http/Step2/Components";
import { IConnectionFormData, IConnectionReadDto } from "@interfaces/IStep2";
import {
	connectionStyleSelectOptions,
	connectionStyleToSelectOption
} from "@interfaces/IStep2/IConnection/Enums";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalConnectionProps extends ModalProps {
	onSubmit: (connection: IConnectionFormData) => Promise<void>;
	title: string;
	analysisId: string;
	isLoading?: boolean;
	connection?: IConnectionReadDto;
	btnText?: string;
}
function ModalConnection({
	open,
	onClose,
	onSubmit,
	title,
	analysisId,
	connection,
	isLoading = false,
	btnText = "Confirm"
}: ModalConnectionProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Components Options

	const [componentsOptions, setComponentsOptions] = useState<SelectOption[]>([]);
	const {
		data: components,
		isLoading: isLoadingComponents,
		isError
	} = useQuery({
		queryKey: ["components-select-options", analysisId],
		queryFn: () => getComponents(analysisId)
	});

	useEffect(() => {
		if (components) {
			setComponentsOptions(componentsToSelectOptions(components, true));
		}
	}, [components]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Connection Data

	const [connectionData, setConnectionData] = useState<IConnectionFormData>({
		code: connection?.code || "",
		style: connection
			? connectionStyleToSelectOption(connection.style)
			: connectionStyleSelectOptions[0],
		source: connection ? componentToSelectOption(connection.source) : componentsOptions[0],
		target: connection ? componentToSelectOption(connection.target) : componentsOptions[0]
	});
	useEffect(() => {
		if (connection) {
			setConnectionData({
				code: connection.code,
				style: connectionStyleToSelectOption(connection.style),
				source: componentToSelectOption(connection.source),
				target: componentToSelectOption(connection.target)
			});
		} else {
			setConnectionData({
				code: "",
				style: connectionStyleSelectOptions[0],
				source: componentsOptions[0],
				target: componentsOptions[0]
			});
		}
	}, [connection, componentsOptions, components]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Connection

	const handleSubmitConnection = async () => {
		if (
			!connectionData.code ||
			!connectionData.style ||
			!connectionData.source ||
			!connectionData.target
		) {
			toast.warning("A required field is empty.");
			return;
		}
		if (connectionData.source.value === connectionData.target.value) {
			toast.warning("Source and target can't be the same.");
			return;
		}
		await onSubmit(connectionData);
		setConnectionData({
			code: connection?.code || "",
			style: connection
				? connectionStyleToSelectOption(connection.style)
				: connectionStyleSelectOptions[0],
			source: connection ? componentToSelectOption(connection.source) : componentsOptions[0],
			target: connection ? componentToSelectOption(connection.target) : componentsOptions[0]
		});
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingComponents)
		return (
			<ModalContainer open={open} size="small">
				<Loader />;
			</ModalContainer>
		);
	if (isError || components === undefined)
		return (
			<ModalContainer open={open} size="small">
				<ModalHeader onClose={onClose} title="Error" />
				<NoResultsMessage message="Error loading modal." />
			</ModalContainer>
		);
	return (
		<ModalContainer open={open} size="big">
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs column="double">
				<Input
					label="Code"
					value={connectionData.code}
					onChange={(value: string) =>
						setConnectionData({ ...connectionData, code: value })
					}
					required
				/>
				<Select
					label="Style"
					options={connectionStyleSelectOptions}
					onChange={(style: SelectOption | null) =>
						setConnectionData({ ...connectionData, style: style })
					}
					value={connectionData.style}
					optionsPosition="bottom"
					required
				/>
				<ComponentSelect
					label="Source"
					components={componentsOptions}
					onChange={(source: SelectOption | null) =>
						setConnectionData({ ...connectionData, source: source })
					}
					value={connectionData.source}
					required
				/>
				<ComponentSelect
					label="Target"
					components={componentsOptions}
					onChange={(target: SelectOption | null) =>
						setConnectionData({ ...connectionData, target: target })
					}
					value={connectionData.target}
					required
				/>
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitConnection}
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

export default ModalConnection;
