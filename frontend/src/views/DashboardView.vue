<template>
  <div class="container pt-5">
    <div>
      <h1 class="fw-bold">Hallo {{ userStore.currentUser?.name }}</h1>
    </div>

    <div class="row g-4 mt-2 align-items-start">
      <!-- LINKE SPALTE -->
      <div class="col-12 col-lg-6 d-flex px-4 flex-column gap-3">

        <AppBoxWithShadow class="text-center mt-4 mb-3 p-3">
          <p class="fs-4 m-0 fw-bold">Lernfortschritt</p>
          <p class="fs-2 fw-bold my-2">
            {{ progressPercent }}%
          </p>
          <p v-if="progressPercent === 100" class="text-success fw-bold m-0">
            Herzlichen Glückwunsch!
          </p>
          <p class="text-muted m-0">
            Du hast {{ progressPercent }}% erfolgreich beantwortet.
          </p>
        </AppBoxWithShadow>

        <AppBoxWithShadow class="text-center mb-3">
          <p class="fs-4 m-0">Der Fragenkatalog beinhaltet momentan:</p>
          <p class="fs-5 fw-bold my-1">{{ questionCount }} Fragen</p>
        </AppBoxWithShadow>

        <AppBoxWithShadow class="text-center">
          <p class="fs-4 m-0">Dein Score:</p>
          <p class="fs-5 fw-bold my-1">
            {{ currentUser?.userscore ?? 0 }} Punkte
          </p>
        </AppBoxWithShadow>

        <div class="d-flex flex-wrap gap-3">
          <DashboardButton to="/addquestion" class="mt-lg-4 w-100">
            Neue Fragen erstellen
          </DashboardButton>

          <DashboardButton to="/singleplayer" class="flex-even py-5">
            Neues Quiz starten
          </DashboardButton>

          <DashboardButton to="/join-team" class="flex-even py-5">
            Session beitreten
          </DashboardButton>
        </div>
      </div>

      <!-- RECHTE SPALTE -->
      <AppCard class="col-12 col-lg-6">
        <template #header>
          <div class="d-flex justify-content-center">
            <h4 class="m-2">Dein Lernteam</h4>
          </div>
        </template>

        <template #body>
          <p class="text-muted">Schau dir den Fortschritt deiner Gruppe an:</p>

          <div class="d-flex flex-column gap-3">
            <!-- Teammitglieder -->
            <AppBox v-for="member in teamUsers" :key="member.userId"
              class="rounded-4 ps-4 d-flex justify-content-between align-items-center shadow-sm p-1">
              <strong>{{ member.name }}</strong>
              <span class="badge text-dark fs-6">
                <AppBox class="border-black">
                  {{ member.userscore ?? 0 }} Punkte
                </AppBox>
              </span>
            </AppBox>

            <!-- Freie Slots -->
            <AppBox v-for="i in freeSlots" :key="'slot-' + i"
              class="rounded-4 ps-4 d-flex justify-content-between align-items-center shadow-sm p-2 bg-light text-muted">
              <strong>Freier Platz</strong>
              <span class="pe-2">
                <AppButton v-if="!showTeamCodeBox" class="p-3 rounded-4" @click="openTeamCodeBox">Teamcode anzeigen
                </AppButton>
                <AppButton v-if="showTeamCodeBox" class="p-3 rounded-4" @click="openTeamCodeBox">Teamcode verbergen
                </AppButton>
              </span>
            </AppBox>

            <AppBox v-if="showTeamCodeBox" class="pb-2">
              <div class="d-flex justify-content-center align-items-center gap-5">
                <h4 class="text-center m-0">Teamcode:</h4>
                <p class="fs-3 fw-bold text-center m-0">
                  {{ currentUser?.team?.joinCode }}
                </p>
              </div>


              <div class="pb-2">
                <p v-if="showCopyMessage" class="p-0">
                  Teamcode wurde kopiert!
                </p>
              </div>

            </AppBox>

            <!-- Zusammenfassung -->
            <AppBox class="fs-4 rounded-4 ps-4 d-flex flex-column align-items-start shadow-sm pe-1">
              <div class="d-flex w-100 justify-content-between">
                <strong>Durchschnittliche Punkte</strong>
                <span class="badge text-dark">
                  {{ averageTeamScore }} Punkte
                </span>
              </div>

              <div class="d-flex w-100 justify-content-between mt-2">
                <strong>Beantwortete Fragen</strong>
                <span class="badge text-dark">
                  {{ answeredQuestionsPercent }} %
                </span>
              </div>
            </AppBox>
          </div>
        </template>
      </AppCard>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { userStore } from "@/stores/userStore";
