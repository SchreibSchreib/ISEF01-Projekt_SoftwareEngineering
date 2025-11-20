<template>

  <div class="login-container text-muted rounded-4 border p-4 shadow">
    <img src="@/assets/circle-user-round.png" class="img-fluid d-block mx-auto mb-2 w-50">
    <p class="login-text text-center fs-1 fw-bold">Anmelden</p>
    <div class="pb-3">
      <LoginInput v-model="name" placeholder="Benutzername" class="rounded-4" />
    </div>

    <div class="pb-5">
      <LoginPassword v-model="password" placeholder="Passwort" class="rounded-4" />
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
import { ref } from 'vue'
import router from '@/router'
import AppButton from '../base/AppButton.vue';
import LoginInput from '../base/LoginInput.vue';
import LoginPassword from '../base/LoginPassword.vue';

const name = ref('')
const password = ref('')
const errorMessage = ref('')

async function handleLogin() {
  try {
    const response = await fetch('/api/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ name: name.value, passwort: password.value }),
    })

    if (response.ok) {
      if (response.ok) {
        await fetch("/api/session/check", { credentials: "include" })
          .then(res => res.json())
          .then(data => {
            localStorage.setItem("userId", data.userId)
            localStorage.setItem("username", data.username)
          });

        router.push({ name: "dashboard" })
      }

    } else {
      const error = await response.json()
      errorMessage.value = error.message || 'Anmeldung fehlgeschlagen.'
    }
  } catch (err) {
    console.error(err)
    errorMessage.value = 'Server nicht erreichbar.'
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