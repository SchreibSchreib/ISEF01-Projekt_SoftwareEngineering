<template>
    <div
        class="container align-content-center justify-content-center py-5 d-flex flex-column align-items-center text-center vh-100">
        <img src="@/assets/under-construction.png" alt="Noch nicht verfügbar" class="mb-4"
            style="width:120px; opacity:0.8;" />

        <h1 class="fw-bold mb-3">Funktion noch nicht verfügbar</h1>

        <p class="fs-5 text-muted mb-4">
            Diese Funktion wurde noch nicht implementiert.<br>
            Bitte probiere es später erneut.
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
    router.push("dashboard");
}
</script>

<style scoped>
img {
    filter: drop-shadow(0 0 4px rgba(0, 0, 0, 0.15));
}

.btn-dark {
    background-color: #3B5B69;
    border-color: #3B5B69;
}
</style>
