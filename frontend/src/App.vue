<script setup>
import { ref } from 'vue';

const message = ref('Noch keine Nachricht');

const getMessage = async () => {
  try {
    const res = await fetch('/api/hello'); // Spring Boot Endpunkt
    if (!res.ok) throw new Error('Fehler beim Abrufen vom Backend');
    const text = await res.text();
    message.value = text;
  } catch (err) {
    message.value = 'Fehler: ' + err.message;
  }
};
</script>

<template>
  <div style="text-align: center; margin-top: 50px;">
    <h1>You did it!</h1>
    <p>
      Visit <a href="https://vuejs.org/" target="_blank" rel="noopener">vuejs.org</a> to read the
      documentation
    </p>

    <hr style="margin: 30px 0;" />

    <p><strong>Backend-Nachricht:</strong> {{ message }}</p>
    <button @click="getMessage" style="padding: 10px 20px; font-size: 16px;">
      Nachricht vom Backend holen
    </button>
  </div>
</template>

<style scoped>
button {
  cursor: pointer;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
}
button:hover {
  background-color: #0056b3;
}
</style>