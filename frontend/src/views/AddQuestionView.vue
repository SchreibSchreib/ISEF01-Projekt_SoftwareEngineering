<template>
    <div class="container py-4">
        <h1 class="mb-4 pb-4">Hallo StudentXY!</h1>

        <div class="row g-4">

            <!--LINKER BEREICH: QUIZCARD-->
            <div class="col-12 col-lg-8">
                <AppCard>
                    <template #header>
                        <h3 class="text-center text-white py-3 mb-0">Fragenkatalog erweitern</h3>
                    </template>

                    <template #body>
                        <div class="mb-4">
                            <AppInput class="form-control input-style border-3 justify-content-center fs-3"
                                placeholder="Neue Frage hier eingeben." v-model="newQuestion" />
                        </div>

                        <!-- Antwortfelder A–D -->
                        <div class="mb-2" v-for="letter in ['A', 'B', 'C', 'D']" :key="letter">
                            <AppInput class="form-control input-style border-3 fs-5"
                                :placeholder="`Antwortmöglichkeit ${letter}`" v-model="choices[letter]" />
                        </div>
                    </template>
                </AppCard>
            </div>

            <!--  RECHTER BEREICH: Antwort + Erklärung   -->
            <div class="col-12 col-lg-4 pt-0 pt-lg-5">

                <!-- Korrekte Antwort -->
                <AppBox :max="225" class="mb-4 mx-auto">
                    <p class="fw-bold text-center">Welche Antwort ist korrekt?</p>

                    <div class="d-flex input-style flex-wrap justify-content-center gap-3 mt-3">
                        <ChoiceButton v-for="letter in ['A', 'B', 'C', 'D']" :key="letter" :letter="letter"
                            :active="correctAnswer === letter" @select="correctAnswer = $event" />
                    </div>
                </AppBox>

                <!-- Begründung -->
                <div class=" mb-4">
                    <p class="fw-bold mb-1">Begründung</p>
                    <AppInput class="form-control fs-5 border-3" placeholder="Hier eingeben" rows="4"
                        v-model="explanation">
                    </AppInput>
                </div>

                <!-- Speichern -->
                <button class="btn btn-dark w-100 py-3 fw-bold save-btn" @click="saveQuestion">
                    Frage übernehmen
                </button>

            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue";
import AppCard from "@/components/base/AppCard.vue";
import AppInput from "@/components/base/AppInput.vue";
import AppBox from "@/components/base/AppBox.vue";
import ChoiceButton from "@/components/base/AppChoiceButton.vue";

// Formular-Daten
const newQuestion = ref("");
const choices = ref({ A: "", B: "", C: "", D: "" });
const correctAnswer = ref(null);
const explanation = ref("");

async function saveQuestion() {
    const payload = {
        text: newQuestion.value,
        choices: {
            A: choices.value.A,
            B: choices.value.B,
            C: choices.value.C,
            D: choices.value.D,
        },
        correct: correctAnswer.value,
        explanation: explanation.value
    };

    const response = await fetch("/api/questions", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
        credentials: "include"
    });

    if (!response.ok) {
        console.error("Fehler beim Speichern", await response.text());
    }
}
</script>

<style scoped>
/* Eingabefelder links */
.input-style {
    border-color: #1fa1a8 !important;
}
</style>
