<template>
  <div 
    class="app-box shadow-sm" 
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
  border-radius: 20px;
  padding: 28px;
  background: white;
}
</style>
