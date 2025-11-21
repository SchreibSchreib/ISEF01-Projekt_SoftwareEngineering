<template>
    <input type="text" :value="modelValue" @input="$emit('update:modelValue', $event.target.value)"
        class="form-control border-2 rounded-4 py-3" />
</template>

<script setup>
defineProps({
    modelValue: String,
})
</script>

<style scoped>
.form-control {
    border-color: #3B5B69;
}
</style>