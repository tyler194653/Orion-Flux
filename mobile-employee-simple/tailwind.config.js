/** @type {import('tailwindcss').Config} */
const { colors } = require('./src/theme/colors');
const { typography } = require('./src/theme/typography');
const { spacing, borderRadius } = require('./src/theme/spacing');

module.exports = {
  content: ["./App.{js,jsx,ts,tsx}", "./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors,
      fontFamily: typography.fontFamily,
      fontSize: typography.fontSize,
      spacing,
      borderRadius,
    },
  },
  plugins: [],
}
