import { useEffect, useRef } from "react";
import {
	BiSolidXCircle as CloseIcon,
	BiFile as FileIcon,
	BiSolidFolderOpen as FolderIcon
} from "react-icons/bi";
import { toast } from "sonner";
import { FileType } from "./FileType";
import styles from "./InputFile.module.css";

interface InputFileProps {
	label: string;
	file: File | null;
	onChangeFile: (file: File | null) => void;
	fileType?: FileType;
	maxSizeMB?: number;
}

function InputFile({ label, file, onChangeFile, fileType, maxSizeMB }: InputFileProps) {
	const inputRef = useRef<HTMLInputElement | null>(null);

	const maxSize = maxSizeMB ? maxSizeMB * 1024 * 1024 : undefined;
	const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const selectedFile = e.target.files?.[0];
		if (selectedFile) {
			if (maxSize && selectedFile.size > maxSize) {
				toast.warning(`The selected file exceeds the maximum size of ${maxSizeMB}MB.`);
				onChangeFile(null);
				clearInput();
				return;
			}
			onChangeFile(selectedFile);
		}
	};

	const clearInput = () => {
		if (inputRef.current) {
			inputRef.current.value = "";
		}
		onChangeFile(null);
	};
	const handleClearFile = () => {
		clearInput();
	};

	useEffect(() => {
		if (!file) {
			clearInput();
		}
	}, [file]);

	const fileName = file ? file.name : null;
	const labelClass = file ? `${styles.label_file} ${styles.with_file}` : styles.label_file;
	return (
		<div className={styles.input_file_container}>
			<label className={labelClass}>
				{fileName && <FileIcon className={styles.icon} />}
				{!fileName && <FolderIcon className={styles.icon} />}
				<div className={styles.label_text}>{fileName || label}</div>
				<input
					ref={inputRef}
					className={styles.input_file}
					type="file"
					onChange={handleFileChange}
					accept={fileType}
				/>
			</label>
			{fileName && (
				<button className={styles.close_button} onClick={handleClearFile}>
					<CloseIcon />
				</button>
			)}
		</div>
	);
}

export default InputFile;
