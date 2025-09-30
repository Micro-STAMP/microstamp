import Button from "@components/Button";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import {
	getFormalClassKey,
	getFormalClassTitle,
	IFormalScenariosClass
} from "@interfaces/IStep4New/Enums";
import { useEffect, useMemo, useState } from "react";
import { BiChevronLeft, BiChevronRight, BiX as CloseIcon } from "react-icons/bi";
import styles from "./ModalFormalClassImages.module.css";

const classOrder = [
	IFormalScenariosClass.CLASS_1,
	IFormalScenariosClass.CLASS_2,
	IFormalScenariosClass.CLASS_3,
	IFormalScenariosClass.CLASS_4
] as const;

interface ModalFormalClassImagesProps extends ModalProps {
	formalClass: IFormalScenariosClass;
}
function ModalFormalClassImages({ open, onClose, formalClass }: ModalFormalClassImagesProps) {
	const [currentClass, setCurrentClass] = useState<IFormalScenariosClass>(formalClass);
	const initialIndex = useMemo(() => classOrder.indexOf(formalClass), [formalClass]);
	const [currentIndex, setCurrentIndex] = useState(initialIndex);

	useMemo(() => setCurrentIndex(initialIndex), [open, initialIndex, formalClass]);
	useEffect(() => {
		setCurrentClass(formalClass);
	}, [formalClass]);

	const handlePrev = () => {
		setCurrentIndex(prev => (prev === 0 ? classOrder.length - 1 : prev - 1));
		setCurrentClass(classOrder[currentIndex === 0 ? classOrder.length - 1 : currentIndex - 1]);
	};
	const handleNext = () => {
		setCurrentIndex(prev => (prev === classOrder.length - 1 ? 0 : prev + 1));
		setCurrentClass(classOrder[currentIndex === classOrder.length - 1 ? 0 : currentIndex + 1]);
	};

	const imageSrc = `/assets/step4/classes/${getFormalClassKey(currentClass)}.png`;
	const imageAlt = `Image of ${getFormalClassTitle(currentClass)}`;
	return (
		<ModalContainer open={open} size="normal">
			<ModalHeader title={getFormalClassTitle(currentClass)} onClose={onClose} />
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

export default ModalFormalClassImages;
