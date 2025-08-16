import Container from "@components/Container";
import { ModalAnalysis } from "@components/Modal/ModalEntity";
import NoResultsMessage from "@components/NoResultsMessage";
import { useAuth } from "@hooks/useAuth";
import { createAnalysis, getAnalyses } from "@http/Analyses";
import { IAnalysisFormData, IAnalysisInsertDto } from "@interfaces/IAnalysis";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { Navigate } from "react-router-dom";
import { toast } from "sonner";
import styles from "./Analyses.module.css";
import AnalysisCard from "./AnalysisCard";

function Analyses() {
	const { user } = useAuth();
	if (!user) return <Navigate to="/logout" />;

	const [modalCreateAnalysisOpen, setModalCreateAnalysisOpen] = useState(false);
	const toggleModalCreateAnalysis = () => setModalCreateAnalysisOpen(!modalCreateAnalysisOpen);

	const queryClient = useQueryClient();
	const { mutateAsync: requestCreateAnalysis, isPending } = useMutation({
		mutationFn: (analysis: IAnalysisInsertDto) => createAnalysis(analysis),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["user-analyses"] });
			toast.success("Analysis created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateAnalysis = async (analysisData: IAnalysisFormData) => {
		const analysis: IAnalysisInsertDto = {
			...analysisData,
			userId: user.id
		};
		await requestCreateAnalysis(analysis);
	};

	const {
		data: analyses,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["user-analyses"],
		queryFn: () => getAnalyses(user.id)
	});

	return (
		<>
			<Container
				title="Analyses"
				onClick={toggleModalCreateAnalysis}
				isLoading={isLoading}
				isError={isError || analyses === undefined}
			>
				{analyses && analyses.length > 0 ? (
					<div className={styles.analyses_container}>
						{analyses!.map(analysis => (
							<AnalysisCard key={analysis.id} analysis={analysis} />
						))}
					</div>
				) : (
					<NoResultsMessage message="No analyses found. Create your first analysis." />
				)}
			</Container>
			<ModalAnalysis
				open={modalCreateAnalysisOpen}
				onClose={toggleModalCreateAnalysis}
				onSubmit={handleCreateAnalysis}
				isLoading={isPending}
				title="New Analysis"
				btnText="Create"
			/>
		</>
	);
}

export default Analyses;
