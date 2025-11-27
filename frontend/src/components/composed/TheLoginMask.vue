<template>
  <div class="login-container text-muted rounded-4 border p-4 shadow">
    <img src="@/assets/circle-user-round.png" class="img-fluid d-block mx-auto mb-2 w-50">
    <p class="login-text text-center fs-1 fw-bold">Anmelden</p>
    <div class="pb-3">
      <AppInput v-model="name" placeholder="Benutzername" />
    </div>

    <div class="pb-3">
      <LoginPassword v-model="password" placeholder="Passwort" />
    </div>
    <div id="turnstile-widget" class="cf-turnstile mb-3 d-flex justify-content-center">
    </div>
    <p v-if="errorMessage" class="text-danger text-center mt-3">
      {{ errorMessage }}
    </p>
    <div class="d-flex justify-content-center">
      <AppButton class="fs-4 fw-bold rounded-4" @click="handleLogin">Login</AppButton>
    </div>
  </div>

</template>

<script setup>
import { ref, onMounted } from 'vue'
import { userStore } from "@/stores/userStore";
import router from '@/router'
import AppButton from '../base/AppButton.vue';
import AppInput from '../base/AppInput.vue';
import LoginPassword from '../base/LoginPassword.vue';

const name = ref('')
const password = ref('')
const errorMessage = ref('')

onMounted(() => {
  const interval = setInterval(() => {
    if (window.turnstile) {
      turnstile.render("#turnstile-widget", {
        sitekey: "0x4AAAAAACCGKnuiMiYVTDPG",
        theme: "light"
      })
      clearInterval(interval)
    }
  }, 200)
})

async function handleLogin() {

  const token = document.querySelector('[name="cf-turnstile-response"]')?.value;
  if (!token) {
    errorMessage.value = "Bitte bestätige das Captcha.";
    return;
  }

  try {
    const response = await fetch("/api/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({
        name: name.value,
        passwort: password.value,
        captchaToken: token
      })
    });

    if (!response.ok) {
      const error = await response.json();
      errorMessage.value = error.message || "Anmeldung fehlgeschlagen.";
      resetTurnstile();
      return;
    }

    userStore.currentUser = null;
    userStore.isLoaded = false;

    const meRes = await fetch("/api/users/me", {
      credentials: "include"
    });

    if (meRes.ok) {
      userStore.currentUser = await meRes.json();
      userStore.isLoaded = true;
    }

    router.push({ name: "dashboard" });

  } catch (err) {
    console.error(err);
    errorMessage.value = "Server nicht erreichbar.";
    resetTurnstile();
  }
}

function resetTurnstile() {
  if (window.turnstile) {
    turnstile.reset()
  }
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