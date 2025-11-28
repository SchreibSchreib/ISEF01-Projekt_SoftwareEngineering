import { reactive } from "vue";

const savedUser = localStorage.getItem("user");
const savedLoaded = localStorage.getItem("isLoaded") === "true";

export const userStore = reactive({
  currentUser: savedUser ? JSON.parse(savedUser) : null,
  isLoaded: savedLoaded,

  setUser(user) {
    this.currentUser = user;
    this.isLoaded = true;
    localStorage.setItem("user", JSON.stringify(user));
    localStorage.setItem("isLoaded", "true");
  },

  clear() {
    this.currentUser = null;
    this.isLoaded = false;
    localStorage.removeItem("user");
    localStorage.removeItem("isLoaded");
  },
});
