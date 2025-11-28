<template>
    <div class="container py-5">
        <h1 class="fw-bold">Hallo {{ userStore.currentUser?.name }}</h1>

        <div class="row g-3">

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

                <AppBox :max="225" class="mt-5 mb-3 mx-auto">
                    <p class="fw-bold text-center">Welche Antwort ist korrekt?</p>

                    <div class="d-flex input-style flex-wrap justify-content-center gap-3 mt-3">
                        <ChoiceButton v-for="letter in ['A', 'B', 'C', 'D']" :key="letter" :letter="letter"
                            :active="correctAnswer === letter" @select="correctAnswer = $event" />
                    </div>
                </AppBox>

                <div class=" mb-2">
                    <p class="fw-bold mb-1">Begründung</p>
                    <AppInput class="form-control fs-5 border-3" placeholder="Hier eingeben" rows="4"
                        v-model="explanation">
                    </AppInput>
                </div>

                <AppButton class="btn btn-dark rounded-3 w-100 mb-0 py-3 fw-bold save-btn" @click="saveQuestion">
                    Frage übernehmen
                </AppButton>
                <SuccessPanel :show="success" message="Frage erfolgreich eingereicht!" @hide="success = false" />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue";
import { userStore } from "@/stores/userStore";
import AppCard from "@/components/base/AppCard.vue";
import AppInput from "@/components/base/AppInput.vue";
import AppBox from "@/components/base/AppBox.vue";
import ChoiceButton from "@/components/base/AppChoiceButton.vue";
import SuccessPanel from "@/components/base/SuccessPanel.vue";
import AppButton from "@/components/base/AppButton.vue";

// Formular-Daten
const newQuestion = ref("");
const choices = ref({ A: "", B: "", C: "", D: "" });
const correctAnswer = ref(null);
const explanation = ref("");
const success = ref(false);

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


    if (response.ok) {
        // Felder zurücksetzen
        newQuestion.value = "";
        choices.value = { A: "", B: "", C: "", D: "" };
        correctAnswer.value = null;
        explanation.value = "";
        // Erfolgsmeldung anzeigen
        success.value = true;
    } else {
        console.error("Fehler beim Speichern", await response.text());
    }
}
</script>

<style scoped>
.input-style {
    border-color: #1fa1a8 !important;
}
</style>
