import { forwardRef, TextareaHTMLAttributes, useCallback, useLayoutEffect, useRef } from 'react';

type Props = TextareaHTMLAttributes<HTMLTextAreaElement>;

const CastTextarea = forwardRef<HTMLTextAreaElement, Props>(function CastTextarea({ style, onInput, ...props }, forwardedRef) {
    const innerRef = useRef<HTMLTextAreaElement | null>(null);

    const setRefs = useCallback((node: HTMLTextAreaElement | null) => {
        innerRef.current = node;
        if (typeof forwardedRef === 'function') forwardedRef(node);
        else if (forwardedRef) forwardedRef.current = node;
    }, [forwardedRef]);

    const fitContent = () => {
        const el = innerRef.current;
        if (!el) return;
        el.style.height = 'auto';
        el.style.height = `${el.scrollHeight + 2}px`;
    };

    useLayoutEffect(fitContent, [props.value]);

    return (
        <textarea
            ref={setRefs}
            {...props}
            onInput={e => { fitContent(); onInput?.(e); }}
            style={{ resize: 'vertical', overflow: 'hidden', ...style }}
        />
    );
});

export default CastTextarea;
