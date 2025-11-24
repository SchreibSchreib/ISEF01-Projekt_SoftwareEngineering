<template>
  <button
    class="btn flex-fill border-3 rounded-4 mt-2 py-3 fw-bold"
    @click="navigate"
  >
    <slot></slot>
  </button>
</template>

<script setup>
import { useRouter } from "vue-router";

const props = defineProps({
  to: {
    type: String,
    required: false, // Button kann auch ohne Route existieren
  },
});

const router = useRouter();

function navigate() {
  if (props.to) {
    router.push(props.to);
  }
}
</script>

<style scoped>
.btn {
  border-color: #4BA1B1;
  background-color: white;
}
</style>