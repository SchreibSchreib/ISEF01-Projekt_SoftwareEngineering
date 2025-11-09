<template>

    <div class="login-container text-muted rounded-4 border p-4 shadow">
        <img src="@/assets/circle-user-round.png" class="img-fluid d-block mx-auto mb-2 w-50">
        <p class="login-text text-center fs-1 fw-bold">Anmelden</p>
        <div class="pb-3">
            <LoginInput class="rounded-4"></LoginInput>
        </div>
        <div class="pb-5">
            <LoginPassword class="rounded-4"></LoginPassword>
        </div>
        <div class="d-flex justify-content-center">
            <AppButton class="fs-4 fw-bold rounded-4" @click="goToMainPage">Weiter
            </AppButton>
        </div>
    </div>

</template>

<script setup>
import router from '@/router';
import AppButton from '../base/AppButton.vue';
import LoginInput from '../base/LoginInput.vue';
import LoginPassword from '../base/LoginPassword.vue';

function goToMainPage() {
    router.push({ name: 'dashboard' });
}

</script>

<style scoped>
.login-container {
    background-color: #FFFFFF;
}

.login-text {
    color: #264352;
}
</style>