<template>
  <b-container fluid>
    <b-form-group label="Logo:" :label-for="id">
      <b-form-file
        @change.native="trasf64"
        :id="id"
        type="file"
        name="logoInput"
        v-model="logoInput"
        placeholder="Scegli un immagine o trascinala qui..."
        accept="image/*"
        required
      />

      <figure v-show="logoStringInput">
        <b-img
          center
          rounded="circle"
          :src="`${logoStringInput}`"
          alt="Anteprima Logo"
        />
      </figure>
    </b-form-group>
  </b-container>
</template>

<script>
import { v4 as uuidv4 } from "uuid";

export default {
  name: "Logo",
  props: ["required", "logo"],
  data() {
    return {
      logoInput: null,
      id: "",
    };
  },
  watch: {
    logoStringInput: function() {
      if (this.logoStringInput === "") this.logoInput = null;
    },
  },
  methods: {
    trasf64(imgObj) {
      const reader = new FileReader();
      reader.onload = (elem) => {
        this.logoStringInput = elem.target.result;
      };
      reader.onerror = function(error) {
        console.log("Error: ", error);
      };
      reader.readAsDataURL(imgObj.target.files[0]);
    },
  },
  computed: {
    logoStringInput: {
      get: function() {
        return this.logo;
      },
      set: function(newValue) {
        this.$emit("update:logo", newValue);
      },
    },
  },
  mounted() {
    if (this.required != true)
      document.getElementById(this.id).removeAttribute("required");
  },
  created() {
    this.id = uuidv4();
  },
};
</script>

<style scoped>
img {
  margin: 10px;
  width: 150px;
  height: 150px;
}
</style>
