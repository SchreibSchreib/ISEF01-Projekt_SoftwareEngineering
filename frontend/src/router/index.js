import { createRouter, createWebHistory } from "vue-router";
import LoginView from "@/views/LoginView.vue";
import DashboardView from "@/views/DashboardView.vue";
import QuizStartView from "@/views/QuizStartView.vue";
import SinglePlayerView from "@/views/SinglePlayerView.vue";
import QuestionsView from "@/views/QuestionsView.vue";
import AddQuestionView from "@/views/AddQuestionView.vue";
import TeamView from "@/views/TeamView.vue";
import JoinTeamView from "@/views/JoinTeamView.vue"; 

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
      meta: { hideNavbar: false, requiresAuth: true },
    },
    {
      path: "/singleplayer",
      name: "singleplayer",
      component: SinglePlayerView,
      meta: { hideNavbar: false, requiresAuth: true },
    },
    {
      path: "/questions",
      name: "questions",
      component: QuestionsView,
      meta: { hideNavbar: false, requiresAuth: true },
    },
    {
      path: "/addquestion",
      name: "addquestion",
      component: AddQuestionView,
      meta: { hideNavbar: false, requiresAuth: true },
    },
    {
      path: "/team",
      name: "team",
      component: TeamView,
      meta: { hideNavbar: false, requiresAuth: true },
    },
    {
      path: "/join-team",
      name: "jointeam",                     
      component: JoinTeamView,
      meta: { hideNavbar: false, requiresAuth: true },
    },
    {
      path: "/:pathMatch(.*)*",
      redirect: "/",
    },
  ],
});

router.beforeEach(async (to, from, next) => {
  if (to.meta.requiresAuth) {
    try {
      const res = await fetch("/api/session/check", {
        credentials: "include",
      });

      if (res.status === 401) {
        return next({ name: "login" });
      }
    } catch (err) {
      return next({ name: "login" });
    }
  }
  next();
});

export default router;
