import { roleIconClass } from './role-icons';

describe('roleIconClass', () => {
  it('returns empty string for null', () => {
    expect(roleIconClass(null)).toBe('');
  });

  it('returns empty string for undefined', () => {
    expect(roleIconClass(undefined)).toBe('');
  });

  it('maps legacy single-letter A to bi-shield-fill', () => {
    expect(roleIconClass('A')).toBe('bi-shield-fill');
  });

  it('maps legacy single-letter B to bi-hammer', () => {
    expect(roleIconClass('B')).toBe('bi-hammer');
  });

  it('maps legacy single-letter C to bi-flower1', () => {
    expect(roleIconClass('C')).toBe('bi-flower1');
  });

  it('passes through unknown single letter', () => {
    expect(roleIconClass('X')).toBe('X');
  });

  it('prepends bi- when missing', () => {
    expect(roleIconClass('shield-fill')).toBe('bi-shield-fill');
  });

  it('passes through when bi- prefix present', () => {
    expect(roleIconClass('bi-shield-fill')).toBe('bi-shield-fill');
  });
});
