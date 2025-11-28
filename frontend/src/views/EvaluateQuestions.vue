<template>
    <div class="container py-5">
        <h1 class="fw-bold mb-4">Eingereichte Fragen bewerten</h1>

        <div v-if="loading" class="text-center py-5">
            <div class="spinner-border"></div>
        </div>

        <div v-else>
            <div 
                v-for="q in questions" 
                :key="q.id"
                class="border rounded-4 p-4 mb-4 bg-white shadow-sm"
            >
                <h4 class="fw-bold">{{ q.text }}</h4>

                <p class="text-muted mb-2">
                    Eingereicht von: <strong>{{ q.submittedBy?.name || 'Unbekannt' }}</strong>
                </p>

                <ul class="mt-3">
                    <li>A: {{ q.choiceA }}</li>
                    <li>B: {{ q.choiceB }}</li>
                    <li>C: {{ q.choiceC }}</li>
                    <li>D: {{ q.choiceD }}</li>
                </ul>

                <p><strong>Erklärung:</strong> {{ q.explanation }}</p>

                <div class="d-flex gap-3 mt-3">
                    <button 
                        class="btn btn-success flex-fill" 
                        @click="vote(q.id, true)"
                    >
                        👍 Akzeptieren ({{ q.positiveVotes }})
                    </button>

                    <button 
                        class="btn btn-danger flex-fill" 
                        @click="vote(q.id, false)"
                    >
                        👎 Ablehnen ({{ q.negativeVotes }})
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

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

        await loadQuestions(); // Liste aktualisieren (approved != 0 kommt nicht mehr mit)
    } catch (e) {
        console.error(e);
        alert("Netzwerkfehler beim Bewerten");
    }
}

onMounted(loadQuestions);
</script>
