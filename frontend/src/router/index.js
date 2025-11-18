import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/LoginView.vue";
import DashboardView from "@/views/DashboardView.vue";
import QuizStartView from "@/views/QuizStartView.vue";
import SinglePlayerView from "@/views/SinglePlayerView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "login",
      component: LoginView,
      meta: { hideNavbar: true },
    },
    {
      path: "/dashboard",
      name: "dashboard",
      component: DashboardView,
      meta: { hideNavbar: false },
    },
    {
      path: "/startquiz",
      name: "startquiz",
      component: QuizStartView,
      meta: { hideNavbar: false },
    },
    {
      path: "/singleplayer",
      name: "singleplayer",
      component: SinglePlayerView,
      meta: { hideNavbar: false },
    },
    {
      path: "/:pathMatch(.*)*",
      redirect: "/",
    },
  ],
});

export default router;
