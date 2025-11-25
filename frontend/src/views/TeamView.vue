<template>
  <div class="container py-4 team-page">
    <h2 class="fw-bold mb-4">Hallo StudentXY!</h2>

    <div v-if="loading" class="text-muted">Teamdaten werden geladen…</div>

    <!-- Kein Team -->
    <div v-else-if="!hasTeam" class="alert alert-info">
      Du bist aktuell noch in keinem Team.
      <br />
      Gehe auf
      <RouterLink to="/join-team">Team beitreten</RouterLink>
      und gib dort einen Teamcode ein oder erstelle ein neues Team.
    </div>

    <!-- Team vorhanden -->
    <div v-else class="row g-4">
      <!-- Team-Lobby -->
      <div class="col-lg-7">
        <AppBoxWithShadow>
          <div class="p-4">
            <h3 class="section-title mb-2 px-3 py-2">Team-Lobby</h3>

            <p class="small text-muted mt-2">
              Du möchtest einem anderen Team beitreten?
              <RouterLink
                to="/join-team"
                class="link-primary text-decoration-underline"
              >
                Team mit Code beitreten
              </RouterLink>
            </p>

            <!-- Sitzungscode -->
            <div class="mb-3 mt-3">
              <label class="form-label fw-semibold small-label">Sitzungscode:</label>
              <div class="session-code-box rounded-4 px-3 py-3">
                {{ sessionCode || "—" }}
              </div>
            </div>

            <!-- Teamname -->
            <div class="mb-3">
              <label class="form-label fw-semibold small-label">Teamname:</label>
              <AppInput v-model="teamName" />
            </div>

            <!-- Mitgliederliste -->
            <ul class="list-unstyled mt-4 mb-0">
              <li
                v-for="member in members"
                :key="member.id ?? member.name"
                class="member-row d-flex align-items-center justify-content-between py-2"
              >
                <div class="d-flex align-items-center gap-3">
                  <div
                    class="member-avatar rounded-circle d-flex align-items-center justify-content-center"
                  >
                    <span>{{ initials(member.name) }}</span>
                  </div>
                  <span class="member-name">{{ member.name }}</span>
                </div>
                <span class="member-points fw-semibold">
                  {{ member.points }} Punkte
                </span>
              </li>
            </ul>
          </div>
        </AppBoxWithShadow>
      </div>

      <!-- Team-Chat -->
      <div class="col-lg-5">
        <AppBoxWithShadow>
          <div class="p-4">
            <h3 class="section-title mb-4 px-3 py-2">Team-Chat</h3>

            <div class="chat-window mb-3">
              <div
                v-if="messages.length === 0"
                class="text-muted fst-italic small"
              >
                Noch keine Nachrichten…
              </div>
              <div
                v-else
                class="d-flex flex-column gap-2"
              >
                <div
                  v-for="(msg, index) in messages"
                  :key="msg.id ?? index"
                  class="chat-message p-2 rounded-3"
                >
                  <div class="small fw-semibold">{{ msg.author }}</div>
                  <div>{{ msg.text }}</div>
                </div>
              </div>
            </div>

            <div class="input-group mb-4">
              <input
                v-model="currentMessage"
                type="text"
                class="form-control border-2 rounded-start-4 py-3"
                placeholder="Nachricht"
                @keyup.enter="sendMessage"
              />
              <button
                class="btn btn-primary px-4 rounded-end-4 send-btn"
                type="button"
                @click="sendMessage"
              >
                ▶
              </button>
            </div>

            <div
              class="average-box d-flex align-items-center justify-content-between rounded-4 px-3 py-3"
            >
              <span class="fw-semibold">Durchschnittliche Punkte</span>
              <span class="fs-4 fw-bold">
                {{ averagePoints }}
              </span>
            </div>
          </div>
        </AppBoxWithShadow>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { RouterLink } from "vue-router";
import AppBoxWithShadow from "@/components/base/AppBoxWithShadow.vue";
import AppInput from "@/components/base/AppInput.vue";

const loading = ref(true);
const hasTeam = ref(false);
const currentUserId = ref(null);

const sessionCode = ref("");
const teamName = ref("");
const members = ref([]); // [{ id, name, points }]

// 🔹 neu: Team-ID merken für Chat-Requests
const teamId = ref(null);

// Chat
const messages = ref([]);
const currentMessage = ref("");

// Durchschnittspunkte
const averagePoints = computed(() => {
  if (members.value.length === 0) return "0,0";
  const sum = members.value.reduce((acc, m) => acc + (m.points ?? 0), 0);
  const avg = sum / members.value.length;
  return avg.toFixed(1).replace(".", ",");
});

