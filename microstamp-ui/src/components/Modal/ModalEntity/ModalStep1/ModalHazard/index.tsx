import Button from "@components/Button";
import { Checkbox, Input } from "@components/FormField";
import { hazardsToSelectOptions } from "@components/FormField/MultiSelect/HazardsMultiSelect/util";
import LossesMultiSelect from "@components/FormField/MultiSelect/LossesMultiSelect";
import { lossesToSelectOptions } from "@components/FormField/MultiSelect/LossesMultiSelect/util";
import HazardSelect from "@components/FormField/Select/HazardSelect";
import { hazardToSelectOption } from "@components/FormField/Select/HazardSelect/util";
import { SelectOption } from "@components/FormField/Templates";
import Loader from "@components/Loader";
import {
	ModalButtons,
	ModalContainer,
	ModalHeader,
	ModalInputs,
	ModalProps
} from "@components/Modal/Templates";
import { getHazards } from "@http/Step1/Hazards";
import { IHazardFormData, IHazardReadDto } from "@interfaces/IStep1";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";

interface ModalHazardProps extends ModalProps {
	onSubmit: (hazard: IHazardFormData) => Promise<void>;
	title: string;
	analysisId: string;
	isLoading?: boolean;
	hazard?: IHazardReadDto;
	btnText?: string;
}
function ModalHazard({
	open,
	onClose,
	onSubmit,
	analysisId,
	title,
	isLoading = false,
	hazard,
	btnText = "Confirm"
}: ModalHazardProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Hazard Options

	const [hazardsOptions, setHazardsOptions] = useState<SelectOption[]>([]);
	const {
		data: hazards,
		isLoading: isLoadingHazards,
		isError
	} = useQuery({
		queryKey: ["hazards-select-options", analysisId],
		queryFn: () => getHazards(analysisId)
	});

	useEffect(() => {
		if (hazards) {
			if (hazard) {
				setHazardsOptions(
					hazardsToSelectOptions(
						hazards.filter(h => h.id !== hazard.id && h.father?.id !== hazard.id)
					)
				);
			} else {
				setHazardsOptions(hazardsToSelectOptions(hazards));
			}
		}
	}, [hazards, hazard]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Is Subhazard State

	const [isSubhazard, setIsSubhazard] = useState<boolean>(
		hazard !== undefined && hazard.father !== null
	);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Hazard Data

	const [hazardData, setHazardData] = useState<IHazardFormData>({
		name: hazard?.name || "",
		code: hazard?.code || "",
		losses: hazard ? lossesToSelectOptions(hazard.losses) : [],
		father: hazard && hazard.father ? hazardToSelectOption(hazard.father) : null
	});

	useEffect(() => {
		if (hazard) {
			setHazardData({
				name: hazard.name,
				code: hazard.code,
				losses: lossesToSelectOptions(hazard.losses),
				father: hazard.father ? hazardToSelectOption(hazard.father) : null
			});
			setIsSubhazard(hazard.father !== null);
		} else {
			setHazardData({
				name: "",
				code: "",
				losses: [],
				father: null
			});
			setIsSubhazard(false);
		}
	}, [hazard]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Submit Hazard

	const handleSubmitHazard = async () => {
		if (!hazardData.name || !hazardData.code || hazardData.losses.length === 0) {
			toast.warning("A required field is empty.");
			return;
		}
		if (isSubhazard && !hazardData.father) {
			toast.warning("Sub-hazards need a Hazard.");
			return;
		}
		await onSubmit(hazardData);
		setHazardData({
			name: hazard ? hazardData.name : "",
			code: hazard ? hazardData.code : "",
			losses: hazard ? lossesToSelectOptions(hazard.losses) : [],
			father: hazard && hazard.father ? hazardToSelectOption(hazard.father) : null
		});
		setIsSubhazard(hazard !== undefined && hazard.father !== null);
		onClose();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingHazards) return <Loader />;
	if (isError || hazards === undefined) return <h1>Error</h1>;
	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title={title} />
			<ModalInputs>
				<Input
					label="Name"
					value={hazardData.name}
					onChange={(value: string) => setHazardData({ ...hazardData, name: value })}
					required
				/>
				<Input
					label="Code"
					value={hazardData.code}
					onChange={(value: string) => setHazardData({ ...hazardData, code: value })}
					required
				/>
				<LossesMultiSelect
					analysisId={analysisId}
					losses={hazardData.losses}
					onChange={(losses: SelectOption[]) =>
						setHazardData({ ...hazardData, losses: losses })
					}
				/>
				<div style={{ width: "100%", marginTop: "1rem" }}>
					<Checkbox
						checked={isSubhazard}
						label="Is Sub-hazard?"
						onChange={checked => {
							setIsSubhazard(checked);
							if (!checked) {
								setHazardData({ ...hazardData, father: null });
							}
						}}
					/>
				</div>
				{isSubhazard && (
					<HazardSelect
						value={hazardData.father}
						onChange={(value: SelectOption | null) =>
							setHazardData({ ...hazardData, father: value })
						}
						hazards={hazardsOptions}
						disabled={hazardsOptions.length === 0}
					/>
				)}
			</ModalInputs>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Cancel
				</Button>
				<Button
					onClick={handleSubmitHazard}
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

export default ModalHazard;
