<template>
    <div class="container py-5">
        <h1 class="fw-bold mb-4">Alle Fragen</h1>

        <!-- Loading -->
        <div v-if="loading" class="text-center py-5">
            <div class="spinner-border"></div>
        </div>

        <div v-else>
            <div class="row justify-content-center g-1">

                <div v-for="question in questions" :key="question.id"
                    class="col-12 col-lg-6 d-flex justify-content-center">
                    <div class="moderation-card w-100">

                            <AppCard class="p-2">

                                <!-- HEADER -->
                                <template #header>
                                    <h4 class="m-2">{{ question.text }}</h4>

                                </template>

                                <!-- BODY -->
                                <template #body>
                                    <div class="d-flex flex-column gap-3">

                                        <!-- Antwortmöglichkeiten -->
                                        <QuizCardButton v-for="(choice, index) in question.choices" :key="choice.id"
                                            :state="choice.id === question.correctChoice.id ? 'correct' : 'wrong'"
                                            :disabled="true">
                                            <template #letter>{{ ['A', 'B', 'C', 'D'][index] }}</template>
                                            <template #answer>{{ choice.text }}</template>
                                        </QuizCardButton>

                                        <ExplainBox :explanation="question.correctAnswerExplanation" />
                                    </div>
                                </template>

                            </AppCard>

                    </div>
                </div>

            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

import AppBoxWithShadow from "@/components/base/AppBoxWithShadow.vue";
import AppCard from "@/components/base/AppCard.vue";
import QuizCardButton from "@/components/base/QuizCardButton.vue";
import ExplainBox from "@/components/base/ExplainBox.vue";

const questions = ref([]);
const loading = ref(true);

/* Fragen holen - gleiche Struktur wie im Singleplayer */
async function loadQuestions() {
    try {
        const res = await fetch("/api/questions", { credentials: "include" });
        if (!res.ok) throw new Error("Fehler");
        questions.value = await res.json();
    } catch (e) {
        console.error("Fehler beim Laden:", e);
    }
    loading.value = false;
}

onMounted(loadQuestions);
</script>

<style scoped>
.moderation-card {
    max-width: 500px;
}
</style>
