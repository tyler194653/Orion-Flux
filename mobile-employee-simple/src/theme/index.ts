import { colors } from './colors';
import { typography } from './typography';
import { spacing, rounded, elevation } from './spacing';

export const theme = {
  colors,
  typography,
  spacing,
  rounded,
  elevation,
};

export type Theme = typeof theme;
