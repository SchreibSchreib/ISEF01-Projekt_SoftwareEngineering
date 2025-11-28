<template>
    <div class="container py-5">
        <h1 class="fw-bold mb-4">Eingereichte Fragen bewerten</h1>

        <!-- Loading -->
        <div v-if="loading" class="text-center py-5">
            <div class="spinner-border"></div>
        </div>

        <!-- Keine Fragen -->
        <div 
    v-else-if="questions.length === 0"
    class="d-flex flex-column justify-content-center align-items-center text-center mt-5" 
    style="min-height: 50vh;"
>
    <TheNothingToEvaluateScreen />
</div>

        <!-- Fragen vorhanden -->
        <div v-else>
            <div v-for="question in questions" :key="question.id" class="moderation-wrapper mb-4">
                <AppBoxWithShadow class="p-0 bg-light rounded-4">
                    <AppCard class="p-2">
                        <template #header>
                            <h4 class="mt-2">{{ question.text }}</h4>
                            <p class="mb-1">
                                Eingereicht von: <strong>{{ question.submittedBy?.name || 'Unbekannt' }}</strong>
                            </p>
                        </template>

                        <template #body>
                            <div class="d-flex flex-column gap-3">
                                <QuizCardButton
                                    v-for="choice in mapChoices(question)"
                                    :key="choice.label"
                                    :state="choice.label === question.correctAnswer ? 'correct' : 'wrong'"
                                    :disabled="true"
                                >
                                    <template #letter>{{ choice.label }}</template>
                                    <template #answer>{{ choice.text }}</template>
                                </QuizCardButton>

                                <ExplainBox :explanation="question.explanation" />

                                <div class="d-flex gap-3 mt-3 flex-column flex-lg-row">
                                    <AppButtonGreen class="rounded-4" @click="vote(question.id, true)">
                                        Akzeptieren ({{ question.positiveVotes }})
                                    </AppButtonGreen>
                                    <AppButtonRed class="rounded-4" @click="vote(question.id, false)">
                                        Ablehnen ({{ question.negativeVotes }})
                                    </AppButtonRed>
                                </div>
                            </div>
                        </template>
                    </AppCard>
                </AppBoxWithShadow>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import AppButtonGreen from "@/components/base/AppButtonGreen.vue";
import AppButtonRed from "@/components/base/AppButtonRed.vue";
import AppCard from "@/components/base/AppCard.vue";
import QuizCardButton from "@/components/base/QuizCardButton.vue";
import ExplainBox from "@/components/base/ExplainBox.vue";
import AppBoxWithShadow from "@/components/base/AppBoxWithShadow.vue";
import TheNothingToEvaluateScreen from "@/components//composed/TheNothingToEvaluateScreen.vue";

const questions = ref([]);
const loading = ref(true);

async function loadQuestions() {
    const res = await fetch("/api/submitted-questions/pending");
    questions.value = await res.json();
    loading.value = false;
}

async function vote(id, positive) {
    try {
        const res = await fetch(`/api/submitted-questions/${id}/vote?positive=${positive}`, {
            method: "POST"
        });

        if (!res.ok) {
            const msg = await res.text();
            alert(msg || "Fehler beim Bewerten");
            return;
        }

        await loadQuestions();
    } catch (e) {
        console.error(e);
        alert("Netzwerkfehler beim Bewerten");
    }
}

function mapChoices(q) {
    return [
        { label: "A", text: q.choiceA },
        { label: "B", text: q.choiceB },
        { label: "C", text: q.choiceC },
        { label: "D", text: q.choiceD }
    ];
}

onMounted(loadQuestions);
</script>
