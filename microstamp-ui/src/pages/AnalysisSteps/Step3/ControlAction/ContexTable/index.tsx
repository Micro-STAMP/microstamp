import Loader from "@components/Loader";
import { ModalUnsafeControlAction } from "@components/Modal/ModalEntity";
import Pagination from "@components/Pagination";
import { createContextTable, getContextTable } from "@http/Step3/ContextTable";
import {
	createNotUnsafeContext,
	deleteNotUnsafeContext,
	getNotUnsafeContexts
} from "@http/Step3/NotUnsafeContexts";
import {
	createUnsafeControlAction,
	getUnsafeControlActions
} from "@http/Step3/UnsafeControlActions";
import { IControlAction } from "@interfaces/IStep2";
import {
	IContext,
	IContextTableInsertDto,
	INotUnsafeContextFormData,
	INotUnsafeContextInsertDto,
	IUCAType,
	IUnsafeControlActionFormData,
	IUnsafeControlActionInsertDto
} from "@interfaces/IStep3";
import { keepPreviousData, useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { toast } from "sonner";
import styles from "./ContextTable.module.css";
import TableHeader from "./TableHeader";
import TableRow from "./TableRow";

interface ContextTableProps {
	controlAction: IControlAction;
	analysisId: string;
}
function ContextTable({ controlAction, analysisId }: ContextTableProps) {
	const queryClient = useQueryClient();

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Variables List

	const sourceVariables = controlAction.connection.source.variables;
	const targetVariables = controlAction.connection.target.variables;
	const variables = sourceVariables.concat(targetVariables);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Context Table

	const [page, setPage] = useState(0);
	const {
		data: contextTable,
		refetch,
		isLoading,
		isError,
		error
	} = useQuery({
		queryKey: ["context-table", controlAction.id, page],
		queryFn: () => getContextTable(controlAction.id, page, variables),
		placeholderData: keepPreviousData,
		retry: false
	});
	const { mutateAsync: requestCreateContextTable, isPending } = useMutation({
		mutationFn: (table: IContextTableInsertDto) => createContextTable(table),
		onSuccess: () => {
			refetch();
		}
	});

	useEffect(() => {
		const handleContextTableNotFound = async () => {
			if (isError && error) {
				await requestCreateContextTable({
					control_action_id: controlAction.id
				});
			}
		};
		if (isError) handleContextTableNotFound();
	}, [isError, error, requestCreateContextTable]);

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Get Unsafe Control Actions List

	const {
		data: unsafeControlActions,
		isLoading: isLoadingUCAs,
		isError: isErrorUCAs
	} = useQuery({
		queryKey: ["context-table-unsafe-control-actions", controlAction.id],
		queryFn: () => getUnsafeControlActions(controlAction.id)
	});

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Not Unsafe Contexts

	const {
		data: notUnsafeContexts,
		isLoading: isLoadingNoUnsafe,
		isError: isErrorNotUnsafe
	} = useQuery({
		queryKey: ["context-table-not-unsafe-contexts", controlAction.id],
		queryFn: () => getNotUnsafeContexts(controlAction.id)
	});

	// Create
	const { mutateAsync: requestCreateNotUnsafeContext, isPending: isCreatingNotUnsafe } =
		useMutation({
			mutationFn: (nuc: INotUnsafeContextInsertDto) => createNotUnsafeContext(nuc),
			onSuccess: () => {
				queryClient.invalidateQueries({ queryKey: ["context-table-not-unsafe-contexts"] });
				toast.success("Not unsafe context created.");
			},
			onError: err => {
				toast.error(err.message);
			}
		});
	const handleCreateNotUnsafeContext = async (notUnsafeData: INotUnsafeContextFormData) => {
		if (!ucaContext || !ucaType) return;
		const notUnsafeContext: INotUnsafeContextInsertDto = {
			statesIds: notUnsafeData.states.map(st => st.value),
			type: notUnsafeData.type!.value as IUCAType,
			analysisId: analysisId,
			controlActionId: controlAction.id
		};
		await requestCreateNotUnsafeContext(notUnsafeContext);
	};

	// Delete
	const { mutateAsync: requestDeleteNotUnsafeContext } = useMutation({
		mutationFn: (id: string) => deleteNotUnsafeContext(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ["context-table-not-unsafe-contexts"] });
			toast.success("Not unsafe context deleted.");
		},
		onError: err => {
			toast.error(err.message);
		}
	});
	const handleDeleteNotUnsafeContext = async (id: string) => {
		const toastLoading = toast.loading("Deleting not unsafe context.");
		try {
			await requestDeleteNotUnsafeContext(id);
		} catch (err) {
		} finally {
			toast.dismiss(toastLoading);
		}
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Select UCA Context & Type to Creation

	const [ucaContext, setUcaContext] = useState<IContext | null>(null);
	const [ucaType, setUcaType] = useState<IUCAType | null>(null);

	const [modalCreateUCAOpen, setModalCreateUCAOpen] = useState(false);
	const toggleModalCreateUCA = () => setModalCreateUCAOpen(!modalCreateUCAOpen);

	const handleToggleModalCreateUca = (context: IContext, type: IUCAType) => {
		if (!modalCreateUCAOpen) {
			setUcaContext(context);
			setUcaType(type);
		} else {
			setUcaContext(null);
			setUcaType(null);
		}
		toggleModalCreateUCA();
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */
	// * Handle Create UCAs by Context Table

	const { mutateAsync: requestCreateUnsafeControlAction, isPending: isCreatingUCA } = useMutation(
		{
			mutationFn: (uca: IUnsafeControlActionInsertDto) => createUnsafeControlAction(uca),
			onSuccess: () => {
				queryClient.invalidateQueries({
					queryKey: ["context-table-unsafe-control-actions"]
				});
				queryClient.invalidateQueries({ queryKey: ["unsafe-control-actions"] });
				queryClient.invalidateQueries({ queryKey: ["ucas-multi-select"] });
				toast.success("Unsafe control action created.");
			},
			onError: err => {
				toast.error(err.message);
			}
		}
	);
	const handleCreateUnsafeControlAction = async (ucaData: IUnsafeControlActionFormData) => {
		if (!ucaContext || !ucaType) return;
		const uca: IUnsafeControlActionInsertDto = {
			states_ids: ucaData.states.map(st => st.value),
			hazard_id: ucaData.hazard!.value,
			type: ucaData.type!.value as IUCAType,
			analysis_id: analysisId,
			control_action_id: controlAction.id
		};
		await requestCreateUnsafeControlAction(uca);
	};

	/* - - - - - - - - - - - - - - - - - - - - - - */

	if (isLoadingNoUnsafe) return <Loader />;
	if (notUnsafeContexts === undefined || isErrorNotUnsafe) return <h1>Error</h1>;
	if (isLoadingUCAs) return <Loader />;
	if (unsafeControlActions === undefined || isErrorUCAs) return <h1>Error</h1>;
	if (isLoading || isPending) return <Loader />;
	if (contextTable === undefined || isError) return <h1>Error</h1>;
	return (
		<>
			<div className={styles.context_table_container}>
				<TableHeader variables={variables} />
				{contextTable.contexts.map(context => (
					<TableRow
						key={context.id}
						context={context}
						unsafeControlActions={unsafeControlActions}
						notUnsafeContexts={notUnsafeContexts}
						toggleModal={handleToggleModalCreateUca}
						deleteNotUnsafeContext={handleDeleteNotUnsafeContext}
					/>
				))}
			</div>
			{contextTable.contexts.length > 0 && contextTable.totalPages > 1 && (
				<Pagination page={page} changePage={setPage} pages={contextTable.totalPages} />
			)}
			{ucaContext && ucaType && (
				<ModalUnsafeControlAction
					open={modalCreateUCAOpen}
					onClose={() => handleToggleModalCreateUca(ucaContext, ucaType)}
					analysisId={analysisId}
					controlAction={controlAction}
					onSubmitUCA={handleCreateUnsafeControlAction}
					onSubmitNotUnsafeContext={handleCreateNotUnsafeContext}
					isLoading={isCreatingUCA || isCreatingNotUnsafe}
					context={ucaContext}
					type={ucaType}
				/>
			)}
		</>
	);
}

export default ContextTable;
