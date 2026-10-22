import { ReactNode, useLayoutEffect, useRef, useState } from 'react';
import { createPortal } from 'react-dom';
import styles from './LinkBadge.module.css';

interface Props {
    code: string;
    tooltip?: string;
    icon?: ReactNode;
}

export default function LinkBadge({ code, tooltip, icon }: Props) {
    const [hovered, setHovered] = useState(false);
    const badgeRef = useRef<HTMLSpanElement>(null);
    const [position, setPosition] = useState({ top: 0, left: 0 });

    useLayoutEffect(() => {
        if (hovered && badgeRef.current) {
            const rect = badgeRef.current.getBoundingClientRect();
            setPosition({
                top: rect.top,
                left: rect.left + rect.width / 2
            });
        }
    }, [hovered]);

    return (
        <span
            ref={badgeRef}
            className={`${styles.badge} ${tooltip ? styles.badgeWithTooltip : ''}`}
            onMouseEnter={() => setHovered(true)}
            onMouseLeave={() => setHovered(false)}
        >
            {icon}{code}
            {tooltip && hovered && createPortal(
                <div
                    className={styles.tooltip}
                    style={{ top: position.top, left: position.left }}
                >
                    {tooltip}
                </div>,
                document.body
            )}
        </span>
    );
}