import AppBoxWithShadow from "@/components/base/AppBoxWithShadow.vue";
import AppCard from "@/components/base/AppCard.vue";
import DashboardButton from "@/components/base/DashboardButton.vue";
import AppBox from "@/components/base/AppBox.vue";
import AppButton from "@/components/base/AppButton.vue";

const currentUser = ref(null);
const questionCount = ref(0);

// Teamdaten
const teamUsers = computed(() => currentUser.value?.team?.users ?? []);
const MAX_TEAM_SIZE = 4;
const showTeamCodeBox = ref(false);
const showCopyMessage = ref(false);

const progressPercent = computed(() => {
  if (!currentUser.value || !questionCount.value) return 0;
  const POINTS_PER_QUESTION = 10;
  const answered = (currentUser.value.userscore ?? 0) / POINTS_PER_QUESTION;
  const percent = (answered / questionCount.value) * 100;

  return Math.min(100, Math.max(0, Math.round(percent)));
});

const freeSlots = computed(() => {
  return Math.max(0, MAX_TEAM_SIZE - teamUsers.value.length);
});

/**
 * Durchschnittliche Punkte im Team
 */
const averageTeamScore = computed(() => {
  if (!teamUsers.value.length) return 0;
  const sum = teamUsers.value.reduce(
    (acc, u) => acc + (u.userscore ?? u.userScore ?? 0),
    0
  );
  return sum === 0
    ? 0
    : (sum / teamUsers.value.length).toFixed(1).replace(".", ",");
});

/**
 * Durchschnittlich beantwortete Fragen im Team (in %)
 */
const answeredQuestionsPercent = computed(() => {
  if (!teamUsers.value.length || !questionCount.value) return 0;

  const POINTS_PER_QUESTION = 10;

  // alle beantworteten Fragen der Teammitglieder aufsummieren
  const totalAnswered = teamUsers.value.reduce((sum, user) => {
    const score =
      user.userscore ??
      user.userScore ??
      0;

    return sum + (score / POINTS_PER_QUESTION);
  }, 0);

  // Durchschnitt pro Teammitglied (die Logik ist so wie bei den Punkten)
  const avgAnswered = totalAnswered / teamUsers.value.length;

  // Berechnung in %
  const percent = (avgAnswered / questionCount.value) * 100;

  return percent.toFixed(1).replace(".", ",");
});

function openTeamCodeBox() {
  showTeamCodeBox.value = !showTeamCodeBox.value;
}

onMounted(async () => {
  try {
    // 1. User laden
    const userData = await fetch("/api/users/me", {
      credentials: "include",
    });

    if (userData.status === 401) {
      console.error("Nicht eingeloggt");
      return;
    }

    const user = await userData.json();
    currentUser.value = user;

    const userId = user.userId;

    // 2. Teamcode prüfen
    const hasValidTeam =
      user.team &&
      user.team.joinCode &&
      user.team.joinCode.length === 6;

    // 3. Wenn kein gültiges Team → Backend auto-fix (Team erstellen)
    if (!hasValidTeam) {
      console.warn("User hat kein gültiges Team → wird erzeugt...");

      const teamRes = await fetch(`/api/teams/checkOrCreate?userId=${userId}`, {
        method: "POST",
        credentials: "include",
      });

      const newTeam = await teamRes.json();
      currentUser.value.team = newTeam;
    }

    // 4. Fragen laden
    const questionData = await fetch("/api/questions", {
      credentials: "include",
    });
    const questions = await questionData.json();
    questionCount.value = questions.length;

  } catch (e) {
    console.error("Fehler beim Laden des Dashboards:", e);
  }
});

</script>

<style scoped></style>
