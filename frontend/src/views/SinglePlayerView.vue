<template>
    <div class="container py-4">
        <h1 class="mb-4 pb-4">Hallo StudentXY!</h1>

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
import QuizCard from "@/components/base/AppCard.vue";
import QuizCardButton from "@/components/base/QuizCardButton.vue";
import ExplainBox from "@/components/base/ExplainBox.vue";

const questions = ref([]);
const loading = ref(true);
const finished = ref(false);

/* Backend laden */
onMounted(async () => {
    const response = await fetch("/api/questions/random/4");
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
const isLastQuestion = computed(() => currentIndex.value === questions.value.length - 1);

/* Antwort wählen */
function selectAnswer(choice) {
    selectedChoice.value = choice;
    answered.value = true;
    console.log("selectAnswer fired for", choice.text)

    if (choice.id === currentQuestion.value.correctChoice.id) {
        score.value++;
    }
}

/* Farben abhängig vom Zustand */
function answerClass(choice) {
    if (!answered.value) return "neutral";

    if (choice.id === currentQuestion.value.correctChoice.id)
        return "correct";

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

        updateScore();
        return;

    }

    currentIndex.value++;
    selectedChoice.value = null;
    answered.value = false;
}

async function updateScore() {
    const userId = localStorage.getItem("userId");
    const points = score.value * 10;

    try {
        await fetch(`/api/users/${userId}/addScore?points=${points}`, {
            method: "POST",
            credentials: "include"
        });

        console.log("Score erfolgreich aktualisiert:", points);

    } catch (err) {
        console.error("Score konnte nicht aktualisiert werden:", err);
    }
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
