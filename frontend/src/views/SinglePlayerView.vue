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

                    <!-- Antwortoptionen mit Backend-Daten -->
                    <button
                        v-for="(choice, index) in currentQuestion.choices"
                        :key="choice.id"
                        class="answer-btn"
                        :class="answerClass(choice)"
                        :disabled="answered"
                        @click="selectAnswer(choice)"
                    >
                        <strong>{{ indexToLetter(index) }}</strong>
                        <span>{{ choice.text }}</span>
                    </button>

                    <div class="d-flex justify-content-end mt-4">
                        <button class="btn btn-outline-primary px-4 py-2 fw-bold"
                            :disabled="!answered"
                            @click="nextQuestion">
                            {{ isLastQuestion ? 'Ergebnis' : 'Weiter' }}
                        </button>
                    </div>

                </div>
            </template>
        </QuizCard>

        <!-- ERGEBNIS -->
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

                    <button class="btn btn-primary px-4 py-2 fw-bold me-3" @click="restart">Nochmal spielen</button>
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
import QuizCard from "@/components/base/QuizCard.vue";

const questions = ref([]);
const loading = ref(true);
const finished = ref(false);

/* Backend laden */
onMounted(async () => {
    const response = await fetch("/api/questions/random/4");
    questions.value = await response.json();
    loading.value = false;
});

/* Quiz State */
const currentIndex = ref(0);
const selectedChoice = ref(null);
const answered = ref(false);
const score = ref(0);

const currentQuestion = computed(() => questions.value[currentIndex.value]);
const isLastQuestion = computed(() => currentIndex.value === questions.value.length - 1);

/* Antwort wählen (Choice-Objekt!) */
function selectAnswer(choice) {
    selectedChoice.value = choice;
    answered.value = true;

    if (choice.id === currentQuestion.value.correctChoice.id) {
        score.value++;
    }
}

/* Farben */
function answerClass(choice) {
    if (!answered.value) return "neutral";

    if (choice.id === currentQuestion.value.correctChoice.id)
        return "correct";

    if (selectedChoice.value?.id === choice.id)
        return "wrong";

    return "neutral";
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
.answer-btn {
    display: flex;
    align-items: center;
    gap: 20px;
    padding: 18px;
    border-radius: 20px;
    border: 3px solid #0ca6b6;
    background: white;
    font-size: 1.2rem;
    text-align: left;
    width: 100%;
    transition: 0.2s;
}

/* Hover */
.answer-btn.neutral:hover {
    background: #e9ffff;
}

/* Richtige Antwort */
.correct {
    background: #aaffdd !important;
    border-color: #00aa55 !important;
}

/* Falsche */
.wrong {
    background: #ff9988 !important;
    border-color: #cc0000 !important;
}
</style>

