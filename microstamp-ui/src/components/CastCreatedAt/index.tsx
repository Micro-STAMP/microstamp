import { useQuery } from '@tanstack/react-query';
import { BiCalendar } from 'react-icons/bi';
import { getAnalysis } from '@http/Analyses';
import { formatDateBR } from '@services/date';

interface Props {
    analysisId: string;
}

export default function CastCreatedAt({ analysisId }: Props) {
    const { data: analysis } = useQuery({
        queryKey: ['analysis-created-at', analysisId],
        queryFn: () => getAnalysis(analysisId),
        enabled: !!analysisId,
        staleTime: 5 * 60 * 1000
    });

    if (!analysis) return null;

    return (
        <span style={{ display: 'inline-flex', alignItems: 'center', gap: '6px', color: 'var(--color-light-gray)', fontSize: '13px' }}>
            <BiCalendar size={14} /> Criado em: <strong style={{ color: 'var(--color-white)', fontWeight: 600 }}>{formatDateBR(analysis.creationDate)}</strong>
        </span>
    );
}
