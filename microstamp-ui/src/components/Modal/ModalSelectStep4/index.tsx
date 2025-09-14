import Button from "@components/Button";
import { SelectSearch } from "@components/FormField";
import { SelectOption } from "@components/FormField/Templates";
import { ModalSelectUCAs } from "@components/Modal/ModalSelectOptions";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { useNavigate } from "react-router-dom";
import styles from "./ModalSelectStep4.module.css";

interface ModalSelectStep4Props extends ModalProps {
	analysisId: string;
	isUpdate?: boolean;
	onSelect?: () => void;
}
function ModalSelectStep4({
	open,
	onClose,
	onSelect,
	analysisId,
	isUpdate = false
}: ModalSelectStep4Props) {
	const navigate = useNavigate();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Step 4 Approach

	const [selectedApproach, setSelectedApproach] = useState<"handbook" | "formal" | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Modal UCAs Options

	const [selectedUCA, setSelectedUCA] = useState<SelectOption | null>(null);
	const [modalSelectUCAsOpen, setModalSelectUCAsOpen] = useState(false);
	const toggleModalSelectUCAs = () => setModalSelectUCAsOpen(!modalSelectUCAsOpen);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Proceed to Step 4 Analysis

	const handleProceed = () => {
		if (isUpdate && selectedUCA) {
			navigate(`/analyses/${analysisId}/formal-scenarios?uca=${selectedUCA.value}`, {
				replace: true
			});
			onClose();
			if (onSelect) onSelect();
		} else if (selectedApproach === "handbook") {
			navigate(`/analyses/${analysisId}/loss-scenarios`);
			onClose();
			if (onSelect) onSelect();
		} else if (selectedApproach === "formal" && selectedUCA) {
			navigate(`/analyses/${analysisId}/formal-scenarios?uca=${selectedUCA.value}`);
			onClose();
			if (onSelect) onSelect();
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<ModalContainer open={open} size="big">
				<ModalHeader
					title={isUpdate ? "Change UCA Under Analysis" : "Select Step 4 Approach"}
					onClose={onClose}
				/>
				<div className={styles.modal_select_step4}>
					{isUpdate ? (
						<>
							<span className={styles.subtitle}>
								Select another <strong>Unsafe Control Action (UCA)</strong> to
								analyze in the <strong>Formal Scenarios</strong> approach.
							</span>
							<SelectSearch value={selectedUCA} onSearch={toggleModalSelectUCAs} />
						</>
					) : (
						<>
							<span className={styles.subtitle}>
								You can choose between two analysis approaches for{" "}
								<strong>Identify Loss Scenarios</strong>: The traditional approach
								of the <strong>STPA Handbook</strong> or the approach with{" "}
								<strong>Formal Scenarios</strong>. Both can be accessed and switched
								between at any time during the analysis.
							</span>
							<div className={styles.options_grid}>
								<div
									className={`${styles.option_card} ${
										selectedApproach === "handbook" ? styles.selected : ""
									}`}
									onClick={() => setSelectedApproach("handbook")}
								>
									<span className={styles.title}>STPA Handbook</span>
									<span className={styles.content}>
										Traditional method for identifying loss scenarios.
									</span>
								</div>

								<div
									className={`${styles.option_card} ${
										selectedApproach === "formal" ? styles.selected : ""
									}`}
									onClick={() => setSelectedApproach("formal")}
								>
									<span className={styles.title}>Formal Scenarios (2024)</span>
									<span className={styles.content}>
										New formal scenario building approach to identify loss
										scenarios.
									</span>
									<div className={styles.reference}>
										<span>Reference Video:</span>
										<a
											href="https://youtu.be/hp-KBjIBmrI?si=Pm0RPVvMZX-KuNml"
											target="_blank"
											rel="noopener noreferrer"
											onClick={e => e.stopPropagation()}
										>
											John Thomas - STPA: Formally Developing Loss Scenarios
										</a>
									</div>
								</div>
							</div>
							{selectedApproach === "formal" && (
								<>
									<span className={styles.subtitle}>
										For use of the <strong>formal scenarios approach</strong>,
										please select a UCA to proceed:
									</span>
									<SelectSearch
										value={selectedUCA}
										onSearch={toggleModalSelectUCAs}
									/>
								</>
							)}
						</>
					)}
				</div>
				<ModalButtons>
					<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
						Cancel
					</Button>
					<Button
						size="small"
						icon={CheckIcon}
						onClick={handleProceed}
						disabled={
							isUpdate
								? !selectedUCA
								: !selectedApproach ||
								  (selectedApproach === "formal" && !selectedUCA)
						}
					>
						{isUpdate ? "Change Unsafe Control Action" : "Continue"}
					</Button>
				</ModalButtons>
			</ModalContainer>
			<ModalSelectUCAs
				open={modalSelectUCAsOpen}
				onClose={toggleModalSelectUCAs}
				analysisId={analysisId}
				ucas={selectedUCA ? [selectedUCA] : []}
				onChange={(ucas: SelectOption[]) => setSelectedUCA(ucas[0])}
				multiple={false}
			/>
		</>
	);
}

export default ModalSelectStep4;
