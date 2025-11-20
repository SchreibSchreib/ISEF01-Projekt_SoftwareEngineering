<template>
    <div class="container py-4">
        <h1 class="mb-4 pb-4">Hallo StudentXY!</h1>

        <div class="row g-4">

            <!-- ======================================= -->
            <!--  LINKER BEREICH: QUIZCARD              -->
            <!-- ======================================= -->
            <div class="col-12 col-lg-8">
                <QuizCard>
                    <template #header>
                        <h3 class="text-center text-white mb-0">Fragenkatalog erweitern</h3>
                    </template>

                    <template #body>
                        <!-- Frage -->
                        <div class="mb-3">
                            <input 
                                type="text"
                                class="form-control input-style"
                                placeholder="Neue Frage hier eingeben."
                                v-model="question"
                            />
                        </div>

                        <!-- Antwortfelder A–D -->
                        <div class="mb-3" v-for="letter in ['A','B','C','D']" :key="letter">
                            <input 
                                type="text"
                                class="form-control input-style"
                                :placeholder="`Antwortmöglichkeit ${letter}`"
                                v-model="choices[letter]"
                            />
                        </div>
                    </template>
                </QuizCard>
            </div>

            <!-- ======================================= -->
            <!--  RECHTER BEREICH: Antwort + Erklärung   -->
            <!-- ======================================= -->
            <div class="col-12 col-lg-4 pt-0 pt-lg-5">

                <!-- Korrekte Antwort -->
                <div class="selector-box mb-4">
                    <p class="fw-bold text-center">Welche Antwort ist korrekt?</p>

                    <div class="d-flex flex-wrap justify-content-center gap-3 mt-3">
                        <button 
                            v-for="letter in ['A','B','C','D']"
                            :key="letter"
                            class="btn btn-outline-info selector-btn"
                            :class="{ activeBtn: correctAnswer === letter }"
                            @click="correctAnswer = letter"
                        >
                            {{ letter }}
                        </button>
                    </div>
                </div>

                <!-- Begründung -->
                <div class="selector-box mb-4">
                    <p class="fw-bold mb-1">Begründung</p>
                    <textarea 
                        class="form-control explanation-box"
                        placeholder="Hier eingeben"
                        rows="4"
                        v-model="explanation"
                    ></textarea>
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
import QuizCard from "@/components/base/QuizCard.vue";

// Formular-Daten
const question = ref("");
const choices = ref({ A: "", B: "", C: "", D: "" });
const correctAnswer = ref(null);
const explanation = ref("");

// später für API-Call
function saveQuestion() {
    console.log("Neue Frage:", question.value);
    console.log("Antworten:", choices.value);
    console.log("Korrekt:", correctAnswer.value);
    console.log("Begründung:", explanation.value);
}
</script>

<style scoped>
/* Eingabefelder links */
.input-style {
    border: 3px solid #1fa1a8;
    border-radius: 20px;
    padding: 15px;
    font-size: 1.1rem;
}

/* Boxen rechts */
.selector-box {
    border: 2px solid #1fa1a8;
    border-radius: 20px;
    padding: 20px;
    background: white;
}

/* Antwort-Buttons */
.selector-btn {
    width: 50px;
    height: 50px;
    font-size: 1.2rem;
    border-radius: 12px;
    border-width: 2px;
}

.activeBtn {
    background-color: #1fa1a8 !important;
    color: white !important;
}

/* Erklärung */
.explanation-box {
    border-radius: 15px;
    border: 2px solid #1fa1a8;
}

/* Speichern */
.save-btn {
    border-radius: 15px;
    font-size: 1.2rem;
}
</style>
