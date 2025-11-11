<template>
  <!-- 🔹 Navbar für kleine Bildschirme) -->
  <nav class="navbar navbar-expand-lg d-md-none fixed-top py-0 border-0 shadow-none">
    <div class="container-fluid px-0">
      <button class="navbar-toggler m-2" type="button" data-bs-toggle="collapse"
        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false"
        aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse bg-body align-content-start" id="navbarSupportedContent">
        <div class="container-nav d-inline-flex px-0 mx-0">
          <ul class="navbar-nav ps-2">
            <li class="nav-item">
              <NavbarButton icon="gauge">Dashboard</NavbarButton>
            </li>
            <li class="nav-item">
              <NavbarButton icon="graduation-cap">Quiz starten</NavbarButton>
            </li>
            <li class="nav-item">
              <NavbarButton icon="heart-handshake">Kooperation</NavbarButton>
            </li>
            <li class="nav-item">
              <NavbarButton icon="book-copy">Fragenkatalog</NavbarButton>
            </li>
            <li class="nav-item">
              <NavbarButton icon="bot">Team</NavbarButton>
            </li>
            <li class="nav-item ">
              <NavbarButton icon="settings">Profil/Einstellungen</NavbarButton>
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
          <NavbarButton icon="gauge">
            Dashboard
          </NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton icon="graduation-cap">Quiz starten</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton icon="heart-handshake">Kooperation</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton icon="book-copy">Fragenkatalog</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton icon="bot">Team</NavbarButton>
        </li>
        <li class="nav-item mt-auto mb-5">
          <NavbarButton icon="settings">Profil/Einstellungen</NavbarButton>
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
}

.navbar {
  background-color: #264352;
  min-width: 250px;
}

.content {
  background-color: #F2F4F5;
}
</style>