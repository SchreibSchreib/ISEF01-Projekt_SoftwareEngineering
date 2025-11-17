<template>
  <div class="container-fluid min-vh-100 g-0">
    <div class="row g-0 h-100">
      <header class="col-12 col-md-auto bg-light border-end p-0 d-flex">
        <TheNavigationBar v-if="!route.meta.hideNavbar" />
      </header>
      <div class="col d-flex flex-column p-0">
        <main class="flex-grow-1 p-4 p-md-5 overflow-auto d-md-none "
          :style="route.meta.hideNavbar ? '' : 'margin-top: 56px;'">
          <router-view />
        </main>
        <main class="flex-grow-1 overflow-auto d-none d-md-block min-vh-100 "
          :style="route.meta.hideNavbar ? '' : 'margin-left: 234px;'">
          <router-view />
        </main>
        <footer>
        </footer>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router';
import TheNavigationBar from './components/composed/TheNavigationBar.vue';
const route = useRoute();
</script>

<style scoped>
main {
  overflow-y: auto;
  background-color: #F2F4F5;
}
</style>