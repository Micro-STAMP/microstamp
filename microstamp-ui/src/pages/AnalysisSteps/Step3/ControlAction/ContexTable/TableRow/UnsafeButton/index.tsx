import {
	IContext,
	INotUnsafeContextReadDto,
	IUCAType,
	IUnsafeControlActionReadDto
} from "@interfaces/IStep3";
import styles from "@pages/AnalysisSteps/Step3/ControlAction/ContexTable/ContextTable.module.css";
import { memo, useCallback, useEffect, useState } from "react";

interface UnsafeButtonProps {
	unsafeControlActions: IUnsafeControlActionReadDto[];
	notUnsafeContexts: INotUnsafeContextReadDto[];
	type: IUCAType;
	context: IContext;
	toggleModal: (context: IContext, type: IUCAType) => void;
}
function UnsafeButton({
	unsafeControlActions,
	notUnsafeContexts,
	type,
	context,
	toggleModal
}: UnsafeButtonProps) {
	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle if a list of states match a context

	const isContextMatching = useCallback(
		(ucaStates: string[], contextStates: string[]) => {
			return ucaStates.every(ucaState => contextStates.includes(ucaState));
		},
		[context]
	);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle if context is included in existing UCA

	const [isUnsafe, setIsUnsafe] = useState(false);
	const [ucaRuleCode, setUcaRuleCode] = useState<string | null>(null);

	const checkForExistingUca = (contextStates: string[]) => {
		for (const uca of unsafeControlActions) {
			const ucaStates = uca.states.map(st => st.id);
			if (uca.type === type && isContextMatching(ucaStates, contextStates)) {
				setIsUnsafe(true);
				setUcaRuleCode(uca.rule_code.length > 0 ? uca.rule_code : null);
				return;
			}
		}
		setIsUnsafe(false);
		setUcaRuleCode(null);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle if context is included in existing Not Unsafe Context

	const [isNotUnsafe, setIsNotUnsafe] = useState(false);

	const checkForExistingNotUnsafeContexts = (contextStates: string[]) => {
		for (const notUnsafe of notUnsafeContexts) {
			const notUnsafeStates = notUnsafe.states.map(st => st.id);
			if (notUnsafe.type === type && isContextMatching(notUnsafeStates, contextStates)) {
				setIsNotUnsafe(true);
				return;
			}
		}
		setIsNotUnsafe(false);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Check for UCAs and Not Unsafe Contexts

	useEffect(() => {
		const contextStates = context.states.map(st => st.id);
		checkForExistingNotUnsafeContexts(contextStates);
		checkForExistingUca(contextStates);
	}, [unsafeControlActions, notUnsafeContexts, type, context, isContextMatching]);

	/* - - - - - - - - - - - - - - - - - - - - - - */

	const buttonClass = isUnsafe
		? styles.unsafe_button
		: isNotUnsafe
		? styles.not_unsafe_button
		: styles.default_button;
	return (
		<>
			<button
				type="button"
				onClick={() => toggleModal(context, type)}
				className={buttonClass}
				disabled={isUnsafe || isNotUnsafe}
			>
				{isUnsafe ? (
					ucaRuleCode ? (
						<span>{ucaRuleCode}</span>
					) : (
						<span>Unsafe</span>
					)
				) : isNotUnsafe ? (
					<span>Not Unsafe</span>
				) : (
					<span>Unsafe?</span>
				)}
			</button>
		</>
	);
}

export default memo(UnsafeButton);
