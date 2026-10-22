import { useCallback, useRef, useState } from 'react';
import ModalConfirm from '@components/Modal/ModalConfirm';

interface ConfirmRequest {
    title: string;
    message: string;
    btnText: string;
}

export function useCastConfirm() {
    const [request, setRequest] = useState<ConfirmRequest | null>(null);
    const resolver = useRef<((confirmed: boolean) => void) | null>(null);

    const close = (confirmed: boolean) => {
        resolver.current?.(confirmed);
        resolver.current = null;
        setRequest(null);
    };

    const confirmDelete = useCallback((itemLabel: string, extraMessage?: string) => {
        return new Promise<boolean>(resolve => {
            resolver.current = resolve;
            setRequest({
                title: 'Confirm deletion',
                message: `Are you sure you want to delete ${itemLabel}? This action cannot be undone.${extraMessage ? ` ${extraMessage}` : ''}`,
                btnText: 'Delete'
            });
        });
    }, []);

    const confirmDialog = (
        <ModalConfirm
            open={!!request}
            onClose={() => close(false)}
            onConfirm={() => close(true)}
            title={request?.title ?? ''}
            message={request?.message ?? ''}
            btnText={request?.btnText}
        />
    );

    return { confirmDelete, confirmDialog };
}
