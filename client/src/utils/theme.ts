const globalTheme = {};
const SignUpTheme = {
  components: {
    Input: {
      hoverBorderColor: "#bdbdbd",
      activeBorderColor: "#bdbdbd",
      activeShadow: "0 0 0 2px rgba(255, 255, 255, 0.2)",
    },
    Form: {
      itemMarginBottom: 10,
    },
  },
};

const LoginTheme = {
  components: {
    Input: {
      hoverBorderColor: "#bdbdbd",
      activeBorderColor: "#bdbdbd",
      activeShadow: "0 0 0 2px rgba(255, 255, 255, 0.2)",
    },
    Form: {
      itemMarginBottom: 20,
    },
  },
};

export { SignUpTheme, LoginTheme, globalTheme };
