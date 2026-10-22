import { useEffect, useRef, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { BiHistory, BiX } from 'react-icons/bi';
import { getAggregatedAuditLog } from '@http/CAST/AuditLog';
import { formatDateBR, formatDateTimeBR } from '@services/date';
import styles from './CastLastUpdated.module.css';

interface Props {
    analysisId: string;
}

export default function CastLastUpdated({ analysisId }: Props) {
    const [open, setOpen] = useState(false);
    const containerRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (!open) return;

        const handleClickOutside = (event: MouseEvent) => {
            if (containerRef.current && !containerRef.current.contains(event.target as Node)) {
                setOpen(false);
            }
        };

        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, [open]);

    const { data: entries } = useQuery({
        queryKey: ['cast-audit-log', analysisId],
        queryFn: () => getAggregatedAuditLog(analysisId),
        enabled: !!analysisId,
        refetchOnWindowFocus: false
    });

    const latest = entries && entries.length > 0 ? entries[0] : null;

    return (
        <div ref={containerRef} className={styles.container}>
            <button
                type="button"
                className={styles.trigger}
                onClick={() => setOpen(prev => !prev)}
                disabled={!latest}
            >
                <BiHistory size={16} />
                {latest
                    ? <>Última atualização: <strong>{formatDateBR(latest.timestamp)}</strong></>
                    : 'Nenhuma atualização registrada'}
            </button>

            {open && entries && entries.length > 0 && (
                <div className={styles.panel}>
                    <div className={styles.panelHeader}>
                        <span>Histórico de alterações</span>
                        <button type="button" className={styles.closeBtn} onClick={() => setOpen(false)}>
                            <BiX size={18} />
                        </button>
                    </div>
                    <div className={styles.panelList}>
                        {entries.slice(0, 20).map(entry => (
                            <div key={entry.id} className={styles.panelItem}>
                                <span className={styles.panelItemStep}>{entry.step}</span>
                                <span className={styles.panelItemField}>{entry.fieldName}</span>
                                <span className={styles.panelItemDate}>{formatDateTimeBR(entry.timestamp)}</span>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
}
