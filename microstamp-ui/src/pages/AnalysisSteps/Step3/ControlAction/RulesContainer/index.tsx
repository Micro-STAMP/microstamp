import Container from "@components/Container";
import ListWrapper from "@components/Container/ListWrapper";
import Loader from "@components/Loader";
import { ModalConfirm } from "@components/Modal";
import { ModalRule } from "@components/Modal/ModalEntity";
import { createRule, deleteRule, getRules } from "@http/Step3/Rules";
import { createUnsafeControlActionsByRule } from "@http/Step3/UnsafeControlActions";
import { IControlAction } from "@interfaces/IStep2";
import { IRuleFormData, IRuleInsertDto, IRuleReadDto, IUCAType } from "@interfaces/IStep3";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";
import RuleItem from "./RuleItem";

interface RulesContainerProps {
	analysisId: string;
	controlAction: IControlAction;
}
function RulesContainer({ analysisId, controlAction }: RulesContainerProps) {
	const queryClient = useQueryClient();
	const [selectedRule, setSelectedRule] = useState<IRuleReadDto | null>(null);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Apply Rule on Context Table

	const { mutateAsync: requestCreateUCAsByRule, isPending: isCreatingUCAs } = useMutation({
		mutationFn: (ruleId: string) => createUnsafeControlActionsByRule(ruleId),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["unsafe-control-actions"] });
			queryClient.invalidateQueries({ queryKey: ["context-table-unsafe-control-actions"] });
			toast.success("Unsafe control actions created by rule.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateUCAsByRule = async (ruleId: string) => {
		const toastLoading = toast.loading("Applying rule on context table.");
		await requestCreateUCAsByRule(ruleId);
		toast.dismiss(toastLoading);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create Rule

	const [modalCreateRuleOpen, setModalCreateRuleOpen] = useState(false);
	const toggleModalCreateRule = () => setModalCreateRuleOpen(!modalCreateRuleOpen);

	const { mutateAsync: requestCreateRule, isPending: isCreating } = useMutation({
		mutationFn: (rule: IRuleInsertDto) => createRule(rule),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-rules"] });
			toast.success("Rule created.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleCreateRule = async (ruleData: IRuleFormData) => {
		const rule: IRuleInsertDto = {
			name: ruleData.name,
			types: ruleData.types.map(type => type.value) as IUCAType[],
			states_ids: ruleData.states.map(state => state.value),
			hazard_id: ruleData.hazard!.value,
			control_action_id: controlAction.id,
			analysis_id: analysisId
		};
		await requestCreateRule(rule);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Delete Rule

	const [modalDeleteRuleOpen, setModalDeleteRuleOpen] = useState(false);
	const toggleModalDeleteRule = () => setModalDeleteRuleOpen(!modalDeleteRuleOpen);

	const { mutateAsync: requestDeleteRule, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteRule(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-rules"] });
			toast.success("Rule deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteRule = async () => {
		if (selectedRule) {
			await requestDeleteRule(selectedRule.id);
			toggleModalDeleteRule();
			setSelectedRule(null);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle List Rules

	const {
		data: rules,
		isLoading,
		isError
	} = useQuery({
		queryKey: ["analysis-rules", controlAction],
		queryFn: () => getRules(controlAction.id)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoading) return <Loader />;
	if (isError || rules === undefined) return <h1>Error</h1>;
	return (
		<>
			<Container title="Rules" onClick={toggleModalCreateRule}>
				<ListWrapper>
					{rules.map(rule => (
						<RuleItem
							rule={rule}
							modalDeleteRule={toggleModalDeleteRule}
							selectRule={setSelectedRule}
							key={rule.id}
							applyRule={handleCreateUCAsByRule}
							isApplyingRule={isCreatingUCAs}
						/>
					))}
				</ListWrapper>
			</Container>
			<ModalRule
				open={modalCreateRuleOpen}
				onClose={toggleModalCreateRule}
				onSubmit={handleCreateRule}
				isLoading={isCreating}
				analysisId={analysisId}
				controlAction={controlAction}
			/>
			<ModalConfirm
				open={modalDeleteRuleOpen}
				onClose={toggleModalDeleteRule}
				onConfirm={handleDeleteRule}
				isLoading={isDeleting}
				message="Do you want to delete this rule?"
				title="Delete Rule"
				btnText="Delete"
			/>
		</>
	);
}

export default RulesContainer;
