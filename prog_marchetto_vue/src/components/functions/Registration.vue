<template>
  <section>
    <header>
      <h3>Registrazione {{ role }}</h3>
    </header>

    <b-form @submit.prevent="registration" @reset="reset">
      <Credenziali
        :required="true"
        :ruolo="role"
        :username.sync="username"
        :password.sync="password"
      />

      <UserInfo :required="true" :name.sync="name" :email.sync="email" />

      <Logo v-if="role === 'uploader'" :required="true" :logo.sync="logo" />

      <b-button type="submit" variant="success">Registrazione</b-button>
      <b-button type="reset" variant="danger">Reset</b-button>
    </b-form>
    <Spinner :attesa="attesa" />
  </section>
</template>

<script>
import Spinner from "@/components/layout/Spinner";
import Credenziali from "@/components/input/Credenziali";
import UserInfo from "@/components/input/UserInfo";
import Logo from "@/components/input/Logo";

import { formUtente } from "@/utils/utils";

import axios from "axios";
axios.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem(
  "jwtToken"
)}`;

export default {
  name: "Registration",
  mixins: [formUtente],
  props: ["potere", "role"],
  components: {
    Credenziali,
    UserInfo,
    Logo,
    Spinner,
  },
  data() {
    return { attesa: false };
  },
  methods: {
    registration() {
      this.attesa = true;
      axios
        .post(`${process.env.VUE_APP_APIROOT}/attori/registration`, {
          username: this.username,
          password: this.password,
          name: this.name,
          email: this.email,
          role: this.role,
          logo: this.logo,
        })
        .then((res) => {
          const nuovoUtente = {
            username: this.username,
            password: this.password,
            name: this.name,
            email: this.email,
            role: this.role,
            logo: this.logo,
          };
          this.$emit("registrazione_utente", nuovoUtente);
          this.$emit("registrazione", res.data);
          this.reset();
        })
        .catch((err) => {
          this.$emit("registrazione", err.response.data);
        })
        .finally(() => {
          this.attesa = false;
        });
    },
  },
};
</script>
