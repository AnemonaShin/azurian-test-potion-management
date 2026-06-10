const LEGACY_MAP: Record<string, string> = {
  A: 'bi-shield-fill',
  B: 'bi-hammer',
  C: 'bi-flower1',
};

export function roleIconClass(icon: string | undefined | null): string {
  if (!icon) return '';
  if (icon.length === 1) return LEGACY_MAP[icon] ?? icon;
  if (!icon.startsWith('bi-')) return `bi-${icon}`;
  return icon;
}
