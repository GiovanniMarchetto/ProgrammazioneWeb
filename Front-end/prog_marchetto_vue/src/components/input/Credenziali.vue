<template>
  <b-container fluid>
    <b-form-group label="Username:" :label-for="usernameInputId">
      <b-form-input
        :id="usernameInputId"
        type="text"
        name="usernameInput"
        maxlength="100"
        v-model="usernameInput"
        placeholder="username"
        required
      />
      <b-form-text v-show="ruolo === 'consumer'">
        Codice fiscale di 16 cifre
      </b-form-text>
      <b-form-text v-show="ruolo === 'uploader'">
        Codice alfa-numerico di 4 caratteri
      </b-form-text>
      <b-form-text v-show="ruolo === 'administrator'">
        Dev'essere una email
      </b-form-text>
      <b-form-text v-show="ruolo === 'modifica'">
        Immettere l'username dell'utente da modificare (se omesso verrà
        considerato l'username dell'utente loggato).
      </b-form-text>
    </b-form-group>

    <b-form-group label="Password:" :label-for="passwordInputId">
      <b-form-input
        :id="passwordInputId"
        type="password"
        name="passwordInput"
        v-model="passwordInput"
        placeholder="password"
        required
      />
    </b-form-group>
  </b-container>
</template>

<script>
import { v4 as uuidv4 } from "uuid";

export default {
  name: "Credenziali",
  props: ["required", "ruolo", "username", "password"],
  data() {
    return {
      usernameInputId: "",
      passwordInputId: "",
    };
  },
  computed: {
    usernameInput: {
      get: function() {
        return this.username;
      },
      set: function(newValue) {
        this.$emit("update:username", newValue);
      },
    },
    passwordInput: {
      get: function() {
        return this.password;
      },
      set: function(newValue) {
        this.$emit("update:password", newValue);
      },
    },
  },
  mounted() {
    if (this.required !== true) {
      document.getElementById(this.usernameInputId).removeAttribute("required");
      document.getElementById(this.passwordInputId).removeAttribute("required");
    }
  },
  created() {
    this.usernameInputId = uuidv4();
    this.passwordInputId = uuidv4();
  },
};
</script>

<style></style>
