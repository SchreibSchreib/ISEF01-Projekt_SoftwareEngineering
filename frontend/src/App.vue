<template>
  <div class="d-flex flex-column min-vh-100">
    <header>
      <TheNavigationBar v-if="!route.meta.hideNavbar" />
    </header>

    <main>
      <router-view />
    </main>

    <footer class="mt-auto">
    </footer>

  </div>
</template>

<script setup>
import { useRoute } from 'vue-router';
import TheNavigationBar from './components/composed/TheNavigationBar.vue';
const route = useRoute();

</script>

<style></style>