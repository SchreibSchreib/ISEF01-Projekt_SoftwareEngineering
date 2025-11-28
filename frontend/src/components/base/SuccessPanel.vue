<template>
    <div v-if="visible"
        class="alert border-4 border-success-subtle bg-success-subtle rounded-4 fw-bold mt-2 py-3 text-center fade show"
        role="alert">
        {{ message }}
    </div>
</template>

<script setup>
import { ref, watch } from "vue";

const props = defineProps({
    message: String,
    show: Boolean,
    duration: { type: Number, default: 3000 },
    persistent: { type: Boolean, default: false }
});

const emit = defineEmits(["hide"]);
const visible = ref(false);

watch(
    () => props.show,
    (newValue) => {
        if (newValue) {
            visible.value = true;

            if (!props.persistent) {
                setTimeout(() => {
                    visible.value = false;
                    emit("hide");
                }, props.duration);
            }
        }
    }
);
</script>

<style scoped></style>