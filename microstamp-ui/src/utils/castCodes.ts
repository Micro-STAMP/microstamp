export function nextCode(prefix: string, existingCodes: (string | null | undefined)[]): string {
    const pattern = new RegExp(`^${prefix.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}-(\\d+)$`, 'i');
    const max = existingCodes.reduce((acc, code) => {
        const match = code?.trim().match(pattern);
        return match ? Math.max(acc, Number(match[1])) : acc;
    }, 0);
    return `${prefix}-${max + 1}`;
}
