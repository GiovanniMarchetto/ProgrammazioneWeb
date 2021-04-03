<template>
  <b-button @click="logout" variant="danger">Logout</b-button>
</template>

<script>
export default {
  methods: {
    logout() {
      localStorage.removeItem("jwtToken");
      localStorage.removeItem("nomeUtente");
      this.$emit("logout");
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
.btn {
  margin: 0;
}
</style>
