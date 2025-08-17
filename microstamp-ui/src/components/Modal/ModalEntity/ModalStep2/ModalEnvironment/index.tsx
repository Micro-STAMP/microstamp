import Button from "@components/Button";
import { Checkbox } from "@components/FormField";
import { ModalButtons, ModalContainer, ModalHeader, ModalProps } from "@components/Modal/Templates";
import { createEnvironment, deleteEnvironment } from "@http/Step2/Environment";
import { IComponentReadDto, IComponentType } from "@interfaces/IStep2";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { BiCheckDouble as CheckIcon, BiUndo as ReturnIcon } from "react-icons/bi";
import { toast } from "sonner";
import styles from "./ModalEnvironment.module.css";

interface ModalEnvironmentProps extends ModalProps {
	analysisId: string;
	components: IComponentReadDto[];
}
function ModalEnvironment({ open, onClose, components, analysisId }: ModalEnvironmentProps) {
	const queryClient = useQueryClient();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Environment Component Logic

	const environmentComponent = components.find(c => c.type === IComponentType.ENVIRONMENT);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Include Environment State

	const [includeEnvironment, setIncludeEnvironment] = useState<boolean>(!!environmentComponent);
	useEffect(() => {
		setIncludeEnvironment(!!environmentComponent);
	}, [environmentComponent]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Environment Requests

	// Add Environment
	const { mutateAsync: requestCreateEnvironment, isPending: isCreating } = useMutation({
		mutationFn: () => createEnvironment(analysisId),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-components"] });
			queryClient.invalidateQueries({ queryKey: ["components-select-options"] });
			queryClient.invalidateQueries({
				queryKey: ["control-action-components-select-options"]
			});
			toast.success("Environment added to analysis.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});

	// Remove Environment
	const { mutateAsync: requestDeleteEnvironment, isPending: isDeleting } = useMutation({
		mutationFn: (id: string) => deleteEnvironment(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["analysis-components"] });
			queryClient.invalidateQueries({ queryKey: ["components-select-options"] });
			queryClient.invalidateQueries({
				queryKey: ["control-action-components-select-options"]
			});
			toast.success("Environment removed from analysis.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Save Environment

	const handleSaveEnvironment = async () => {
		try {
			if (includeEnvironment && !environmentComponent) {
				await requestCreateEnvironment();
			} else if (!includeEnvironment && environmentComponent) {
				await requestDeleteEnvironment(environmentComponent.id);
			}
		} finally {
			onClose();
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	const isLoading = isCreating || isDeleting;
	return (
		<ModalContainer open={open}>
			<ModalHeader onClose={onClose} title="Environment" />
			<div className={styles.modal_content}>
				<span className={styles.message}>
					Do you want to include the <strong>environment</strong> in your analysis?
					Enabling this option allows you to create connections between the environment
					and other components.
				</span>
				<Checkbox
					label="Include environment?"
					checked={includeEnvironment}
					onChange={setIncludeEnvironment}
				/>
			</div>
			<ModalButtons>
				<Button variant="dark" onClick={onClose} size="small" icon={ReturnIcon}>
					Close
				</Button>
				<Button
					onClick={handleSaveEnvironment}
					isLoading={isLoading}
					size="small"
					icon={CheckIcon}
				>
					Save
				</Button>
			</ModalButtons>
		</ModalContainer>
	);
}

export default ModalEnvironment;
