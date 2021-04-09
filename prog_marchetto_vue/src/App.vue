<template>
  <div id="app">
    <header>
      <img id="logoPagina" src="./assets/logo.png" alt="Logo sito" />
      <h1>Progetto Marchetto</h1>
    </header>
    <router-view />
    <Footer />
  </div>
</template>

<script>
import Footer from "./components/layout/Footer";

export default {
  name: "App",
  components: {
    Footer,
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
}

header {
  padding: 10px;
  color: #fff;
  background-color: #343a40;
}
section > header {
  padding: 0;
}

#logoPagina {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 64px;
  height: 64px;
}

@media all and (max-width: 500px) {
  h1 {
    padding-left: 64px;
  }
}

h3 {
  padding: 10px;
  color: #fff;
  background-color: #e66648;
}

h4 {
  padding: 10px;
  background-color: beige;
}

form {
  padding: 10px;
  border: 1px solid #2c3e50;
  text-align: left;
  background-color: aliceblue;

  display: flex;
  flex-direction: column;
  max-width: 600px;
  margin: 0 auto;
}

.btn {
  margin: 10px;
}
</style>
