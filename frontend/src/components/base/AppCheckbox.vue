<template>
    <div class="form-check">
        <input class="form-check-input" type="checkbox" :id="id" :checked="modelValue"
            @change="$emit('update:modelValue', $event.target.checked)">
        <label class="form-check-label" :for="id">
            <slot></slot>
        </label>
    </div>
</template>

<script setup>
const props = defineProps({
    modelValue: Boolean,
    id: {
        type: String,
        default: "checkbox-" + Math.random().toString(36).substring(2, 9)
    }
});

const emits = defineEmits(["update:modelValue"]);
</script>

<style scoped>
.form-check-input {
    width: 25px;
    height: 25px;
}
</style>