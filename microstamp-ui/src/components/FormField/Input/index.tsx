import styles from "./Input.module.css";

interface InputProps {
	label: string;
	value: string;
	onChange: (value: string) => void;
	required?: boolean;
	type?: React.HTMLInputTypeAttribute;
}
function Input({ label, value, onChange, required = false, type = "text" }: InputProps) {
	const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		onChange(e.target.value);
	};

	return (
		<label className={styles.label}>
			<input
				required={required}
				className={styles.input}
				type={type}
				value={value}
				onChange={handleInputChange}
			/>
			<span className={styles.name}>{label}:</span>
		</label>
	);
}

export default Input;
