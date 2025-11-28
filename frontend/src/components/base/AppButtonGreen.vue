<template>
    <button class="btn btn-success w-100 p-3">
        <slot></slot>
    </button>
</template>

<script setup>

</script>

<style scoped>
.btn-primary {
    background-color: #3B5B69;
    border-color: #3B5B69;
}

button:focus,
button:active {
    outline: none !important;
    box-shadow: none !important;
}
</style>