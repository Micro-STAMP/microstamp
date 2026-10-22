export function cleanList(items: string[] | null | undefined): string[] {
    return (items ?? []).map(item => item.trim()).filter(item => item.length > 0);
}
