<template>
  <div class="container py-5">
    <h1 class="fw-bold mb-4">Team beitreten</h1>

    <AppBoxWithShadow>
      <div class="p-4">

        <!-- Fehlermeldung / Status -->
        <div v-if="error" class="alert alert-danger mb-3">
          {{ error }}
        </div>
        <div v-if="status && !error" class="alert alert-success mb-3">
          {{ status }}
        </div>

        <!-- Abschnitt: Team mit Code beitreten -->
        <section class="mb-5">
          <h3 class="mb-3">Gib deinen Teamcode ein</h3>
          <p class="text-muted small">
            Den Teamcode bekommst du von der Person, die das Team erstellt hat.
            Der Code entspricht aktuell dem Join-Code des Teams.
          </p>

          <div class="mb-3">
            <label class="form-label fw-semibold">Teamcode</label>
            <AppInput v-model="teamCode" placeholder="z. B. ABC123" />
          </div>

          <button
            class="btn btn-primary w-100 py-3 fw-semibold"
            :disabled="loadingJoin || !teamCode"
            @click="joinTeam"
          >
            <span v-if="loadingJoin">Tritt bei…</span>
            <span v-else>Team beitreten</span>
          </button>
        </section>

        <hr />

        <!-- Abschnitt: Neues Team erstellen -->
        <section class="mt-4">
          <h3 class="mb-3">Oder ein neues Team erstellen</h3>
          <p class="text-muted small">
            Wenn du noch kein Team hast, kannst du hier ein neues Team anlegen.
            Du erhältst danach einen Teamcode (Join-Code), den du mit anderen teilen kannst.
          </p>

          <div class="mb-3">
            <label class="form-label fw-semibold">Teamname</label>
            <AppInput
              v-model="teamName"
              placeholder="z. B. Team Alpha"
            />
          </div>

          <button
            class="btn btn-secondary w-100 py-3 fw-semibold mb-3"
            :disabled="loadingCreate || !userId"
            @click="createTeam"
          >
            <span v-if="loadingCreate">Team wird erstellt…</span>
            <span v-else>Neues Team & Teamcode erzeugen</span>
          </button>

          <div v-if="createdTeamCode" class="mt-2">
            <p class="small mb-1">
              <strong>Dein neuer Teamcode:</strong>
            </p>
            <div class="session-code-box d-inline-block rounded-4 px-3 py-2">
              {{ createdTeamCode }}
            </div>
            <p class="small text-muted mt-2">
              Teile diesen Code mit deinen Mitspielern – sie können damit deinem Team beitreten.
            </p>
          </div>
        </section>
      </div>
    </AppBoxWithShadow>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import AppBoxWithShadow from "@/components/base/AppBoxWithShadow.vue";
import AppInput from "@/components/base/AppInput.vue";

const router = useRouter();

const teamCode = ref("");
const teamName = ref("");
const createdTeamCode = ref("");

const status = ref("");
const error = ref("");

const loadingJoin = ref(false);
const loadingCreate = ref(false);

const userId = ref(null);

// Eingeloggten User laden, um creatorUserId / join userId zu kennen
onMounted(async () => {
  try {
    const res = await fetch("/api/session/check", {
      credentials: "include",
    });

    if (!res.ok) {
      error.value = "Du bist nicht eingeloggt.";
      return;
    }

    const data = await res.json();
    
    userId.value = data.id ?? data.userId ?? data.userID;

    if (!userId.value) {
      console.warn("Konnte userId aus /api/session/check nicht ermitteln:", data);
      error.value = "Benutzerinformation konnte nicht geladen werden.";
    }
  } catch (e) {
    console.error(e);
    error.value = "Fehler beim Laden des Benutzers.";
  }
});

async function joinTeam() {
  error.value = "";
  status.value = "";
  createdTeamCode.value = "";

  if (!userId.value) {
    error.value = "Du bist nicht eingeloggt.";
    return;
  }

  const code = teamCode.value.trim();
  if (!code) {
    error.value = "Bitte gib einen Teamcode ein.";
    return;
  }

  loadingJoin.value = true;
  try {
    const res = await fetch(
      `/api/teams/joinByCode?code=${encodeURIComponent(code)}&userId=${userId.value}`,
      {
        method: "POST",
        credentials: "include",
      }
    );

    if (!res.ok) {
      const text = await res.text();
      console.error("Fehler beim Team beitreten:", text);
      error.value = "Team konnte nicht gefunden werden oder Beitritt fehlgeschlagen.";
      return;
    }

    const team = await res.json();
    status.value = `Du bist dem Team "${team.name}" beigetreten.`;

    // Zur Team-Seite
    router.push({ name: "team" });
  } catch (e) {
    console.error(e);
    error.value = "Netzwerkfehler beim Beitreten des Teams.";
  } finally {
    loadingJoin.value = false;
  }
}

async function createTeam() {
  error.value = "";
  status.value = "";
  createdTeamCode.value = "";

  if (!userId.value) {
    error.value = "Du bist nicht eingeloggt.";
    return;
  }

  const name = teamName.value.trim() || "Mein Team";

  loadingCreate.value = true;
  try {
    const res = await fetch("/api/teams", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify({
        name,
        creatorUserId: userId.value,
      }),
    });

    if (!res.ok) {
      const text = await res.text();
      console.error("Fehler beim Erstellen des Teams:", text);
      error.value = "Team konnte nicht erstellt werden.";
      return;
    }

    const team = await res.json();

    const code = team.joinCode ?? team.teamId;
    createdTeamCode.value = code;
    status.value = `Team "${team.name}" wurde erstellt. Dein Teamcode ist ${code}.`;

    router.push({ name: "team" });
  } catch (e) {
    console.error(e);
    error.value = "Netzwerkfehler beim Erstellen des Teams.";
  } finally {
    loadingCreate.value = false;
  }
}
</script>

<style scoped>
.session-code-box {
  background-color: #173f4b;
  color: white;
  font-weight: 600;
  letter-spacing: 0.05em;
}
</style>
