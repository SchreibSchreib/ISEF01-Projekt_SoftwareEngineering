<template>
    <button class="btn btn-primary w-100 p-3">
        <slot></slot>
    </button>
</template>

<script setup>

</script>

<style scoped>
.btn-primary {
    background-color: #264352;
    border-color: #264352;
}

button:focus,
button:active {
    outline: none !important;
    box-shadow: none !important;
}
</style>