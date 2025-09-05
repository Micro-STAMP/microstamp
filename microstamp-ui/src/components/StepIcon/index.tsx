import { ISteps } from "@interfaces/ISteps";
import { GoGoal as Step1Icon } from "react-icons/go";
import { IoWarningOutline as Step3Icon } from "react-icons/io5";
import { PiTreeStructure as Step2Icon } from "react-icons/pi";
import { RiArrowGoBackLine as Step4Icon } from "react-icons/ri";
import {
	TbCircleDashedNumber1 as Number1Icon,
	TbCircleDashedNumber2 as Number2Icon,
	TbCircleDashedNumber3 as Number3Icon,
	TbCircleDashedNumber4 as Number4Icon
} from "react-icons/tb";

interface StepIconProps {
	step: ISteps;
	icon?: "icon" | "number";
	className?: string;
}
function StepIcon({ step, className, icon = "icon" }: StepIconProps) {
	return (
		<>
			{step === ISteps.STEP_1 &&
				(icon === "icon" ? (
					<Step1Icon className={className} />
				) : (
					<Number1Icon className={className} />
				))}
			{step === ISteps.STEP_2 &&
				(icon === "icon" ? (
					<Step2Icon style={{ rotate: "90deg" }} className={className} />
				) : (
					<Number2Icon className={className} />
				))}
			{step === ISteps.STEP_3 &&
				(icon === "icon" ? (
					<Step3Icon className={className} />
				) : (
					<Number3Icon className={className} />
				))}
			{step === ISteps.STEP_4 &&
				(icon === "icon" ? (
					<Step4Icon className={className} />
				) : (
					<Number4Icon className={className} />
				))}
		</>
	);
}

export default StepIcon;
