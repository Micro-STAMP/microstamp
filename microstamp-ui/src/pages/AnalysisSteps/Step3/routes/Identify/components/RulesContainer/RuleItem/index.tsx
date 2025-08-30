import IconButton from "@components/Button/IconButton";
import { IRuleReadDto } from "@interfaces/IStep3";
import { ucaTypeToSelectOption } from "@interfaces/IStep3/IUnsafeControlAction/Enums";
import { BiSolidTrash as DeleteIcon } from "react-icons/bi";
import styles from "./RuleItem.module.css";

function RuleCell({ tag, value }: { tag: string; value: string }) {
	return (
		<div className={styles.cell}>
			<div className={styles.name}>{tag}</div>
			<div>{value}</div>
		</div>
	);
}

interface RuleItemProps {
	rule: IRuleReadDto;
	selectRule: (rule: IRuleReadDto) => void;
	modalDeleteRule: () => void;
	applyRule: (ruleId: string) => Promise<void>;
	isApplyingRule: boolean;
}
function RuleItem({ rule, selectRule, modalDeleteRule, applyRule, isApplyingRule }: RuleItemProps) {
	return (
		<div className={styles.rule_item} key={rule.id}>
			<RuleCell tag="Code" value={rule.code} />
			<RuleCell tag="Name" value={rule.name} />
			<RuleCell
				tag="Types"
				value={rule.types.map(t => ucaTypeToSelectOption(t).label).join(", ")}
			/>
			{rule.states.map(state => (
				<RuleCell tag={state.variable.name} value={state.name} key={state.id} />
			))}
			<RuleCell tag="Hazard" value={rule.hazard.name} />
			<IconButton
				icon={DeleteIcon}
				onClick={() => {
					selectRule(rule);
					modalDeleteRule();
				}}
			/>
			<button
				onClick={() => applyRule(rule.id)}
				disabled={isApplyingRule}
				className={styles.rule_apply_button}
			>
				Apply
			</button>
		</div>
	);
}

export default RuleItem;
