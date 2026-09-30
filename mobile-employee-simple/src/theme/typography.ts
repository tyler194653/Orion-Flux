export const typography = {
  fontFamily: {
    regular: 'Inter-Regular',
    medium: 'Inter-Medium',
    semiBold: 'Inter-SemiBold',
    bold: 'Inter-Bold',
  },

  headlineLg: {
    fontSize: 30,
    lineHeight: 36,
    letterSpacing: -0.02 * 30, // Tracking -0.02em
    fontWeight: '700' as const,
  },
  headlineLgMobile: {
    fontSize: 24,
    lineHeight: 30,
    letterSpacing: -0.02 * 24,
    fontWeight: '700' as const,
  },
  headlineMd: {
    fontSize: 20,
    lineHeight: 26,
    letterSpacing: -0.015 * 20,
    fontWeight: '600' as const,
  },
  headlineSm: {
    fontSize: 18,
    lineHeight: 24,
    letterSpacing: -0.01 * 18,
    fontWeight: '600' as const,
  },

  bodyLg: {
    fontSize: 16,
    lineHeight: 24,
    letterSpacing: -0.005 * 16,
    fontWeight: '400' as const,
  },
  bodyMd: {
    fontSize: 14,
    lineHeight: 20,
    letterSpacing: 0,
    fontWeight: '400' as const,
  },
  bodySm: {
    fontSize: 13,
    lineHeight: 18,
    letterSpacing: 0.005 * 13,
    fontWeight: '400' as const,
  },

  labelLg: {
    fontSize: 14,
    lineHeight: 18,
    letterSpacing: 0.01 * 14,
    fontWeight: '600' as const,
  },
  labelMd: {
    fontSize: 12,
    lineHeight: 16,
    letterSpacing: 0.02 * 12,
    fontWeight: '600' as const,
  },
  labelSm: {
    fontSize: 11,
    lineHeight: 14,
    letterSpacing: 0.04 * 11,
    fontWeight: '700' as const,
    textTransform: 'uppercase' as const,
  },
};
