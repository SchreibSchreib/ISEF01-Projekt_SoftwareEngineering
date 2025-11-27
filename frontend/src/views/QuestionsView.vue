<template>
    <div class="container py-5">
        <h1 class="mb-4 pb-5 fw-bold">Hallo {{ userStore.currentUser?.name }}</h1>
        <div class="d-flex justify-content-center mt-5">
            <div class="col-12 col-sm-11 col-md-11 col-lg-10">
                <TheQuestionsCard />
            </div>
        </div>

    </div>
</template>

<script setup>
import TheQuestionsCard from '@/components/composed/TheQuestionsCard.vue';
import { userStore } from '@/stores/userStore';
</script>

<style scoped></style>