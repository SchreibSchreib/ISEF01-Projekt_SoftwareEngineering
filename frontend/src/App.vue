<template>
  <div class="container-fluid min-vh-100 g-0">
    <div class="row g-0 h-100">

      <header class="col-12 col-md-auto bg-light border-end p-0 d-flex">
        <TheNavigationBar v-if="!route.meta.hideNavbar" />
      </header>

      <div class="col d-flex flex-column p-0">
        <main class="flex-grow-1 overflow-auto min-vh-100" :class="isMobile ? 'p-2 p-md-4' : ''" :style="mainStyle">
          <router-view />
        </main>

        <footer></footer>
      </div>

    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router';
import { ref, onMounted, computed } from 'vue';
import TheNavigationBar from './components/composed/TheNavigationBar.vue';

const route = useRoute();
const isMobile = ref(false);

// Bildschirmbreite beobachten
onMounted(() => {
  const update = () => {
    isMobile.value = window.innerWidth < 768;
  };
  update();
  window.addEventListener("resize", update);
});

// Dynamischer Style für das <main>
const mainStyle = computed(() => {
  if (route.meta.hideNavbar) return '';

  return isMobile.value
    ? 'margin-top: 56px;'
    : 'margin-left: 234px;';
});
</script>

<style scoped>
main {
  overflow-y: auto;
  background-color: #F2F4F5;
}
</style>
