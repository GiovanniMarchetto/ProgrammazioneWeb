<template>
  <section>
    <header>
      <h3>Modifica informazioni {{ role }}</h3>
    </header>

    <b-form @submit.prevent="modInfo" @reset="reset">
      <Credenziali
        v-show="potere != 'consumer'"
        :required="false"
        :ruolo="'modifica'"
        :username.sync="username"
        :password.sync="password"
      />

      <UserInfo :required="false" :name.sync="name" :email.sync="email" />

      <Logo v-if="role === 'uploader'" :required="false" :logo.sync="logo" />

      <b-button type="submit" variant="success">Modifica informazioni</b-button>
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
  name: "ModInfo",
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
    modInfo() {
      this.attesa = true;
      axios
        .post(`${process.env.VUE_APP_APIROOT}/attori/modInfo`, {
          username: this.username,
          password: this.password,
          name: this.name,
          email: this.email,
          role: this.role,
          logo: this.logo,
        })
        .then((res) => {
          const modUtente = {
            utente: res.data,
            role: this.role,
          };
          this.$emit("modInfo_utente", modUtente);
          if (this.username !== "")
            this.$emit(
              "modInfo",
              "Modifica attore eseguita - " + this.username
            );
          else
            this.$emit(
              "modInfo",
              "Modifica delle proprie informazioni eseguita"
            );
          this.reset();
        })
        .catch((err) => {
          this.$emit("modInfo", err.response.data);
        })
        .finally(() => {
          this.attesa = false;
        });
    },
  },
};
</script>

<style scoped></style>