async function initTeam() {
  loading.value = true;
  hasTeam.value = false;

  try {
    // statt /api/session/check + /api/teams direkt /api/users/me
    const userRes = await fetch("/api/users/me", {
      credentials: "include",
    });

    if (!userRes.ok) {
      console.error("Fehler bei /api/users/me", await userRes.text());
      loading.value = false;
      return;
    }

    const user = await userRes.json();
    currentUserId.value = user.userId ?? user.id ?? user.userID ?? null;

    // Team direkt vom User
    const team = user.team ?? null;

    if (!team) {
      hasTeam.value = false;
      loading.value = false;
      return;
    }

    hasTeam.value = true;
    mapTeamToState(team);

    // Chat-Nachrichten laden
    await loadMessages();
  } catch (err) {
    console.error("Fehler beim Laden des Teams", err);
  } finally {
    loading.value = false;
  }
}

function mapTeamToState(team) {
  // Team-ID für Chat merken
  teamId.value = team.teamId ?? team.id ?? null;

  teamName.value = team.name ?? "";
  sessionCode.value = team.joinCode ?? String(team.teamId ?? "");

  const users = team.users ?? [];
  members.value = users.map((u) => {
    const id = u.userId ?? u.id ?? null;
    const name = u.name ?? u.username ?? "Unbekannt";

    const points =
      u.userscore ??
      u.userScore ??
      u.user_score ??
      u.score ??
      u.points ??
      0;

    return { id, name, points };
  });
}

// 🔹 Nachrichten vom Backend laden
async function loadMessages() {
  if (!teamId.value) return;

  try {
    const res = await fetch(`/api/teams/${teamId.value}/messages`, {
      credentials: "include",
    });

    if (!res.ok) {
      console.error("Fehler beim Laden der Nachrichten", await res.text());
      return;
    }

    const data = await res.json();
    // Erwartet: [{ id, userId, authorName, content, createdAt }]
    messages.value = data.map((m) => ({
      id: m.id,
      author: m.userId === currentUserId.value ? "Du" : m.authorName,
      text: m.content,
      createdAt: m.createdAt,
    }));
  } catch (err) {
    console.error("Netzwerkfehler beim Laden der Nachrichten", err);
  }
}

// 🔹 Nachricht senden
async function sendMessage() {
  const text = currentMessage.value.trim();
  if (!text || !teamId.value || !currentUserId.value) return;

  try {
    const res = await fetch(`/api/teams/${teamId.value}/messages`, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        content: text,
        userId: currentUserId.value,
      }),
    });

    if (!res.ok) {
      console.error("Fehler beim Senden der Nachricht", await res.text());
      return;
    }

    currentMessage.value = "";

    const saved = await res.json();
    messages.value.push({
      id: saved.id,
      author: saved.userId === currentUserId.value ? "Du" : saved.authorName,
      text: saved.content,
      createdAt: saved.createdAt,
    });
  } catch (err) {
    console.error("Netzwerkfehler beim Senden der Nachricht", err);
  }
}

function initials(name) {
  if (!name) return "?";
  const parts = name.split(" ").filter(Boolean);
  if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
  return (
    parts[0].charAt(0).toUpperCase() +
    parts[1].charAt(0).toUpperCase()
  );
}

onMounted(initTeam);
</script>

<style scoped>
.team-page {
  background-color: #f4f7f8;
  min-height: 100vh;
}

/* Überschriften-Balken */
.section-title {
  background-color: #159daf;
  color: white;
  border-radius: 16px;
}

/* Labels */
.small-label {
  color: #3b5b69;
}

/* Sitzungscode */
.session-code-box {
  background-color: #173f4b;
  color: white;
  font-weight: 600;
}

/* Mitgliederliste */
.member-row + .member-row {
  border-top: 1px solid #e2e6ea;
}

.member-avatar {
  width: 40px;
  height: 40px;
  background-color: #1fa1a8;
  color: white;
  font-weight: 700;
  font-size: 0.9rem;
}

.member-name {
  font-weight: 500;
  color: #173f4b;
}

.member-points {
  color: #3b5b69;
}

/* Chat-Bereich */
.chat-window {
  min-height: 220px;
  max-height: 320px;
  overflow-y: auto;
  border: 2px solid #e2e6ea;
  border-radius: 16px;
  padding: 16px;
  background-color: #fdfefe;
}

.chat-message {
  background-color: #f3f9fa;
}

/* Durchschnittliche Punkte */
.average-box {
  background-color: #f3f9fa;
  border: 2px solid #e2e6ea;
}

/* Send-Button optisch passend */
.send-btn {
  background-color: #274957;
  border-color: #274957;
}
</style>
