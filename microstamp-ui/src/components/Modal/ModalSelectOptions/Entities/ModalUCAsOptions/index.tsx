import { SelectOption } from "@components/FormField/Templates";
import { ModalSelectOptions } from "@components/Modal";
import { ModalProps } from "@components/Modal/Templates";
import { getUnsafeControlActionsByAnalysis } from "@http/Step3/UnsafeControlActions";
import { IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";

interface ModalUCAsOptionsProps extends ModalProps {
	analysisId: string;
	ucas: SelectOption[];
	onChange: (ucas: SelectOption[]) => void;
	multiple?: boolean;
}
function ModalUCAsOptions({
	open,
	onClose,
	analysisId,
	ucas,
	onChange,
	multiple = true
}: ModalUCAsOptionsProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * UCAs to Select Options

	const ucasToSelectOptions = (ucas: IUnsafeControlActionReadDto[]): SelectOption[] => {
		return ucas.map(uca => ({
			label: `${uca.uca_code}: ${uca.name}`,
			value: uca.id
		}));
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle UCAs Options

	const [ucasOptions, setUCAsOptions] = useState<SelectOption[]>([]);
	const {
		data: ucasList,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["ucas-multi-select", analysisId],
		queryFn: () => getUnsafeControlActionsByAnalysis(analysisId)
	});

	useEffect(() => {
		if (ucasList) {
			setUCAsOptions(ucasToSelectOptions(ucasList));
		}
	}, [ucasList]);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<ModalSelectOptions
				open={open && !isLoading && !isError}
				onClose={onClose}
				title={
					multiple
						? "Select the Unsafe Control Actions"
						: "Select the Unsafe Control Action"
				}
				onChange={onChange}
				selectedOptions={ucas}
				options={ucasOptions}
				multiple={multiple}
			/>
		</>
	);
}

export default ModalUCAsOptions;
