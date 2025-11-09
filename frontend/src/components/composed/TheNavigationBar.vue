<template>
    <!-- 🔹 Navbar für kleine Bildschirme (Topbar) -->
<nav class="navbar navbar-expand-lg d-lg-none fixed-top">
  <div class="container-fluid">
    <button
      class="navbar-toggler"
      type="button"
      data-bs-toggle="collapse"
      data-bs-target="#navbarSupportedContent"
      aria-controls="navbarSupportedContent"
      aria-expanded="false"
      aria-label="Toggle navigation"
    >
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
          <a
            class="nav-link dropdown-toggle"
            href="#"
            role="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            Dropdown
          </a>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="#">Action</a></li>
            <li><a class="dropdown-item" href="#">Another action</a></li>
            <li><hr class="dropdown-divider" /></li>
            <li><a class="dropdown-item" href="#">Something else here</a></li>
          </ul>
        </li>
        <li class="nav-item">
          <a class="nav-link disabled" aria-disabled="true">Disabled</a>
        </li>
      </ul>
      <form class="d-flex" role="search">
        <input
          class="form-control me-2"
          type="search"
          placeholder="Search"
          aria-label="Search"
        />
        <button class="btn btn-outline-success" type="submit">Search</button>
      </form>
    </div>
  </div>
</nav>

<!-- 🔹 Vertikale Navbar (Sidebar) für große Bildschirme -->
<nav class="navbar navbar-expand-lg flex-column align-items-start vh-100 position-fixed d-none d-lg-flex">
  <div class="container-fluid flex-column">
    <a class="navbar-brand mb-4" href="#">Meine App</a>
    <ul class="navbar-nav flex-column w-100">
      <li class="nav-item">
        <a class="nav-link active" href="#">Home</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#">Link</a>
      </li>
      <li class="nav-item dropdown">
        <a
          class="nav-link dropdown-toggle"
          href="#"
          role="button"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          Dropdown
        </a>
        <ul class="dropdown-menu">
          <li><a class="dropdown-item" href="#">Action</a></li>
          <li><a class="dropdown-item" href="#">Another action</a></li>
          <li><hr class="dropdown-divider" /></li>
          <li><a class="dropdown-item" href="#">Something else here</a></li>
        </ul>
      </li>
      <li class="nav-item">
        <a class="nav-link disabled" aria-disabled="true">Disabled</a>
      </li>
    </ul>
  </div>
</nav>

<!-- 🔹 Hauptinhalt -->
<div class="content container-fluid vh-100">
  <div class="row">
    <!-- Sidebar-Spalte -->
    <div class="col-lg-3 col-xl-2 d-none d-lg-block"></div>

    <!-- Inhalt-Spalte -->
    <main class="col-12 col-lg-9 col-xl-10 pt-5 pt-lg-0">
      <div class="p-4">
        <h1>Dashboard</h1>
        <p>
          Auf großen Bildschirmen steht das Menü links.<br />
          Auf kleineren Geräten erscheint es oben als ausklappbare Navbar.
        </p>
      </div>
    </main>
  </div>
</div>

</template>

<script>

</script>

<style scoped>
.navbar {
  background-color: #264352;
}

.content {
  background-color: #F2F4F5;
}
</style>