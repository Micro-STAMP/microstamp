import { IContext, IUCAType, IUnsafeControlActionReadDto } from "@interfaces/IStep3";
import styles from "@pages/AnalysisSteps/Step3/ControlAction/ContexTable/ContextTable.module.css";
import { memo, useCallback, useEffect, useState } from "react";

interface UnsafeButtonProps {
	unsafeControlActions: IUnsafeControlActionReadDto[];
	type: IUCAType;
	context: IContext;
	toggleModal: (context: IContext, type: IUCAType) => void;
}
function UnsafeButton({
	unsafeControlActions,
	type,
	context,

	toggleModal
}: UnsafeButtonProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle if context is included in existing UCA

	const [isUnsafe, setIsUnsafe] = useState(false);
	const [ucaRuleCode, setUcaRuleCode] = useState<string | null>(null);

	const isContextUnsafe = useCallback(
		(ucaStates: string[], contextStates: string[]) => {
			return ucaStates.every(ucaState => contextStates.includes(ucaState));
		},
		[context]
	);

	useEffect(() => {
		const contextStates = context.states.map(st => st.id);
		const checkForExistingUca = () => {
			for (const uca of unsafeControlActions) {
				const ucaStates = uca.states.map(st => st.id);
				if (uca.type === type && isContextUnsafe(ucaStates, contextStates)) {
					setIsUnsafe(true);
					setUcaRuleCode(uca.rule_code.length > 0 ? uca.rule_code : null);
					return;
				}
			}
			setIsUnsafe(false);
			setUcaRuleCode(null);
		};
		checkForExistingUca();
	}, [unsafeControlActions, type, context, isContextUnsafe]);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	const buttonClass = isUnsafe ? styles.unsafe_button : styles.not_unsafe_button;
	return (
		<>
			<button
				type="button"
				onClick={() => toggleModal(context, type)}
				className={buttonClass}
				disabled={isUnsafe}
			>
				{isUnsafe ? (
					ucaRuleCode ? (
						<span>{ucaRuleCode}</span>
					) : (
						<span>Unsafe</span>
					)
				) : (
					<span>Unsafe?</span>
				)}
			</button>
		</>
	);
}

export default memo(UnsafeButton);
