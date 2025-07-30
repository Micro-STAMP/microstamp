import IconButton from "@components/Button/IconButton";
import Container from "@components/Container";
import { ListItem as FourTuple } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import Loader from "@components/Loader";
import { ModalFourTupleDetails } from "@components/Modal/ModalEntity/ModalStep4";
import { getUnsafeControlActionsByAnalysis } from "@http/Step3/UnsafeControlActions";
import { getFourTuples } from "@http/Step4/FourTuple";
import { IFourTupleReadDto } from "@interfaces/IStep4";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
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
	// * Handle Get All Unsafe Control Actions

	const {
		data: ucas,
		isLoading: isLoadingUCAs,
		isError: isErrorUCAs
	} = useQuery({
		queryKey: ["all-unsafe-control-actions"],
		queryFn: () => getUnsafeControlActionsByAnalysis(analysisId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Four Tuples

	const {
		data: fourTuples,
		isLoading: isLoadingFourTuples,
		isError: isErrorFourTuples
	} = useQuery({
		queryKey: ["four-tuples", analysisId],
		queryFn: () => getFourTuples(analysisId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Group Four Tuples by UCA

	const groupFourTuplesByUCA = () => {
		if (!ucas || !fourTuples) return [];

		const ucaMap = new Map<string, string>();
		ucas.forEach(uca => {
			ucaMap.set(uca.id, uca.name);
		});

		const grouped = new Map<string, { ucaName: string; tuples: IFourTupleReadDto[] }>();
		fourTuples.forEach(tuple => {
			tuple.unsafeControlActionIds.forEach(ucaId => {
				if (!grouped.has(ucaId)) {
					grouped.set(ucaId, {
						ucaName: ucaMap.get(ucaId) || `UCA ${ucaId}`,
						tuples: []
					});
				}
				grouped.get(ucaId)?.tuples.push(tuple);
			});
		});

		return Array.from(grouped.entries()).map(([ucaId, group]) => ({
			ucaId,
			ucaName: group.ucaName,
			tuples: group.tuples
		}));
	};

	const groupedTuples = groupFourTuplesByUCA();

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingFourTuples || isLoadingUCAs) return <Loader />;
	if (isErrorFourTuples || fourTuples === undefined || isErrorUCAs || ucas === undefined)
		return <h1>Error</h1>;
	return (
		<>
			<Container title="Scenarios by Unsafe Control Actions" justTitle>
				<div className={styles.four_tuples_list}>
					{groupedTuples.map(group => (
						<div key={group.ucaId} className={styles.uca_section}>
							<span className={styles.title}> - {group.ucaName}</span>
							<ListWrapper>
								{group.tuples.map(ft => (
									<FourTuple.Root key={ft.id}>
										<FourTuple.Name code={ft.code} name={ft.scenario} />
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
					))}
				</div>
			</Container>
			{selectedTuple && (
				<ModalFourTupleDetails
					fourTuple={selectedTuple}
					open={modalFourTupleDetailsOpen}
					onClose={toggleModalFourTupleDetails}
				/>
			)}
		</>
	);
}

export default FourTupleByUCAsContainer;
