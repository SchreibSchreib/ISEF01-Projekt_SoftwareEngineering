<template>

    <div class="container py-4">
        <div class="mb-5">
            <h1>Hallo {{ currentUser?.name }}</h1>
        </div>

        <div class="row g-4 mt-5 align-items-start">

            <!-- LINKE SPALTE -->
            <div class="col-12 col-lg-6 d-flex px-4 flex-column gap-3">


                <AppBoxWithShadow class="shadow-sm text-center mt-4 mb-3">
                    <p class="fs-4 m-0">Der Fragenkatalog beinhaltet momentan:</p>
                    <p class="fs-5 fw-bold my-1">{{ questionCount }} Fragen</p>
                </AppBoxWithShadow>
                <AppBoxWithShadow class="shadow-sm text-center">
                    <p class="fs-4 m-0">Dein Score:</p>
                    <p class="fs-5 fw-bold my-1">{{ currentUser?.userscore }} Puntke</p>
                </AppBoxWithShadow>


                <div class="d-flex flex-wrap gap-3">
                    <DashboardButton to="/addquestion" class="mt-lg-4 w-100">
                        Neue Fragen erstellen
                    </DashboardButton>

                    <DashboardButton to="/singleplayer" class="flex-even py-5">
                        Neues Quiz starten
                    </DashboardButton>

                    <DashboardButton to="/multiplayer" class="flex-even py-5">
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

                        <AppBox v-for="member in teamUsers" :key="member.userId"
                            class="rounded-4  ps-4 d-flex justify-content-between align-items-center shadow-sm p-1 rounded">
                            <strong>{{ member.name }}</strong>
                            <span class="badge text-dark fs-6">
                                <AppBox class="border-black">{{ member.userscore }} Punkte</AppBox>
                            </span>
                        </AppBox>

                        <AppBox v-for="i in freeSlots" :key="'slot-' + i"
                            class="rounded-4 ps-4 d-flex justify-content-between align-items-center shadow-sm p-2 bg-light text-muted">
                            <strong>Freier Platz</strong>
                            <span class="pe-2">
                                <AppButton class="p-3 rounded-4">Hinzufügen</AppButton>
                            </span>
                        </AppBox>

                        <AppBox
                            class=" fs-4 rounded-4 ps-4 d-flex flex-column align-items-start shadow-sm pe-1 rounded">
                            <div class="d-flex w-100 justify-content-between">
                                <strong>Durchschnittliche Punkte</strong>
                                <span class="badge text-dark">{{ }} Punkte</span>
                            </div>

                            <div class="d-flex w-100 justify-content-between mt-2">
                                <strong>Beantwortete Fragen</strong>
                                <span class="badge text-dark">{{ }} %</span>
                            </div>
                        </AppBox>
                    </div>
                </template>
            </AppCard>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { computed } from 'vue';
import AppBoxWithShadow from '@/components/base/AppBoxWithShadow.vue';
import AppCard from '@/components/base/AppCard.vue';
import DashboardButton from '@/components/base/DashboardButton.vue';
import AppBox from '@/components/base/AppBox.vue';
import AppButton from '@/components/base/AppButton.vue';

const currentUser = ref(null)
const teamUsers = computed(() => currentUser.value?.team?.users ?? []);
const MAX_TEAM_SIZE = 4;
const freeSlots = computed(() => {
    return MAX_TEAM_SIZE - teamUsers.value.length;
});
const questionCount = ref(0)


onMounted(async () => {
    try {
        const userData = await fetch("/api/users/me", {
            credentials: "include"
        });

        if (userData.status === 401) {
            console.error("Nicht eingeloggt");
            return;
        }

        const user = await userData.json();
        currentUser.value = user;

        const questionData = await fetch("/api/questions", { credentials: "include" });
        const questions = await questionData.json();
        questionCount.value = questions.length;

    } catch (e) {
        console.error("Fehler beim Laden des Users:", e);
    }
});



</script>

<style scoped></style>
