import Container from "@components/Container";
import Loader from "@components/Loader";
import { ModalAnalysis } from "@components/Modal/ModalEntity";
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

	if (isLoading) return <Loader />;
	if (isError || analyses === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="Analyses" onClick={toggleModalCreateAnalysis}>
				<div className={styles.analyses_container}>
					{analyses.map(analysis => (
						<AnalysisCard key={analysis.id} analysis={analysis} />
					))}
				</div>
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
