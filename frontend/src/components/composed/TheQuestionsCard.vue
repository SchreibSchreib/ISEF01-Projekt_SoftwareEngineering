<template>
    <AppCard>
        <template #header>
            <h1 class="m-3">Fragenkatalog</h1>
        </template>
        <template #body>
            <AppButton class="fs-3 fw-bold rounded-4 mt-4 mb-3" @click="navigateAddQuestion">Fragenkatalog erweitern
            </AppButton>
            <AppButton class="fs-3 fw-bold rounded-4 mt-3 mb-4">Neue Frage bewerten</AppButton>
        </template>
    </AppCard>
</template>
<script setup>

import AppCard from '../base/AppCard.vue';
import AppButton from '../base/AppButton.vue';
import router from '@/router';

function navigateAddQuestion() {
    router.push({ name: 'addquestion' });
}

</script>
<style scoped></style>