import Button from "@components/Button";
import Container from "@components/Container";
import { InputFile } from "@components/FormField";
import { FileType } from "@components/FormField/InputFile/FileType";
import { ModalConfirm } from "@components/Modal";
import { addAnalysisImage, deleteAnalysisImage } from "@http/Analyses";
import { IImageReadDto } from "@interfaces/IImage";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import {
	BiTrash as DeleteIcon,
	BiImageAlt as ImageIcon,
	BiImageAdd as UploadIcon
} from "react-icons/bi";
import { toast } from "sonner";
import styles from "./AnalysisImage.module.css";

interface AnalysisImageProps {
	analysisId: string;
	image: IImageReadDto | null;
}

function AnalysisImage({ image, analysisId }: AnalysisImageProps) {
	const queryClient = useQueryClient();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Image File

	const [imageFile, setImageFile] = useState<File | null>(null);
	const [previewImage, setPreviewImage] = useState<string | null>(null);

	const handleFileChange = (file: File | null) => {
		setImageFile(file);
		if (file) {
			const reader = new FileReader();
			reader.onloadend = () => {
				setPreviewImage(reader.result as string);
			};
			reader.readAsDataURL(file);
		} else {
			setPreviewImage(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Add Analysis Image

	const { mutateAsync: requestSaveImage, isPending: isCreatingImage } = useMutation({
		mutationFn: (file: File) => addAnalysisImage(file, analysisId),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-display"] });
			toast.success("Image uploaded.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleSaveImage = async () => {
		if (!imageFile) {
			toast.warning("Please select an image.");
			return;
		}
		await requestSaveImage(imageFile);
		setImageFile(null);
		setPreviewImage(null);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Analysis Image

	const [modalDeleteImageOpen, setModalDeleteImageOpen] = useState(false);
	const toggleModalDeleteImage = () => setModalDeleteImageOpen(!modalDeleteImageOpen);

	const { mutateAsync: requestDeleteImage, isPending: isDeletingImage } = useMutation({
		mutationFn: () => deleteAnalysisImage(analysisId),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-display"] });
			toast.success("Image deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteImage = async () => {
		await requestDeleteImage();
		toggleModalDeleteImage();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	return (
		<>
			<Container title="Analysis Image" justTitle>
				<div className={styles.image_container}>
					{previewImage ? (
						<img className={styles.image} src={previewImage} alt="Preview" />
					) : image ? (
						<img
							className={styles.image}
							src={`data:image/png;base64,${image.base64}`}
							alt="Analysis Image"
						/>
					) : (
						<div className={styles.message}>
							<ImageIcon className={styles.icon} />
							<span className={styles.text}>No images have been added</span>
						</div>
					)}

					<footer className={styles.image_actions}>
						{!image && (
							<>
								<InputFile
									file={imageFile}
									label="Choose Image"
									onChangeFile={handleFileChange}
									fileType={FileType.Image}
								/>
								<Button
									size="small"
									icon={UploadIcon}
									onClick={handleSaveImage}
									isLoading={isCreatingImage}
								>
									Save
								</Button>
							</>
						)}
						{image && (
							<Button
								size="small"
								variant="dark"
								icon={DeleteIcon}
								onClick={toggleModalDeleteImage}
								isLoading={isDeletingImage}
							>
								Remove Image
							</Button>
						)}
					</footer>
				</div>
			</Container>
			<ModalConfirm
				open={modalDeleteImageOpen}
				onClose={toggleModalDeleteImage}
				onConfirm={handleDeleteImage}
				isLoading={isDeletingImage}
				message="Do you want to remove the image?"
				title="Delete Image"
				btnText="Delete"
			/>
		</>
	);
}

export default AnalysisImage;
