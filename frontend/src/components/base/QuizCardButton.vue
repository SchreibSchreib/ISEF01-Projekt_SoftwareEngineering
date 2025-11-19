<template>
    <button class="quiz-card-btn btn w-100 border-2 rounded-4 px-3 py-3 d-flex align-items-center gap-3"
        :class="buttonClasses" :disabled="disabled">
        <span class="fw-bold">
            <slot name="letter"></slot>
        </span>

        <span>
            <slot name="answer"></slot>
        </span>
    </button>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
    state: { type: String, default: "neutral" },
    disabled: { type: Boolean, default: false }
});

const buttonClasses = computed(() => {
    return {
        "btn-correct": props.state === "correct",
        "btn-wrong": props.state === "wrong",
        "btn-neutral": props.state === "neutral"
    };
});
</script>

<style scoped>
.quiz-card-btn {
    border-color: #250b0b !important;
    background: white;
    transition: 0.2s;
}

/* Hover nur bei neutral */
.btn-neutral:hover {
    background: #e9ffff;
}

/* Richtig */
.btn-correct {
    background: #aaffdd !important;
    border-color: #00aa55 !important;
}

/* Falsch */
.btn-wrong {
    background: #ff9988 !important;
    border-color: #cc0000 !important;
}
</style>
