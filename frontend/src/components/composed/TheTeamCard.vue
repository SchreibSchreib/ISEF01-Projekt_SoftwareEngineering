<template>
  <div class="container py-4 team-page">
    <h2 class="fw-bold mb-4">Hallo StudentXY!</h2>

    <div class="row g-4">
      <!-- Linke Spalte: Team-Lobby -->
      <div class="col-lg-7">
        <AppBoxWithShadow>
          <h3 class="section-title mb-4 px-3 py-2">Team-Lobby</h3>

          <!-- Sitzungscode -->
          <div class="mb-3">
            <label class="form-label fw-semibold small-label">Sitzungscode:</label>
            <div class="session-code-box rounded-4 px-3 py-3">
              {{ sessionCode }}
            </div>
          </div>

          <!-- Teamname -->
          <div class="mb-3">
            <label class="form-label fw-semibold small-label">Teamname:</label>
            <AppInput v-model="teamName" />
          </div>

          <!-- Teammitglieder / Einladen -->
          <div class="mb-3">
            <label class="form-label fw-semibold small-label">Mitglied einladen:</label>
            <div class="d-flex flex-column flex-md-row gap-2">
              <AppInput v-model="newMemberName" />
              <AppButton
                class="fw-semibold rounded-4 flex-shrink-0 invite-btn"
                @click="addMember"
              >
                Mitglied einladen
              </AppButton>
            </div>
          </div>

          <!-- Mitgliederliste -->
          <ul class="list-unstyled mt-4 mb-0">
            <li
              v-for="member in members"
              :key="member.name"
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
        </AppBoxWithShadow>
      </div>

      <!-- Rechte Spalte: Team-Chat -->
      <div class="col-lg-5">
        <AppBoxWithShadow>
          <h3 class="section-title mb-4 px-3 py-2">Team-Chat</h3>

          <!-- Chatfenster -->
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
                :key="index"
                class="chat-message p-2 rounded-3"
              >
                <div class="small fw-semibold">{{ msg.author }}</div>
                <div>{{ msg.text }}</div>
              </div>
            </div>
          </div>

          <!-- Chat-Eingabe -->
          <div class="input-group mb-4">
            <!-- Hier normales input, weil AppInput aktuell keinen placeholder unterstützt -->
            <input
              v-model="currentMessage"
              type="text"
              class="form-control border-2 rounded-start-4 py-3"
              placeholder="Nachricht"
              @keyup.enter="sendMessage"
            />
            <AppButton
              class="rounded-end-4 px-4 send-btn"
              @click="sendMessage"
            >
              ▶
            </AppButton>
          </div>

          <!-- Durchschnittliche Punkte -->
          <div
            class="average-box d-flex align-items-center justify-content-between rounded-4 px-3 py-3"
          >
            <span class="fw-semibold">Durchschnittliche Punkte</span>
            <span class="fs-4 fw-bold">
              {{ averagePoints }}
            </span>
          </div>
        </AppBoxWithShadow>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import AppBoxWithShadow from "../base/AppBoxWithShadow.vue";
import AppInput from "../base/AppInput.vue";
import AppButton from "../base/AppButton.vue";

const sessionCode = ref("ABCD-1234");
const teamName = ref("Test-Team");

const members = ref([
  { name: "StudentXYZ", points: 49 },
  { name: "Überflieger123", points: 20 },
  { name: "Genie123", points: 19 },
  { name: "Student123", points: 18 },
]);

const newMemberName = ref("");

// Chat
const messages = ref([]);
const currentMessage = ref("");

function addMember() {
  const name = newMemberName.value.trim();
  if (!name) return;

  members.value.push({
    name,
    points: 0,
  });
  newMemberName.value = "";
}

function sendMessage() {
  const text = currentMessage.value.trim();
  if (!text) return;

  messages.value.push({
    author: "Du",
    text,
  });
  currentMessage.value = "";
}

const averagePoints = computed(() => {
  if (members.value.length === 0) return "0,0";
  const sum = members.value.reduce((acc, m) => acc + m.points, 0);
  const avg = sum / members.value.length;
  return avg.toFixed(1).replace(".", ",");
});

function initials(name) {
  const parts = name.split(" ").filter(Boolean);
  if (parts.length === 0) return "?";
  if (parts.length === 1) return parts[0].charAt(0).toUpperCase();
  return (
    parts[0].charAt(0).toUpperCase() +
    parts[1].charAt(0).toUpperCase()
  );
}
</script>

<style scoped>
.team-page {
  background-color: #f2f4f5;
}

/* Überschriften-Balken */
.section-title {
  background-color: #4ba1b1; /* wie AppCard-Header */
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
  background-color: #4ba1b1;
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

/* Buttons */
.invite-btn,
.send-btn,
.btn-primary {
  background-color: #3b5b69;
  border-color: #3b5b69;
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
</style>
