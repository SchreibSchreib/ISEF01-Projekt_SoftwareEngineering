<template>
    <div class="d-flex align-items-start gap-3 border border-3 rounded-4 bg-white">

        <div class="d-flex align-items-center gap-3 p-3 border rounded-4"
     >
    
    <img src="../../assets/lightbulb.png"
         alt="Hinweis"
         style="width:40px; height:40px;">

    <p class="m-0 fs-5">
        Die Beschaffung umfasst alle Aktivitäten, um Materialien,
        Rohstoffe oder Dienstleistungen von außen zu beziehen.
    </p>

</div>
    </div>
</template>

<script setup>
const props = defineProps({
    explanation: {
        type: String,
        required: true
    }
});
</script>

<style scoped>
.border-3 {
    border-color: #34d6d6 !important;
}
</style>
