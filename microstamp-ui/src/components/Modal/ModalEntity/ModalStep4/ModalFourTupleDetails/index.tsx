import Button from "@components/Button";
import { ListItem as Detail } from "@components/Container/ListItem";
import ListWrapper from "@components/Container/ListWrapper";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { IFourTupleReadDto } from "@interfaces/IStep4";
import { truncateText } from "@services/text";
import { BiUndo as ReturnIcon } from "react-icons/bi";

interface ModalFourTupleDetailsProps extends ModalProps {
	fourTuple: IFourTupleReadDto;
	isLoading?: boolean;
	btnText?: string;
}
function ModalFourTupleDetails({ fourTuple, open, onClose }: ModalFourTupleDetailsProps) {
	return (
		<ModalContainer open={open} size="large">
			<ModalHeader onClose={onClose} title={"Loss Scenario Details"} />
			<ListWrapper>
				<Detail.Root>
					<Detail.Name code="Code" name={fourTuple.code} />
				</Detail.Root>
				<Detail.Root>
					<Detail.Name code="Scenario" name={fourTuple.scenario} />
				</Detail.Root>
				<Detail.Root>
					<Detail.Name
						code="Associated Causal Factor"
						name={fourTuple.associatedCausalFactor}
					/>
				</Detail.Root>
				<Detail.Root>
					<Detail.Name code="Recommendation" name={fourTuple.recommendation} />
				</Detail.Root>
				<Detail.Root>
					<Detail.Name code="Rationale" name={fourTuple.rationale} />
				</Detail.Root>
				{fourTuple.unsafeControlActions && fourTuple.unsafeControlActions.length > 0 && (
					<Detail.Root>
						<Detail.Name
							code="Unsafe Control Actions"
							name={fourTuple.unsafeControlActions
								.map(uca => `[${uca.uca_code}] ${truncateText(uca.name, 32)}`)
								.join(" / ")}
						/>
					</Detail.Root>
				)}
			</ListWrapper>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Close
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalFourTupleDetails;
