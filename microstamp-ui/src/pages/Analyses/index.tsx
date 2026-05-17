import { ModalAnalysis } from "@components/Modal/ModalEntity";
import NoResultsMessage from "@components/NoResultsMessage";
import { useAuth } from "@hooks/useAuth";
import { createAnalysis, getAnalyses, deleteAnalysis } from "@http/Analyses";
import { IAnalysisInsertDto, IAnalysisReadDto } from "@interfaces/IAnalysis";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState, useMemo } from "react";
import { Navigate } from "react-router-dom";
import { toast } from "sonner";
import { BiPlus } from "react-icons/bi";
import AnalysisCard from "./AnalysisCard";
import AnalysisFilters from "./AnalysisFilters";
import CastStepOne from "../AnalysisSteps/CAST/Step1";
import styles from "./Analyses.module.css"; 

interface IAnalysisExt extends IAnalysisReadDto {
    type?: "STPA" | "CAST";
}

function Analyses() {
    const { user } = useAuth();
    if (!user) return <Navigate to="/logout" />;

    const [modalCreateAnalysisOpen, setModalCreateAnalysisOpen] = useState(false);
    const toggleModalCreateAnalysis = () => setModalCreateAnalysisOpen(!modalCreateAnalysisOpen);
    const [activeFilter, setActiveFilter] = useState<"All" | "STPA" | "CAST">("All");
    const [searchQuery, setSearchQuery] = useState("");
    const [openedAnalysis, setOpenedAnalysis] = useState<IAnalysisExt | null>(null);

    const queryClient = useQueryClient();

    const { mutateAsync: requestCreateAnalysis, isPending } = useMutation({
        mutationFn: (analysis: IAnalysisInsertDto) => createAnalysis(analysis),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ["user-analyses"] });
            toast.success("Analysis created successfully!");
            const analysisType = (variables as any).type || "STPA";
            setOpenedAnalysis({
                id: data?.id || crypto.randomUUID(), 
                name: variables.name,
                description: variables.description,
                type: analysisType
            } as IAnalysisExt);
        },
        onError: err => toast.error(err.message)
    });

    const { mutateAsync: requestDeleteAnalysis } = useMutation({
        mutationFn: (id: string) => deleteAnalysis(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["user-analyses"] });
            toast.success("Analysis deleted successfully.");
        }
    });

    const handleCreateAnalysis = async (analysisData: any) => {
        const analysis: IAnalysisInsertDto = { ...analysisData, userId: user.id };
        await requestCreateAnalysis(analysis);
    };

    const handleDelete = async (id: string) => {
        if (window.confirm("Are you sure you want to delete this analysis?")) {
            await requestDeleteAnalysis(id);
        }
    };

    const { data: analysesRaw, isLoading, isError } = useQuery({
        queryKey: ["user-analyses"],
        queryFn: () => getAnalyses(user.id)
    });

    const counts = useMemo(() => {
        if (!analysesRaw) return { All: 0, STPA: 0, CAST: 0 };
        const mapped = analysesRaw.map(a => ({ ...a, type: (a as any).type || "STPA" }));
        return {
            All: mapped.length,
            STPA: mapped.filter(a => a.type === "STPA").length,
            CAST: mapped.filter(a => a.type === "CAST").length,
        };
    }, [analysesRaw]);

    const filteredAnalyses = useMemo(() => {
        if (!analysesRaw) return [];
        const analyses = analysesRaw.map(a => ({ ...a, type: (a as any).type || "STPA" })) as IAnalysisExt[];
        return analyses.filter(a => {
            const matchType = activeFilter === "All" || a.type === activeFilter;
            const matchSearch = a.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
                              a.description.toLowerCase().includes(searchQuery.toLowerCase());
            return matchType && matchSearch;
        });
    }, [analysesRaw, activeFilter, searchQuery]);

    if (openedAnalysis) {
        // if (openedAnalysis.type === "CAST") {
            return (
                <CastStepOne 
                    analysisId={openedAnalysis.id} 
                    analysisName={openedAnalysis.name} 
                    onBack={() => setOpenedAnalysis(null)} 
                />
            );
        // }
    }

    return (
        <div className={styles.pageContainer}>
            <div className={styles.pageHeader}>
                <div>
                    <h1 className={styles.pageTitle}>Analyses</h1>
                    <p className={styles.pageSubtitle}>Manage your STPA and CAST safety analyses</p>
                </div>
                <button onClick={toggleModalCreateAnalysis} className={styles.newAnalysisBtn}>
                    <BiPlus size={18} /> New Analysis
                </button>
            </div>

            {analysesRaw && analysesRaw.length > 0 && (
                <AnalysisFilters 
                    activeFilter={activeFilter} 
                    setActiveFilter={setActiveFilter}
                    searchQuery={searchQuery}
                    setSearchQuery={setSearchQuery}
                    counts={counts}
                />
            )}

            {isLoading ? (
                <div>Loading...</div>
            ) : isError ? (
                <div>Error loading analyses.</div>
            ) : filteredAnalyses && filteredAnalyses.length > 0 ? (
                <div className={styles.analyses_container}>
                    {filteredAnalyses.map(analysis => (
                        <AnalysisCard 
                            key={analysis.id} 
                            analysis={analysis} 
                            onOpen={() => setOpenedAnalysis(analysis)}
                            onEdit={() => alert(`Vamos editar a análise: ${analysis.name}`)}
                            onDelete={() => handleDelete(analysis.id)}
                        />
                    ))}
                </div>
            ) : (
                <NoResultsMessage message="No analyses found." />
            )}

            <ModalAnalysis
                open={modalCreateAnalysisOpen}
                onClose={toggleModalCreateAnalysis}
                onSubmit={handleCreateAnalysis}
                isLoading={isPending}
                title="New Analysis"
                btnText="Create"
            />
        </div>
    );
}

export default Analyses;