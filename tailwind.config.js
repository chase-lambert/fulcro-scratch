const { scanClojure } = require("@multiplyco/tailwind-clj");
module.exports = {
  content: {
    files: ["./src/scratch/**/*.cljs"],
    extract: {
      cljs: (content) => scanClojure(content),
    },
  },
  theme: {
    extend: {},
  },
  plugins: [],
};
