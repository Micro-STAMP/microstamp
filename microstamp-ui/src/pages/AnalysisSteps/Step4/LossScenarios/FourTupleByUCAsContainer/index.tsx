import IconButton from "@components/Button/IconButton";
import Container from "@components/Container";
import { ListItem as FourTuple } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import Loader from "@components/Loader";
import { ModalFourTupleDetails } from "@components/Modal/ModalEntity/ModalStep4";
import { getFourTuplesByUCA } from "@http/Step4/FourTuple";
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
	// * Handle List Four Tuples By UCA

	const {
		data: ucasWithFourTuples,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["four-tuples-by-uca", analysisId],
		queryFn: () => getFourTuplesByUCA(analysisId)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || ucasWithFourTuples === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="Scenarios by Unsafe Control Actions" justTitle>
				<div className={styles.four_tuples_list}>
					{ucasWithFourTuples
						.filter(uca => uca.fourTuples.length > 0)
						.map(uca => (
							<div key={uca.id} className={styles.uca_section}>
								<span className={styles.title}> - {uca.name}</span>
								<ListWrapper>
									{uca.fourTuples.map(ft => (
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
