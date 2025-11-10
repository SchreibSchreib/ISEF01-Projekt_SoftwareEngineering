<template>
  <!-- 🔹 Navbar für kleine Bildschirme) -->
  <nav class="navbar navbar-expand-lg d-md-none fixed-top">
    <div class="container-fluid">
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <a class="navbar-brand" href="#">Meine App</a>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="#">Home</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Link</a>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              Dropdown
            </a>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item" href="#">Action</a></li>
              <li><a class="dropdown-item" href="#">Another action</a></li>
              <li>
                <hr class="dropdown-divider" />
              </li>
              <li><a class="dropdown-item" href="#">Something else here</a></li>
            </ul>
          </li>
          <li class="nav-item">
            <a class="nav-link disabled" aria-disabled="true">Disabled</a>
          </li>
        </ul>
        <form class="d-flex" role="search">
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" />
          <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
      </div>
    </div>
  </nav>

  <!-- 🔹 Fixierte Sidebar für große Bildschirme -->
  <nav class="navbar navbar-expand-md flex-column vh-100 position-fixed d-none d-md-flex  ">
    <div class="container-fluid flex-column pe-0">
      <ul class="navbar-nav flex-column w-100 ">
        <li class="nav-item d-flex justify-content-end pt-5">
          <NavbarButton>Dashboard</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton>Dashboard</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton>Dashboard</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton>Dashboard</NavbarButton>
        </li>
        <li class="nav-item">
          <NavbarButton>Dashboard</NavbarButton>
        </li>
      </ul>
    </div>
  </nav>

</template>

<script setup>
import NavbarButton from '@/components/base/NavbarButton.vue';
</script>

<style scoped>
.navbar {
  background-color: #264352;
  min-width: 250px;
}

.content {
  background-color: #F2F4F5;
}
</style>