import Button from "@components/Button";
import Container from "@components/Container";
import Loader from "@components/Loader";
import {
	deleteControlStructureImage,
	getControlStructureImages,
	saveControlStructureImage
} from "@http/Step2/ControlStructureImage";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import {
	BiTrash as DeleteIcon,
	BiImageAlt as ImageIcon,
	BiImageAdd as UploadIcon
} from "react-icons/bi";

import { InputFile } from "@components/FormField";
import { FileType } from "@components/FormField/InputFile/FileType";
import { ModalConfirm } from "@components/Modal";
import { toast } from "sonner";
import styles from "./ControlStructureImage.module.css";

interface ControlStructureImageProps {
	analysisId: string;
}

function ControlStructureImage({ analysisId }: ControlStructureImageProps) {
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
	// * Handle Save Image

	const { mutateAsync: requestSaveImage, isPending: isCreating } = useMutation({
		mutationFn: (file: File) => saveControlStructureImage(file, analysisId),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["control-structure-image", analysisId] });
			toast.success("Image uploaded.");
			setImageFile(null);
			setPreviewImage(null);
		},
		onError: err => {
			toast.error(err.message);
		}
	});

	const handleSaveImage = async () => {
		if (!imageFile) {
			toast.warning("Please select an image before saving.");
			return;
		}
		await requestSaveImage(imageFile);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Image

	const [modalDeleteImageOpen, setModalDeleteImageOpen] = useState(false);
	const toggleModalDeleteImage = () => setModalDeleteImageOpen(!modalDeleteImageOpen);

	const { mutateAsync: requestDeleteImage, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteControlStructureImage(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["control-structure-image", analysisId] });
			toast.success("Image deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});

	const handleDeleteImage = async (id: string) => {
		await requestDeleteImage(id);
		toggleModalDeleteImage();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Show Image

	const {
		data: images,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["control-structure-image", analysisId],
		queryFn: () => getControlStructureImages(analysisId)
	});

	if (isLoading) return <Loader />;
	if (isError || images === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="Control Structure Image" justTitle>
				<div className={styles.image_container}>
					{previewImage ? (
						<img className={styles.image} src={previewImage} alt="Preview" />
					) : images.length > 0 ? (
						<img
							className={styles.image}
							src={`data:image/png;base64,${images[0].base64}`}
							alt="Control Structure Image"
						/>
					) : (
						<div className={styles.message}>
							<ImageIcon className={styles.icon} />
							<span className={styles.text}>No images have been added</span>
						</div>
					)}

					<footer className={styles.image_actions}>
						{images.length === 0 && (
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
									isLoading={isCreating}
								>
									Save
								</Button>
							</>
						)}
						{images.length > 0 && (
							<Button
								size="small"
								variant="dark"
								icon={DeleteIcon}
								onClick={toggleModalDeleteImage}
								isLoading={isDeleting}
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
				onConfirm={() => handleDeleteImage(images[0].id)}
				isLoading={isDeleting}
				message="Do you want to remove the image?"
				title="Delete Image"
				btnText="Delete"
			/>
		</>
	);
}

export default ControlStructureImage;
