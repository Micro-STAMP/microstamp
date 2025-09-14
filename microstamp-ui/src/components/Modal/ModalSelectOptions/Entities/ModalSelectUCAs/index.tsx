import Button from "@components/Button";
import { SelectOption } from "@components/FormField/Templates";
import Loader from "@components/Loader";
import { OptionsList } from "@components/Modal/ModalSelectOptions/Template";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { getUnsafeControlActionsByAnalysis } from "@http/Step3/UnsafeControlActions";
import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiX } from "react-icons/bi";

interface ModalSelectUCAsProps extends ModalProps {
	analysisId: string;
	ucas: SelectOption[];
	onChange: (ucas: SelectOption[]) => void;
	options?: SelectOption[];
	multiple?: boolean;
}
function ModalSelectUCAs({
	open,
	onClose,
	analysisId,
	ucas,
	onChange,
	options,
	multiple = false
}: ModalSelectUCAsProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle UCAs Options

	const [ucasOptions, setUCAsOptions] = useState<SelectOption[]>(options || []);
	const { data: ucasList, isLoading } = useQuery({
		queryKey: ["ucas-select", analysisId],
		queryFn: () => getUnsafeControlActionsByAnalysis(analysisId),
		enabled: (!options || options.length === 0) && open
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * UCAs to Select Options

	const ucasToSelectOptions = (ucas: IUnsafeControlActionReadDto[]): SelectOption[] => {
		return ucas.map(uca => ({
			label: `${uca.uca_code}: ${uca.name}`,
			value: uca.id
		}));
	};
	useEffect(() => {
		if (ucasList) {
			setUCAsOptions(ucasToSelectOptions(ucasList));
		}
	}, [ucasList]);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<ModalContainer open={open} size="big">
				<ModalHeader
					title={
						multiple
							? "Select the Unsafe Control Actions"
							: "Select the Unsafe Control Action"
					}
					onClose={onClose}
				/>
				{isLoading ? (
					<Loader />
				) : (
					<OptionsList
						options={ucasOptions}
						selectedOptions={ucas}
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

export default ModalSelectUCAs;
