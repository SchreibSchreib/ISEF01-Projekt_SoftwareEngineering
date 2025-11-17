<template>
  <!-- 🔹 Navbar für kleine Bildschirme) -->
  <nav class="navbar navbar-expand-lg d-md-none fixed-top py-0 border-0 shadow-none">
    <div class="container-fluid px-0">
      <button class="navbar-toggler m-2" type="button" data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
        aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse small-collapse" id="navbarSupportedContent">
        <div class="container-nav px-0 mx-0">
          <ul class="navbar-nav ps-2">
            <li class="nav-item">
              <NavbarButton icon="gauge" to="dashboard">Dashboard</NavbarButton>
            </li>
            <li class="nav-item">
              <NavbarButton icon="graduation-cap" to="startquiz">Quiz starten</NavbarButton>
            </li>
            <li class="nav-item">
              <NavbarButton icon="heart-handshake" to="cooperate">Kooperation</NavbarButton>
            </li>
            <li class="nav-item">
              <NavbarButton icon="book-copy" to="questions">Fragenkatalog</NavbarButton>
            </li>
            <li class="nav-item">
              <NavbarButton icon="bot" to="team">Team</NavbarButton>
            </li>
            <li class="nav-item ">
              <NavbarButton icon="settings" to="settings">Profil/Einstellungen</NavbarButton>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </nav>

  <!-- 🔹 Fixierte Sidebar für große Bildschirme -->
  <nav class="navbar navbar-expand-md flex-column vh-100 position-fixed d-none d-md-flex  ">
    <div class="container-fluid flex-column h-100 pt-5 pb-5 pe-0">
      <ul class="navbar-nav flex-column ps-3 w-100 h-100">
        <li class="nav-item pt-5">
          <NavbarButton icon="gauge" to="dashboard">Dashboard</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton icon="graduation-cap" to="startquiz">Quiz starten</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton icon="heart-handshake" to="cooperate">Kooperation</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton icon="book-copy" to="questions">Fragenkatalog</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton icon="bot" to="team">Team</NavbarButton>
        </li>
        <li class="nav-item mt-auto mb-5">
          <NavbarButton icon="settings" to="settings">Profil/Einstellungen</NavbarButton>
        </li>
      </ul>
    </div>
  </nav>

</template>

<script setup>
import NavbarButton from '@/components/base/NavbarButton.vue';
</script>

<style scoped>
.container-nav {
  background-color: #264352;
  ;
}

.navbar {
  background-color: #264352;
}

.small-collapse {
  position: absolute;
  top: 100%;
  left: 0;

  width: auto !important;
  max-width: fit-content !important;

  background-color: inherit;
  z-index: 1000;
}

.small-collapse .navbar-nav {
  text-align: left;
}

.container-nav {
  display: block !important;
}

.content {
  background-color: #F2F4F5;
}
</style>