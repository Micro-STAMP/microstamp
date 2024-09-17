import Button from "@components/Button";
import Loader from "@components/Loader";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { handleDownloadPdf } from "@services/pdf";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiX as CloseIcon, BiDownload as DownloadIcon } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./ModalPDFPreview.module.css";

interface ModalPDFPreviewProps extends ModalProps {
	fetchPDF: (id: string) => Promise<Blob>;
	analysisId: string;
	title: string;
}

function ModalPDFPreview({ open, onClose, fetchPDF, analysisId, title }: ModalPDFPreviewProps) {
	const queryClient = useQueryClient();

	const [pdfUrl, setPdfUrl] = useState<string | undefined>(undefined);

	const {
		data: pdfBlob,
		isLoading,
		isSuccess,
		isError
	} = useQuery({
		queryKey: [`pdf-preview-${analysisId}`],
		queryFn: () => fetchPDF(analysisId),
		enabled: open,
		refetchOnWindowFocus: false,
		retry: false
	});

	useEffect(() => {
		if (open) {
			if (isSuccess && pdfBlob) {
				if (pdfUrl) {
					window.URL.revokeObjectURL(pdfUrl);
				}
				const blob = new Blob([pdfBlob], { type: "application/pdf" });
				const url = window.URL.createObjectURL(blob);
				setPdfUrl(url);
			}
		} else {
			if (pdfUrl) {
				window.URL.revokeObjectURL(pdfUrl);
				setPdfUrl(undefined);
			}
			queryClient.invalidateQueries({ queryKey: [`pdf-preview-${analysisId}`] });
			queryClient.resetQueries({ queryKey: [`pdf-preview-${analysisId}`] });
		}
		return () => {
			if (pdfUrl) {
				window.URL.revokeObjectURL(pdfUrl);
			}
		};
	}, [isSuccess, pdfBlob, open]);

	useEffect(() => {
		if (!open && pdfUrl) {
			window.URL.revokeObjectURL(pdfUrl);
			setPdfUrl(undefined);
		}
	}, [open]);

	useEffect(() => {
		if (isError) {
			toast.error("Failed to load PDF");
		}
	}, [isError]);

	const handleDownload = () => {
		if (pdfBlob && isSuccess) {
			handleDownloadPdf(pdfBlob, "analysis");
		}
	};

	return (
		<ModalContainer open={open} size="large">
			<ModalHeader title={title} onClose={onClose} />
			{isLoading || isError ? (
				isLoading ? (
					<Loader />
				) : (
					<div className={styles.error_message}>Error loading PDF</div>
				)
			) : (
				<embed src={pdfUrl} type="application/pdf" className={styles.frame} />
			)}
			<ModalButtons>
				<Button
					size="small"
					variant="dark"
					iconPosition="left"
					icon={CloseIcon}
					onClick={onClose}
				>
					Close
				</Button>
				<Button
					size="small"
					onClick={handleDownload}
					icon={DownloadIcon}
					isLoading={isLoading}
					disabled={isError}
				>
					Download
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalPDFPreview;
