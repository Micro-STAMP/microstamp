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
}
function ModalUCAsOptions({ open, onClose, analysisId, ucas, onChange }: ModalUCAsOptionsProps) {
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

	const [ucasOptions, setucasOptions] = useState<SelectOption[]>([]);
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
			setucasOptions(ucasToSelectOptions(ucasList));
		}
	}, [ucasList]);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<ModalSelectOptions
				open={open && !isLoading && !isError}
				onClose={onClose}
				title={"Select the Unsafe Control Actions"}
				onChange={onChange}
				selectedOptions={ucas}
				options={ucasOptions}
				multiple
			/>
		</>
	);
}

export default ModalUCAsOptions;
