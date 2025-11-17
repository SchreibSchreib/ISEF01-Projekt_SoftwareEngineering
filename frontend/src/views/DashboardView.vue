<template>

    <div class="container py-4 ">
        <h1 class="mb-4">Hallo StudentXY!</h1>

        <div class="row g-4">

            <!-- LINKE SPALTE -->
            <div class="col-12 col-lg-8 d-flex flex-column gap-4">


                <div class="card shadow-sm p-4">
                    <div class="row g-3 align-items-center">
                        <div class="col-auto">
                            <div class="progress-circle"></div>
                        </div>

                        <div class="col">
                            <h4>Lernfortschritt</h4>
                            <p class="mb-0 text-muted">
                                Herzlichen Glückwunsch! Du hast 100% erfolgreich beantwortet.
                            </p>
                        </div>
                    </div>
                </div>

                <div class="d-flex flex-wrap gap-3">
                    <div class="card small-info-card shadow-sm p-3 flex-fill text-center">
                        <strong>200 von 200 Fragen</strong>
                    </div>
                    <div class="card small-info-card shadow-sm p-3 flex-fill text-center">
                        <strong>200 Punkte</strong>
                    </div>
                </div>

                <div class="d-flex flex-wrap gap-3">
                    <button class="btn btn-outline-primary flex-fill py-3 fw-bold">
                        Neue Fragen erstellen
                    </button>

                    <button class="btn btn-outline-primary flex-fill py-3 fw-bold">
                        Neues Quiz starten
                    </button>

                    <button class="btn btn-outline-primary flex-fill py-3 fw-bold">
                        Kooperativer Session beitreten
                    </button>
                </div>
            </div>
            <!-- RECHTE SPALTE -->
            <div class="col-12 col-lg-4">
                <div class="card shadow-sm p-4 team-card">

                    <h4 class="mb-3">Dein Lernteam</h4>
                    <p class="text-muted">Schau dir den Fortschritt deiner Gruppe an:</p>

                    <div class="d-flex flex-column gap-3">

                        <div class="team-entry d-flex justify-content-between align-items-center p-3 shadow-sm rounded">
                            <strong>StudentXY</strong>
                            <span class="badge bg-light text-dark fs-6">200 Punkte</span>
                        </div>

                        <div class="team-entry d-flex justify-content-between align-items-center p-3 shadow-sm rounded">
                            <strong>Überflieger123</strong>
                            <span class="badge bg-light text-dark fs-6">190 Punkte</span>
                        </div>

                        <div class="team-entry d-flex justify-content-between align-items-center p-3 shadow-sm rounded">
                            <strong>Genie123</strong>
                            <span class="badge bg-light text-dark fs-6">150 Punkte</span>
                        </div>

                        <div class="team-entry d-flex justify-content-between align-items-center p-3 shadow-sm rounded">
                            <strong>Student123</strong>
                            <span class="badge bg-light text-dark fs-6">130 Punkte</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import router from '@/router';

onMounted(async () => {
    const response = await fetch('/api/session/check', {
        credentials: "include"
    });

    if (response.status === 401) {
        router.push({ name: 'login' });
    } else {
        const data = await response.json();
        console.log("Eingeloggt als:", data.username);
    }
});
</script>

<style scoped></style>
