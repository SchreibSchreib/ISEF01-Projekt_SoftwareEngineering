<template>
    <button class="btn btn-outline-info selector-btn border-3 rounded-4" :class="{ activeBtn: active }"
        @click="$emit('select', letter)">
        {{ letter }}
    </button>
</template>

<script setup>
const props = defineProps({
    letter: {
        type: String,
        required: true
    },
    active: {
        type: Boolean,
        default: false
    }
});

defineEmits(['select']);
</script>

<style scoped>
.selector-btn {
    width: 50px;
    height: 50px;
    border-color: #1fa1a8;
}

.activeBtn {
    background-color: #79e8cd !important;
    color: white !important;
}
</style>