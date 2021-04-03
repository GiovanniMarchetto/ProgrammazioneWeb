<template>
  <section>
    <header>
      <h3>Login</h3>
    </header>

    <b-form @submit.prevent="login">
      <Credenziali
        :required="true"
        :username.sync="username"
        :password.sync="password"
      />
      <b-button type="submit" variant="primary">Login</b-button>
    </b-form>

    <Spinner :attesa="attesa" />
  </section>
</template>

<script>
import Credenziali from "@/components/input/Credenziali";
import Spinner from "@/components/layout/Spinner";
import axios from "axios";
export default {
  name: "Login",
  components: {
    Credenziali,
    Spinner,
  },
  data() {
    return {
      username: "",
      password: "",
      attesa: false,
    };
  },
  methods: {
    login() {
      this.attesa = true;
      axios
        .post(`${process.env.VUE_APP_APIROOT}/login`, {
          username: this.username,
          password: this.password,
        })
        .then((res) => {
          localStorage.setItem("jwtToken", res.data);
          localStorage.setItem("nomeUtente", this.username);
          this.$emit("login", "Login eseguito - " + this.username);
          this.$emit("login_username", this.username);
        })
        .catch((err) => {
          this.$emit("login", err.response.data);
        })
        .finally(() => {
          this.attesa = false;
        });
    },
  },
};
</script>
