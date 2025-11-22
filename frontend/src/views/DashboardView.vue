<template>

    <div class="container-fluid vh-100  py-4">
        <div class="mb-5">
        <h1>Hallo StudentXY!</h1>
        </div>

        <div class="row g-4 mt-5 align-items-start">

            <!-- LINKE SPALTE -->
            <div class="col-12 col-lg-6 d-flex px-4 flex-column gap-3">


                <AppBoxWithShadow class="shadow-sm text-center mt-4 mb-3">
                    <p class="fs-4 m-0">Der Fragenkatalog beinhaltet momentan:</p>
                    <p class="fs-5 fw-bold my-1">{{ }} Fragen</p>
                </AppBoxWithShadow>
                <AppBoxWithShadow class="shadow-sm text-center">
                    <p class="fs-4 m-0">Dein Score:</p>
                    <p class="fs-5 fw-bold my-1">{{ }} Puntke</p>
                </AppBoxWithShadow>


                <div class="d-flex flex-wrap gap-3">
                    <DashboardButton class="mt-lg-4 w-100">
                        Neue Fragen erstellen
                    </DashboardButton>

                    <DashboardButton class="flex-even">
                        Neues Quiz starten
                    </DashboardButton>

                    <DashboardButton class="flex-even">
                        Session beitreten
                    </DashboardButton>
                </div>
            </div>
            <!-- RECHTE SPALTE -->
            <AppCard class="col-12 col-lg-6">
                <template #header>
                    <div class="d-flex justify-content-center">
                        <h4 class="m-2">Dein Lernteam</h4>
                    </div>
                </template>
                <template #body>
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
                </template>


            </AppCard>
        </div>
    </div>
</template>

<script setup>
import AppBoxWithShadow from '@/components/base/AppBoxWithShadow.vue';
import AppCard from '@/components/base/AppCard.vue';
import DashboardButton from '@/components/base/DashboardButton.vue';


</script>

<style scoped></style>
