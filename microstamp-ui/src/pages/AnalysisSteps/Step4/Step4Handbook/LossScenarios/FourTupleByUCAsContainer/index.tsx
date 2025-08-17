import IconButton from "@components/Button/IconButton";
import Container from "@components/Container";
import { ListItem as FourTuple } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { SelectSearch } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import Loader from "@components/Loader";
import { ModalFourTupleDetails } from "@components/Modal/ModalEntity/ModalStep4";
import { ModalUCAsOptions } from "@components/Modal/ModalSelectOptions/Entities";
import NoResultsMessage from "@components/NoResultsMessage";
import { getUnsafeControlActionsByAnalysis } from "@http/Step3/UnsafeControlActions";
import { getFourTuplesByUCA } from "@http/Step4/FourTuple";
import { IFourTupleReadDto } from "@interfaces/IStep4";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiInfoCircle as InfoIcon } from "react-icons/bi";
import styles from "./FourTupleByUCAsContainer.module.css";

interface FourTupleByUCAsContainerProps {
	analysisId: string;
}
function FourTupleByUCAsContainer({ analysisId }: FourTupleByUCAsContainerProps) {
	const [selectedTuple, setSelectedTuple] = useState<IFourTupleReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Four Tuple Details

	const [modalFourTupleDetailsOpen, setModalFourTupleDetailsOpen] = useState(false);
	const toggleModalFourTupleDetails = () =>
		setModalFourTupleDetailsOpen(!modalFourTupleDetailsOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Modal UCAs Options

	const [modalUCAsOptionsOpen, setModalUCAsOptionsOpen] = useState(false);
	const toggleModalUCAsOptions = () => setModalUCAsOptionsOpen(!modalUCAsOptionsOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle UCAs Search

	const [selectedUCA, setSelectedUCA] = useState<SelectOption | null>(null);
	const { data: ucasList, isLoading: isLoadingUcas } = useQuery({
		queryKey: ["ucas-multi-select", analysisId],
		queryFn: () => getUnsafeControlActionsByAnalysis(analysisId)
	});

	useEffect(() => {
		if (ucasList && ucasList.length > 0 && !selectedUCA) {
			const firstUCA = {
				label: `${ucasList[0].uca_code}: ${ucasList[0].name}`,
				value: ucasList[0].id
			};
			setSelectedUCA(firstUCA);
		}
	}, [ucasList, selectedUCA]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Four Tuples By UCA

	const {
		data: ucaWithFourTuples,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["four-tuples-by-uca", selectedUCA, analysisId],
		queryFn: () => getFourTuplesByUCA(selectedUCA!.value),
		enabled: !!selectedUCA
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<Container
				title="Scenarios by Unsafe Control Action"
				justTitle
				isLoading={isLoadingUcas}
				isError={isError || ucasList === undefined}
			>
				{selectedUCA && ucasList && (
					<div className={styles.four_tuples_list}>
						<SelectSearch
							value={selectedUCA}
							onSearch={toggleModalUCAsOptions}
							disabled={isLoadingUcas || !ucasList}
						/>
						{isLoading ? (
							<Loader />
						) : (
							<>
								{ucaWithFourTuples && ucaWithFourTuples.fourTuples.length > 0 ? (
									<div key={ucaWithFourTuples.id} className={styles.uca_section}>
										<span className={styles.title}> Scenarios:</span>
										<ListWrapper>
											{ucaWithFourTuples.fourTuples.map(ft => (
												<FourTuple.Root key={ft.id}>
													<FourTuple.Name
														code={ft.code}
														name={ft.scenario}
													/>
													<FourTuple.Actions>
														<IconButton
															icon={InfoIcon}
															onClick={() => {
																setSelectedTuple(ft);
																toggleModalFourTupleDetails();
															}}
														/>
													</FourTuple.Actions>
												</FourTuple.Root>
											))}
										</ListWrapper>
									</div>
								) : (
									<NoResultsMessage message="No scenario identified for this UCA." />
								)}
							</>
						)}
					</div>
				)}
			</Container>
			{selectedTuple && (
				<ModalFourTupleDetails
					fourTuple={selectedTuple}
					open={modalFourTupleDetailsOpen}
					onClose={toggleModalFourTupleDetails}
				/>
			)}
			{selectedUCA && (
				<ModalUCAsOptions
					open={modalUCAsOptionsOpen}
					onClose={toggleModalUCAsOptions}
					analysisId={analysisId}
					ucas={[selectedUCA]}
					onChange={(ucas: SelectOption[]) => setSelectedUCA(ucas[0])}
					multiple={false}
				/>
			)}
		</>
	);
}

export default FourTupleByUCAsContainer;
