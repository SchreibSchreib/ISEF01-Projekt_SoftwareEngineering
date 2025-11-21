<template>
    <button class="btn btn-primary w-100 rounded-3 rounded-end-0 py-3 d-flex align-items-center justify-content-start" @click="handleClick">
        <div class="d-flex ps-3">
            <img v-if="iconSrc" :src="iconSrc" alt="" class="me-2 icon-white" />
            <slot></slot>
        </div>
    </button>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { computed } from 'vue'

const router = useRouter()

const props = defineProps({
    icon: {
        type: String,
        default: null
    },
    to: {
        type: String,
        default: null
    }
})

// Router-Navigation bei Klick
const handleClick = () => {
    if (props.to) {
        router.push(props.to)
    }
}

// Bildpfad zum Icon berechnen
const iconSrc = computed(() => {
    if (!props.icon) return null
    return new URL(`../../assets/${props.icon}.png`, import.meta.url).href
})
</script>

<style scoped>
.btn {
    margin-bottom: 5px;
    background-color: #3B5B69;
    color: white;
    border: none;
}

.icon-white {
    filter: invert(1);
}
</style>
