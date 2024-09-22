import styles from "./Checkbox.module.css";

interface CheckboxProps {
	label: string;
	checked: boolean;
	onChange: (checked: boolean) => void;
}
function Checkbox({ label, checked, onChange }: CheckboxProps) {
	const handleCheck = (e: React.ChangeEvent<HTMLInputElement>) => {
		onChange(e.target.checked);
	};
	return (
		<div className={styles.checkbox}>
			<label className={styles.label}>
				<input
					className={styles.input}
					type="checkbox"
					checked={checked}
					onChange={handleCheck}
				/>
				<span className={styles.check}>
					<svg>
						<use xlinkHref="#check"></use>
					</svg>
				</span>
				<span className={styles.name}>{label}</span>
			</label>
			<svg className={styles.svg}>
				<symbol id="check" viewBox="0 0 12 10">
					<polyline points="1.5 6 4.5 9 10.5 1"></polyline>
				</symbol>
			</svg>
		</div>
	);
}

export default Checkbox;
