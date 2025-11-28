<template>
    <div class="container py-5">
        <h1 class="fw-bold pb-3">Hallo {{ userStore.currentUser?.name }}</h1>

        <AppBoxWithShadow class="px-5">

            <div class="row">
                <div class="d-flex align-items-center justify-content-center gap-4 mb-3">
                    <img src="@/assets/circle-user-round.png" alt="avatar" style="width:120px; opacity:0.8;" />
                    <button class="btn btn-outline-dark rounded-4 px-4 py- fw-bold">
                        Bild ändern
                    </button>
                </div>

                <!-- LINKE SPALTE -->
                <div class="col-12 col-lg-7">
                    <label class="fw-bold">Anzeigename:</label>
                    <AppInput type="text" class="form-control rounded-4 mb-3 p-3 fs-5 border-3"
                        :value="userStore.currentUser?.name" />

                    <label class="fw-bold">Hinterlegte E-Mail:</label>
                    <AppInput type="text" class="form-control rounded-4 mb-3 p-3 fs-5 border-3"
                        value="Es wurde noch keine E-Mail hinterlegt!" disabled />

                    <label class="fw-bold">Passwort ändern:</label>

                    <AppInput type="password" class="form-control rounded-4 mb-3 p-3 fs-5 border-3"
                        placeholder="Aktuelles Passwort" />

                    <AppInput type="password" class="form-control rounded-4 mb-3 p-3 fs-5 border-3"
                        placeholder="Neues Passwort" />

                    <AppInput type="password" class="form-control rounded-4 mb-4 p-3 fs-5 border-3"
                        placeholder="Neues Passwort wiederholen" />

                </div>

                <!-- RECHTE SPALTE -->
                <div class="col-12 col-lg-5 mt-4">

                    <AppBox class="rounded-4 border border-3 mb-3 " style="border-color:#1fa1a8 !important;">

                        <AppBox class="setting-item border-black mb-3">
                            <span class="fw-bold">Dark-Modus aktivieren</span>
                            <AppCheckbox v-model="darkMode"></AppCheckbox>
                        </AppBox>

                        <AppBox class="setting-item border-black mb-3">
                            <span class="fw-bold">Benachrichtigungen aktivieren</span>
                            <AppCheckbox v-model="notifications"></AppCheckbox>
                        </AppBox>

                        <AppButton class="fw-bold rounded-4">
                            Daten exportieren
                        </AppButton>

                    </AppBox>

                    <button class="resetButton btn bg-warning-subtle mt-3 w-100 py-3 rounded-4 mb-3 fw-bold border border-dark">
                        Fortschritt zurücksetzen
                    </button>

                    <button class="deleteButton btn mt-1 w-100 py-3 rounded-4 mb-4 fw-bold border border-dark">
                        Konto löschen
                    </button>

                </div>
            </div>

            <!-- FOOTER BUTTONS / OUTSIDE OF ROW -->
            <div class="d-flex flex-column flex-md-row justify-content-end gap-3 mt-2 footer-buttons-container">

                <AppButton class="btn btn-dark px-5 py-3 rounded-4 fw-bold">
                    Abbrechen
                </AppButton>
                <AppButton class="btn btn-dark px-5 py-3 rounded-4 fw-bold">
                    Speichern
                </AppButton>
            </div>

        </AppBoxWithShadow>
    </div>
</template>

<script setup>
import { userStore } from '@/stores/userStore';
import AppBoxWithShadow from '@/components/base/AppBoxWithShadow.vue';
import AppInput from '@/components/base/AppInput.vue';
import AppBox from '@/components/base/AppBox.vue';
import AppButton from '@/components/base/AppButton.vue';
import AppCheckbox from '@/components/base/AppCheckbox.vue';
</script>

<style scoped>
.setting-item {
    border: 2px solid #1fa1a8;
    border-radius: 14px;
    padding: 12px 16px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
.deleteButton{
    background-color: rgba(255, 0, 0, 0.61);
}
</style>
