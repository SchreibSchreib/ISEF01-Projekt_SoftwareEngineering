import { reactive } from "vue";

export const userStore = reactive({
  currentUser: null,
  isLoaded: false
});
