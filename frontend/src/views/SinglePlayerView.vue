<template>
    <div class="container py-5">
        <h1 class="fw-bold">Hallo {{ userStore.currentUser?.name }}</h1>

        <!-- Loading -->
        <div v-if="loading" class="text-center py-5">
            <p>Wird geladen...</p>
        </div>

        <!-- When loaded -->
        <QuizCard v-else-if="!finished">
            <template #header>
                <div class="d-flex flex-column">
                    <strong>Frage {{ currentIndex + 1 }} von {{ questions.length }}</strong>
                    <span class="fw-light mt-2">{{ currentQuestion.text }}</span>
                </div>
            </template>

            <template #body>
                <div class="d-flex flex-column gap-3">
                    <QuizCardButton v-for="(choice, index) in currentQuestion.choices"
                        :key="`${currentIndex}-${choice.id}-${index}`" :state="answerClass(choice)" :disabled="answered"
                        @click="selectAnswer(choice)">
                        <template #letter>{{ indexToLetter(index) }}</template>
                        <template #answer>{{ choice.text }}</template>
                    </QuizCardButton>

                    <div class="d-flex flex-column flex-md-row justify-content-end align-items-md-center mt-4 gap-3">
                        <div class="flex-grow-1">
                            <ExplainBox v-if="answered" :explanation="currentQuestion.correctAnswerExplanation" />
                        </div>

                        <button class="btn border-3 rounded-3 px-4 py-2 fw-bold" style="height: 50px; width: 150px;"
                            :disabled="!answered" @click="nextQuestion">
                            {{ isLastQuestion ? 'Ergebnis' : 'Weiter' }}
                        </button>
                    </div>
                </div>
            </template>
        </QuizCard>

        <!-- Ergebnis -->
        <QuizCard v-else>
            <template #header>
                <h4 class="mb-0">Dein Ergebnis</h4>
            </template>

            <template #body>
                <div class="text-center">
                    <h2 class="fw-bold mt-3">{{ score }} / {{ questions.length }}</h2>
                    <p class="text-muted mb-4 fs-5">
                        Du hast {{ score }} von {{ questions.length }} Fragen korrekt beantwortet.
                    </p>

                    <button class="btn btn-primary px-4 py-2 fw-bold me-3" @click="restart">
                        Nochmal spielen
                    </button>
                    <button class="btn btn-secondary px-4 py-2 fw-bold" @click="$router.push('/dashboard')">
                        Zurück zum Dashboard
                    </button>
                </div>
            </template>
        </QuizCard>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { userStore } from "@/stores/userStore";
import QuizCard from "@/components/base/AppCard.vue";
import QuizCardButton from "@/components/base/QuizCardButton.vue";
import ExplainBox from "@/components/base/ExplainBox.vue";

const questions = ref([]);
const loading = ref(true);
const finished = ref(false);

/* Backend laden */
onMounted(async () => {
    const response = await fetch("/api/questions/random/10");
    questions.value = await response.json();
    console.log("RAW QUESTIONS:", JSON.stringify(questions.value, null, 2));
    loading.value = false;
});

/* Quiz State */
const currentIndex = ref(0);
const selectedChoice = ref(null);
const answered = ref(false);
const score = ref(0);

const currentQuestion = computed(() => questions.value[currentIndex.value]);
const isLastQuestion = computed(
    () => currentIndex.value === questions.value.length - 1
);

/* Antwort wählen + Backend-Call */
async function selectAnswer(choice) {
    if (answered.value) return;

    selectedChoice.value = choice;
    answered.value = true;
    console.log("selectAnswer fired for", choice.text);

    const isCorrect =
        choice.id === currentQuestion.value.correctChoice.id;

    // Für die Anzeige im aktuellen Quizlauf lokal mitzählen
    if (isCorrect) {
        score.value++;
    }

    // Punkte & Tracking im Backend speichern
    const userId = localStorage.getItem("userId");

    if (!userId) {
        console.warn("Kein userId in localStorage gefunden – Antwort wird nicht getrackt.");
        return;
    }

    try {
        const response = await fetch(`/api/users/${userId}/answer`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({
                questionId: currentQuestion.value.id, // ggf. an dein Feld anpassen
                choiceId: choice.id,
                correct: isCorrect
            })
        });

        if (!response.ok) {
            console.error("Fehler beim Antworten:", response.status, response.statusText);
            return;
        }

        const data = await response.json();
        console.log("AnswerResponse:", data);

    } catch (err) {
        console.error("Fehler beim Senden der Antwort:", err);
    }
}

/* Farben abhängig vom Zustand */
function answerClass(choice) {
    if (!answered.value) return "neutral";

    if (choice.id === currentQuestion.value.correctChoice.id) return "correct";

    // *** ALLE falschen Antworten rot ***
    return "wrong";
}

/* A/B/C/D */
function indexToLetter(index) {
    return ["A", "B", "C", "D"][index];
}

/* Weiter */
function nextQuestion() {
    if (isLastQuestion.value) {
        finished.value = true;
        return;
    }

    currentIndex.value++;
    selectedChoice.value = null;
    answered.value = false;
}

/* Restart */
function restart() {
    currentIndex.value = 0;
    score.value = 0;
    selectedChoice.value = null;
    answered.value = false;
    finished.value = false;
}
</script>

<style scoped>
.btn {
    border-color: #357c7c !important;
}
</style>
