<template>

    <div class="container py-4 ">
        <h1 class="mb-4">Hallo StudentXY!</h1>

        <div class="row g-4">
            <QuizCard/>
        </div>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import router from '@/router';
import QuizCard from '@/components/base/QuizCard.vue';

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