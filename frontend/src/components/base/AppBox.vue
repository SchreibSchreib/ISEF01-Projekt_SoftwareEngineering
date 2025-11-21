<template>
  <div 
    class="app-box" 
    :style="{
      maxWidth: max ? max + 'px' : '100%',
    }"
  >
    <slot ></slot>
  </div>
</template>

<script setup>
const props = defineProps({
  max: {
    type: Number,
    default: null
  }
});
</script>

<style scoped>
.app-box {
  border: 2px solid #1fa1a8;
  border-radius: 20px;
  padding: 20px;
  background: white;
}
</style>
