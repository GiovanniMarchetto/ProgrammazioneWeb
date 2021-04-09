<template>
  <b-container fluid>
    <b-form-group label="Name:" :label-for="nameInputId">
      <b-form-input
        :id="nameInputId"
        type="text"
        name="nameInput"
        maxlength="100"
        v-model="nameInput"
        placeholder="name"
        required
      />
    </b-form-group>

    <b-form-group label="Email:" :label-for="emailInputId">
      <b-form-input
        :id="emailInputId"
        type="email"
        name="emailInput"
        maxlength="100"
        v-model="emailInput"
        placeholder="email"
        required
      />
    </b-form-group>
  </b-container>
</template>

<script>
import { v4 as uuidv4 } from "uuid";

export default {
  name: "UserInfo",
  props: ["required", "name", "email"],
  data() {
    return {
      nameInputId: "",
      emailInputId: "",
    };
  },
  computed: {
    nameInput: {
      get: function() {
        return this.name;
      },
      set: function(newValue) {
        this.$emit("update:name", newValue);
      },
    },
    emailInput: {
      get: function() {
        return this.email;
      },
      set: function(newValue) {
        this.$emit("update:email", newValue);
      },
    },
  },
  mounted() {
    if (this.required != true) {
      document.getElementById(this.nameInputId).removeAttribute("required");
      document.getElementById(this.emailInputId).removeAttribute("required");
    }
  },
  created() {
    this.nameInputId = uuidv4();
    this.emailInputId = uuidv4();
  },
};
</script>

<style></style>
