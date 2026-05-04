import Button from "@components/Button";
import { Input, Textarea } from "@components/FormField";
import {
    ModalButtons,
    ModalContainer,
    ModalHeader,
    ModalInputs,
    ModalProps
} from "@components/Modal/Templates";
import { IAnalysisFormData, IAnalysisReadDto } from "@interfaces/IAnalysis";
import { useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon, BiErrorAlt, BiTargetLock } from "react-icons/bi";
import { toast } from "sonner";

interface IAnalysisFormExt extends IAnalysisFormData {
    type?: "STPA" | "CAST";
}

interface ModalAnalysisProps extends ModalProps {
    onSubmit: (analysis: IAnalysisFormExt) => Promise<void>;
    title: string;
    isLoading?: boolean;
    analysis?: IAnalysisReadDto & { type?: "STPA" | "CAST" };
    btnText?: string;
}

function ModalAnalysis({
    open,
    onClose,
    onSubmit,
    title,
    isLoading = false,
    analysis,
    btnText = "Confirm"
}: ModalAnalysisProps) {
    const [analysisData, setAnalysisData] = useState<IAnalysisFormExt>({
        name: analysis?.name || "",
        description: analysis?.description || "",
        type: analysis?.type || "STPA" 
    });

    const handleSubmitAnalysis = async () => {
        if (!analysisData.name || !analysisData.description) {
            toast.warning("A required field is empty.");
            return;
        }
        await onSubmit(analysisData);
        setAnalysisData({
            name: analysis ? analysisData.name : "",
            description: analysis ? analysisData.description : "",
            type: analysis ? analysisData.type : "STPA"
        });
        onClose();
    };

    return (
        <ModalContainer open={open}>
            <ModalHeader onClose={onClose} title={title} />
            <ModalInputs>
                
                <div style={{ display: "flex", gap: "10px", marginBottom: "10px" }}>
                    <div 
                        onClick={() => setAnalysisData({ ...analysisData, type: "STPA" })}
                        style={{
                            flex: 1, padding: "12px", borderRadius: "8px", cursor: "pointer",
                            border: analysisData.type === "STPA" ? "1px solid #3b82f6" : "1px solid #374151",
                            backgroundColor: analysisData.type === "STPA" ? "rgba(59, 130, 246, 0.1)" : "rgba(31, 41, 55, 0.5)",
                            display: "flex", alignItems: "center", gap: "8px", transition: "0.2s"
                        }}
                    >
                        <BiTargetLock size={20} color={analysisData.type === "STPA" ? "#60a5fa" : "#9ca3af"} />
                        <div>
                            <div style={{ fontSize: "14px", fontWeight: "bold", color: analysisData.type === "STPA" ? "#60a5fa" : "#d1d5db" }}>STPA</div>
                            <div style={{ fontSize: "10px", color: "#9ca3af" }}>Proactive hazard</div>
                        </div>
                    </div>

                    <div 
                        onClick={() => setAnalysisData({ ...analysisData, type: "CAST" })}
                        style={{
                            flex: 1, padding: "12px", borderRadius: "8px", cursor: "pointer",
                            border: analysisData.type === "CAST" ? "1px solid #f97316" : "1px solid #374151",
                            backgroundColor: analysisData.type === "CAST" ? "rgba(249, 115, 22, 0.1)" : "rgba(31, 41, 55, 0.5)",
                            display: "flex", alignItems: "center", gap: "8px", transition: "0.2s"
                        }}
                    >
                        <BiErrorAlt size={20} color={analysisData.type === "CAST" ? "#fb923c" : "#9ca3af"} />
                        <div>
                            <div style={{ fontSize: "14px", fontWeight: "bold", color: analysisData.type === "CAST" ? "#fb923c" : "#d1d5db" }}>CAST</div>
                            <div style={{ fontSize: "10px", color: "#9ca3af" }}>Reactive accident</div>
                        </div>
                    </div>
                </div>

                <Input
                    label="Name"
                    value={analysisData.name}
                    onChange={(value: string) => setAnalysisData({ ...analysisData, name: value })}
                    required
                />
                <Textarea
                    label="Description"
                    value={analysisData.description}
                    onChange={(value: string) =>
                        setAnalysisData({ ...analysisData, description: value })
                    }
                    required
                />
            </ModalInputs>
            <ModalButtons>
                <Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
                    Cancel
                </Button>
                <Button
                    onClick={handleSubmitAnalysis}
                    isLoading={isLoading}
                    size="small"
                    icon={CheckIcon}
                >
                    {btnText}
                </Button>
            </ModalButtons>
        </ModalContainer>
    );
}

export default ModalAnalysis;