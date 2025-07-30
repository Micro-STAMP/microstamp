import styles from "./Textarea.module.css";

interface TextareaProps {
	label: string;
	value: string;
	onChange: (value: string) => void;
	required?: boolean;
	rows?: number;
}

function Textarea({ label, value, onChange, required = false, rows = 4 }: TextareaProps) {
	const handleTextareaChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
		onChange(e.target.value);
	};

	return (
		<label className={styles.label}>
			<textarea
				required={required}
				className={styles.textarea}
				value={value}
				onChange={handleTextareaChange}
				spellCheck={true}
				autoComplete="off"
				rows={rows}
			/>
			<span className={styles.name}>{label}:</span>
		</label>
	);
}

export default Textarea;
