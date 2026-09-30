import { createContext, useCallback, useContext, useRef } from 'react';
import type { CastSectionHandle } from './index';

interface CastSectionNavigatorContextValue {
    register: (key: string, handle: CastSectionHandle | null) => void;
    goTo: (key: string) => void;
}

const CastSectionNavigatorContext = createContext<CastSectionNavigatorContextValue | null>(null);

export function CastSectionNavigatorProvider({ children }: { children: React.ReactNode }) {
    const registry = useRef(new Map<string, CastSectionHandle>());

    const register = useCallback((key: string, handle: CastSectionHandle | null) => {
        if (handle) {
            registry.current.set(key, handle);
        } else {
            registry.current.delete(key);
        }
    }, []);

    const goTo = useCallback((key: string) => {
        registry.current.get(key)?.openAndScrollTo();
    }, []);

    return (
        <CastSectionNavigatorContext.Provider value={{ register, goTo }}>
            {children}
        </CastSectionNavigatorContext.Provider>
    );
}

export function useRegisterCastSection(key: string) {
    const ctx = useContext(CastSectionNavigatorContext);
    return useCallback((handle: CastSectionHandle | null) => {
        ctx?.register(key, handle);
    }, [ctx, key]);
}

export function useGoToCastSection() {
    const ctx = useContext(CastSectionNavigatorContext);
    return useCallback((key: string) => {
        ctx?.goTo(key);
    }, [ctx]);
}
