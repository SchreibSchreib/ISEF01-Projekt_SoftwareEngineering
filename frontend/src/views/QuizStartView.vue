<template>
    <div class="container py-4">
        <h1 class="mb-4 pb-5">Hallo StudentXY!</h1>
        <div class="d-flex justify-content-center mt-5">
            <div class="col-12 col-sm-11 col-md-11 col-lg-10">
                <TheNewGameCard />
            </div>
        </div>

    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import router from '@/router';
import TheNewGameCard from '@/components/composed/TheNewGameCard.vue';

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