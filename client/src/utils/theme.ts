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

const adminTheme = {
  components: {
    Menu: {
      itemSelectedBg: "#0832de",
      itemSelectedColor: "white",
    },
  },
};

const profileTheme = {
  components: {
    Button: {
      defaultHoverBorderColor: "#bdbdbd",
      defaultHoverColor: "none",
      defaultHoverBg: "none",
    },
    Tabs: {
      itemActiveColor: "black",
      inkBarColor: "black",
      itemColor: "#a1a1a1",
      itemHoverColor: "black",
      itemSelectedColor: "black",
    },
  },
};

export { SignUpTheme, LoginTheme, adminTheme, globalTheme, profileTheme };
