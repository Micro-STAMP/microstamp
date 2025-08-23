import Button from "@components/Button";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { useMemo, useState } from "react";
import { BiChevronLeft, BiChevronRight, BiX as CloseIcon } from "react-icons/bi";
import styles from "./ModalClassImages.module.css";

interface ModalClassImagesProps extends ModalProps {
	formalClass: "class1" | "class2" | "class3" | "class4";
}
const classOrder = ["class1", "class2", "class3", "class4"] as const;
function ModalClassImages({ open, onClose, formalClass }: ModalClassImagesProps) {
	const initialIndex = useMemo(() => classOrder.indexOf(formalClass), [formalClass]);
	const [currentIndex, setCurrentIndex] = useState(initialIndex);

	useMemo(() => setCurrentIndex(initialIndex), [open, initialIndex]);

	const handlePrev = () => {
		setCurrentIndex(prev => (prev === 0 ? classOrder.length - 1 : prev - 1));
	};
	const handleNext = () => {
		setCurrentIndex(prev => (prev === classOrder.length - 1 ? 0 : prev + 1));
	};

	const currentClass = classOrder[currentIndex];
	const imageSrc = `/assets/step4/classes/${currentClass}.png`;
	const imageAlt = `Image of ${currentClass}`;

	return (
		<ModalContainer open={open} size="normal">
			<ModalHeader title="Classes of Formal Scenarios" onClose={onClose} />
			<div className={styles.slider_container}>
				<button
					className={styles.nav_button}
					onClick={handlePrev}
					aria-label="Previous class"
				>
					<BiChevronLeft />
				</button>
				<div className={styles.image_wrapper}>
					<img
						src={imageSrc}
						alt={imageAlt}
						className={styles.class_image}
						draggable={false}
					/>
				</div>
				<button className={styles.nav_button} onClick={handleNext} aria-label="Next class">
					<BiChevronRight />
				</button>
			</div>
			<div className={styles.ref}>
				<span>Images created after Thomas (2024):</span>
				<a
					href="https://youtu.be/hp-KBjIBmrI?si=Pm0RPVvMZX-KuNml"
					target="_blank"
					rel="noopener noreferrer"
				>
					John Thomas - STPA: Formally Developing Loss Scenarios
				</a>
			</div>
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
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalClassImages;
