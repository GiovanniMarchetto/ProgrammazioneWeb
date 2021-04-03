<template>
  <header>
    <h2><slot>Pagina</slot></h2>

    <b-navbar toggleable="lg" type="dark" variant="dark">
      <b-navbar-brand @click="$emit('mostraSezione', '')"
        >Barra di navigazione</b-navbar-brand
      >

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav class="ml-auto">
          <b-nav-item @click="$emit('mostraSezione', '')">{{
            nomePrimaLista
          }}</b-nav-item>

          <b-nav-item @click="$emit('mostraSezione', 'secondaLista')">{{
            nomeSecondaLista
          }}</b-nav-item>

          <b-nav-item-dropdown
            v-if="potere === 'administrator'"
            text="Registra utente"
          >
            <b-dropdown-item
              @click="
                $emit('mostraSezione', 'registration'),
                  $emit('ruoloForm', 'administrator')
              "
              >Administrator</b-dropdown-item
            >
            <b-dropdown-item
              @click="
                $emit('mostraSezione', 'registration'),
                  $emit('ruoloForm', 'uploader')
              "
              >Uploader</b-dropdown-item
            >
          </b-nav-item-dropdown>
          <b-nav-item
            v-if="potere === 'uploader'"
            @click="$emit('mostraSezione', 'registration')"
            >Creare Consumer</b-nav-item
          >

          <b-nav-item
            v-if="potere === 'consumer'"
            @click="$emit('mostraSezione', 'modInfo')"
            >Modifica informazioni</b-nav-item
          >
          <b-nav-item-dropdown v-else text="Modifica utente">
            <b-dropdown-item
              v-if="potere === 'administrator'"
              @click="
                $emit('mostraSezione', 'modInfo'),
                  $emit('ruoloForm', 'administrator')
              "
              >Administrator</b-dropdown-item
            >
            <b-dropdown-item
              @click="
                $emit('mostraSezione', 'modInfo'),
                  $emit('ruoloForm', 'uploader')
              "
              >Uploader</b-dropdown-item
            >
            <b-dropdown-item
              v-if="potere === 'uploader'"
              @click="
                $emit('mostraSezione', 'modInfo'),
                  $emit('ruoloForm', 'consumer')
              "
              >Consumer</b-dropdown-item
            >
          </b-nav-item-dropdown>

          <b-nav-item
            v-if="potere === 'uploader'"
            @click="$emit('mostraSezione', 'upload')"
            >Upload file</b-nav-item
          >

          <b-nav-item
            v-if="potere !== 'consumer'"
            @click="$emit('mostraSezione', 'delete')"
            >Elimina</b-nav-item
          >

          <b-nav-item><Logout /></b-nav-item>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </header>
</template>

<script>
import Logout from "@/components/functions/Logout";

export default {
  name: "Navbar",
  props: ["potere", "nomePrimaLista", "nomeSecondaLista"],
  components: {
    Logout,
  },
};
</script>

<style scoped></style>
