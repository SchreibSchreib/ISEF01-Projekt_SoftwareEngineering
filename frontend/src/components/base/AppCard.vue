<template lang="">
  <div class="container d-flex justify-content-center py-5">
    <div class="card w-100 mb-3 rounded-5 shadow-lg" style="max-width: 800px;">
      <div class="card-header rounded-top-5">
        <slot name="header"></slot>
      </div>
      <div class="card-body rounded-bottom-5">
        <slot name="body"></slot>
      </div>
    </div>
  </div>
</template>
<script setup>

</script>
<style scoped>
.card-header {
    background-color: #4BA1B1;
    color: #FFFFFF;
}

.card-body {
    background-color: #FFFFFF;
}
</style>