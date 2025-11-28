<template>
    <div class="text-center">

        
        <img src="@/assets/under-construction.png"
             alt="Keine Einträge"
             class="mb-4 nothing-image" />

        <h1 class="fw-bold mb-3">Keine Fragen zur Bewertung vorhanden</h1>

        <p class="fs-5 text-muted mb-4">
            Momentan wurden keine neuen Fragen eingereicht.<br>
            Schau später noch einmal vorbei!
        </p>

        <button class="btn btn-dark rounded-4 px-4 py-3 fw-bold" @click="goHome">
            Zurück zum Dashboard
        </button>
    </div>
</template>

<script setup>
import { useRouter } from "vue-router";
const router = useRouter();

function goHome() {
    router.push("/dashboard");
}
</script>

<style scoped>
.nothing-wrapper {
  height: 100vh;
}

.nothing-image {
    width: 120px;
    opacity: 0.85;
    filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.15));
}

.btn-dark {
    background-color: #3B5B69;
    border-color: #3B5B69;
}
</style>

