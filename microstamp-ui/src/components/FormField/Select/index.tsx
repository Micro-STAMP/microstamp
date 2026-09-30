import { SelectOption } from "@components/FormField/Templates";
import { useEffect, useRef, useState } from "react";
import { createPortal } from "react-dom";
import { BiChevronDown as ArrowDown, BiChevronUp as ArrowUp } from "react-icons/bi";
import styles from "./Select.module.css";

interface SelectProps {
	label: string;
	options: SelectOption[];
	value: SelectOption | null;
	onChange: (value: SelectOption | null) => void;
	optionsPosition?: "top" | "bottom";
	disabled?: boolean;
	required?: boolean;
}
function Select({
	label,
	options,
	value,
	onChange,
	optionsPosition = "bottom",
	disabled = false,
	required = false
}: SelectProps) {
	const [isOpen, setIsOpen] = useState(false);
	const buttonRef = useRef<HTMLButtonElement>(null);
	const optionsRef = useRef<HTMLUListElement>(null);
	const [coords, setCoords] = useState<{ top: number; left: number; width: number } | null>(null);

	useEffect(() => {
		if (required && !value) {
			onChange(options[0]);
			value = options[0];
		}
	}, [required, value, options]);

	useEffect(() => {
		if (!isOpen || !buttonRef.current) return;

		const updateCoords = () => {
			if (!buttonRef.current) return;
			const rect = buttonRef.current.getBoundingClientRect();
			setCoords({
				top: optionsPosition === "top" ? rect.top : rect.bottom,
				left: rect.left,
				width: rect.width
			});
		};

		updateCoords();

		window.addEventListener("scroll", updateCoords, true);
		window.addEventListener("resize", updateCoords);
		return () => {
			window.removeEventListener("scroll", updateCoords, true);
			window.removeEventListener("resize", updateCoords);
		};
	}, [isOpen, optionsPosition]);

	useEffect(() => {
		if (!isOpen) return;
		const handleClickOutside = (e: MouseEvent) => {
			const target = e.target as Node;
			if (buttonRef.current && buttonRef.current.contains(target)) return;
			if (optionsRef.current && optionsRef.current.contains(target)) return;
			setIsOpen(false);
		};
		document.addEventListener("mousedown", handleClickOutside);
		return () => document.removeEventListener("mousedown", handleClickOutside);
	}, [isOpen]);

	const options_class = `${styles.options} ${
		optionsPosition === "top" ? styles.options_top : ""
	}`;
	const select_class = `${styles.select} ${
		isOpen ? (optionsPosition === "top" ? styles.open_top : styles.open_bottom) : ""
	}`;

	const handleSelect = (op: SelectOption | null) => {
		onChange(op);
		setIsOpen(false);
	};

	return (
		<label className={styles.select_label}>
			<span className={styles.name}>{label}:</span>
			<div className={styles.select_container}>
				<button
					ref={buttonRef}
					type="button"
					onClick={() => setIsOpen(!isOpen)}
					className={select_class}
					disabled={disabled}
				>
					{value ? (
						<span>{value.label}</span>
					) : (
						<span style={{ color: "var(--color-muted-text)" }}>None</span>
					)}
					{isOpen ? (
						<ArrowUp className={styles.icon} />
					) : (
						<ArrowDown className={styles.icon} />
					)}
				</button>
				{isOpen && !disabled && coords && createPortal(
					<ul
						ref={optionsRef}
						className={options_class}
						style={{
							position: "fixed",
							top: optionsPosition === "top" ? undefined : coords.top,
							bottom: optionsPosition === "top" ? window.innerHeight - coords.top : undefined,
							left: coords.left,
							width: coords.width
						}}
					>
						{!required && (
							<li
								style={{ color: "var(--color-muted-text)" }}
								className={styles.option}
								onClick={() => handleSelect(null)}
							>
								None
							</li>
						)}
						{options.map(op => (
							<li
								className={styles.option}
								key={op.value}
								onClick={() => handleSelect(op)}
							>
								{op.label}
							</li>
						))}
					</ul>,
					document.body
				)}
			</div>
		</label>
	);
}

export default Select;
