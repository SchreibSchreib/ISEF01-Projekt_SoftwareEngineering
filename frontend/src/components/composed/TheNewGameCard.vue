<template>
    <QuizCard>
        <template #header>
            <h1 class="m-3">Quiz starten</h1>
        </template>
        <template #body>
            <AppButton class="fs-3 fw-bold rounded-4 mt-4 mb-3" @click="navigateSinglePlayer">Einzelmodus</AppButton>
            <AppButton class="fs-3 fw-bold rounded-4 mt-3 mb-4">Teammodus</AppButton>
        </template>
    </QuizCard>
</template>
<script setup>

import QuizCard from '../base/AppCard.vue';
import AppButton from '../base/AppButton.vue';
import router from '@/router';

function navigateSinglePlayer() {
    router.push({ name: 'singleplayer' });
}

</script>
<style scoped></style>