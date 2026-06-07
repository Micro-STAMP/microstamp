import { ModalAnalysis } from "@components/Modal/ModalEntity";
import NoResultsMessage from "@components/NoResultsMessage";
import { useAuth } from "@hooks/useAuth";
import { createAnalysis, getAnalyses, deleteAnalysis } from "@http/Analyses";
import { IAnalysisInsertDto, IAnalysisReadDto } from "@interfaces/IAnalysis";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState, useMemo } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { BiPlus } from "react-icons/bi";
import AnalysisCard from "./AnalysisCard";
import AnalysisFilters from "./AnalysisFilters";
import styles from "./Analyses.module.css"; 

interface IAnalysisExt extends IAnalysisReadDto {
    type?: "STPA" | "CAST";
}

function Analyses() {
    const { user } = useAuth();
    const navigate = useNavigate();
    
    const [modalCreateAnalysisOpen, setModalCreateAnalysisOpen] = useState(false);
    const toggleModalCreateAnalysis = () => setModalCreateAnalysisOpen(!modalCreateAnalysisOpen);
    const [activeFilter, setActiveFilter] = useState<"All" | "STPA" | "CAST">("All");
    const [searchQuery, setSearchQuery] = useState("");

    const queryClient = useQueryClient();

    const { mutateAsync: requestCreateAnalysis, isPending } = useMutation({
        mutationFn: (analysis: IAnalysisInsertDto) => createAnalysis(analysis),
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ["user-analyses"] });
            toast.success("Analysis created successfully!");
            
            const analysisType = (variables as any).type || "STPA";
            if (analysisType === "CAST") {
                navigate(`/analyses/${data.id}/cast/step1`);
            } else {
                navigate(`/analyses/${data.id}`);
            }
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
        if (!user) return; 

        const finalDescription = analysisData.type === "CAST" 
            ? `[CAST] ${analysisData.description}` 
            : analysisData.description;

        const analysis: IAnalysisInsertDto = { 
            name: analysisData.name, 
            description: finalDescription,
            userId: user.id 
        };

        await requestCreateAnalysis({ ...analysis, type: analysisData.type } as any);
    };

    const handleDelete = async (id: string) => {
        if (window.confirm("Are you sure you want to delete this analysis?")) {
            await requestDeleteAnalysis(id);
        }
    };

    const { data: analysesRaw, isLoading, isError } = useQuery({
        queryKey: ["user-analyses", user?.id],
        queryFn: () => getAnalyses(user!.id), 
        enabled: !!user 
    });

    const processedAnalyses = useMemo(() => {
        if (!analysesRaw) return [];
        return analysesRaw.map(a => {
            const isCast = a.description.startsWith("[CAST] ");
            return {
                ...a,
                description: isCast ? a.description.replace("[CAST] ", "") : a.description,
                type: isCast ? "CAST" : "STPA"
            } as IAnalysisExt;
        });
    }, [analysesRaw]);

    const counts = useMemo(() => {
        return {
            All: processedAnalyses.length,
            STPA: processedAnalyses.filter(a => a.type === "STPA").length,
            CAST: processedAnalyses.filter(a => a.type === "CAST").length,
        };
    }, [processedAnalyses]);

    const filteredAnalyses = useMemo(() => {
        return processedAnalyses.filter(a => {
            const matchType = activeFilter === "All" || a.type === activeFilter;
            const matchSearch = a.name.toLowerCase().includes(searchQuery.toLowerCase()) || 
                              a.description.toLowerCase().includes(searchQuery.toLowerCase());
            return matchType && matchSearch;
        });
    }, [processedAnalyses, activeFilter, searchQuery]);

    if (!user) return <Navigate to="/logout" />;

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
                            onOpen={() => {
                                if (analysis.type === "CAST") {
                                    navigate(`/analyses/${analysis.id}/cast/step1`);
                                } else {
                                    navigate(`/analyses/${analysis.id}`);
                                }
                            }}
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