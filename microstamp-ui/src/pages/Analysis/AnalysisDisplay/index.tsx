import Button from "@components/Button";
import Container from "@components/Container";
import { ModalConfirm } from "@components/Modal";
import { ModalAnalysis } from "@components/Modal/ModalEntity";
import { deleteAnalysis, updateAnalysis } from "@http/Analyses";
import { IAnalysisFormData, IAnalysisReadDto, IAnalysisUpdateDto } from "@interfaces/IAnalysis";
import { formatDate } from "@services/date";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { BiTrash as DeleteIcon, BiSolidEdit as EditIcon } from "react-icons/bi";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import styles from "./AnalysisDisplay.module.css";

interface AnalysisDisplayProps {
	analysis: IAnalysisReadDto;
}
function AnalysisDisplay({ analysis }: AnalysisDisplayProps) {
	const navigate = useNavigate();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Update Analysis

	const [modalUpdateAnalysisOpen, setModalUpdateAnalysisOpen] = useState(false);
	const toggleModalUpdateAnalysis = () => setModalUpdateAnalysisOpen(!modalUpdateAnalysisOpen);

	const queryClient = useQueryClient();
	const { mutateAsync: requestUpdateAnalysis, isPending: isUpdating } = useMutation({
		mutationFn: (analysisDto: IAnalysisUpdateDto) => updateAnalysis(analysis.id, analysisDto),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["user-analyses"] });
			queryClient.invalidateQueries({ queryKey: ["analysis-display"] });
			toast.success("Analysis updated.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleUpdateAnalysis = async (analysisData: IAnalysisFormData) => {
		const analysis: IAnalysisUpdateDto = { ...analysisData };
		await requestUpdateAnalysis(analysis);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Analysis

	const [modalDeleteAnalysisOpen, setModalDeleteAnalysisOpen] = useState(false);
	const toggleModalDeleteAnalysis = () => setModalDeleteAnalysisOpen(!modalDeleteAnalysisOpen);

	const { mutateAsync: requestDeleteAnalysis, isPending: isDeleting } = useMutation({
		mutationFn: () => deleteAnalysis(analysis.id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["user-analyses"] });
			queryClient.invalidateQueries({ queryKey: ["analysis-display"] });
			toast.success("Analysis deleted.");
			navigate("/analyses");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteAnalysis = async () => {
		await requestDeleteAnalysis();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<Container title="Analysis" justTitle>
				<div className={styles.analysis_display}>
					<div className={styles.display}>
						<div className={styles.text}>
							<strong>Name: </strong>
							<span>{analysis.name}</span>
						</div>
						<div className={styles.text}>
							<strong>Description: </strong>
							<span>{analysis.description}</span>
						</div>
						<div className={styles.text}>
							<strong>Creation Date: </strong>
							<span>{formatDate(analysis.creationDate)}</span>
						</div>
					</div>
					<footer className={styles.analysis_actions}>
						<Button
							size="small"
							variant="dark"
							icon={EditIcon}
							onClick={toggleModalUpdateAnalysis}
						>
							Edit
						</Button>
						<Button
							size="small"
							variant="dark"
							icon={DeleteIcon}
							onClick={toggleModalDeleteAnalysis}
						>
							Delete
						</Button>
					</footer>
				</div>
			</Container>
			<ModalAnalysis
				open={modalUpdateAnalysisOpen}
				onClose={toggleModalUpdateAnalysis}
				title="Update Analysis"
				btnText="Update"
				onSubmit={handleUpdateAnalysis}
				isLoading={isUpdating}
				analysis={analysis}
			/>
			<ModalConfirm
				open={modalDeleteAnalysisOpen}
				onClose={toggleModalDeleteAnalysis}
				title="Delete Analysis"
				btnText="Delete"
				message="Do you want to delete this analysis?"
				onConfirm={handleDeleteAnalysis}
				isLoading={isDeleting}
			/>
		</>
	);
}

export default AnalysisDisplay;
