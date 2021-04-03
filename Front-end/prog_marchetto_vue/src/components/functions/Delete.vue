<template>
  <section>
    <header>
      <h3>Eliminazione</h3>
    </header>
    
    <b-container fluid>
      <h4>Elimina attore</h4>
      <b-form @submit.prevent="deleteActor">
        <label for="username">Username attore da eliminare</label>
        <b-form-input
          list="attori-list"
          id="username"
          type="text"
          name="username"
          v-model="username"
          placeholder="username"
          required
        />
        <b-form-datalist
          id="attori-list"
          :options="attoriOptions"
        ></b-form-datalist>
        <b-form-text>
          Non puoi cancellare il tuo account
        </b-form-text>
        <b-button type="submit" variant="danger">Elimina utente</b-button>
      </b-form>
    </b-container>

    <hr />

    <b-container fluid v-if="potere === 'uploader'">
      <h4>Elimina file</h4>

      <b-form @submit.prevent="deleteFile">
        <label for="fileId">Identificativo file da eliminare</label>
        <b-form-input
          list="fileId-list"
          id="fileId"
          type="text"
          name="fileId"
          v-model="fileId"
          placeholder="id file"
          required
        ></b-form-input>
        <b-form-datalist
          id="fileId-list"
          :options="fileOptions"
        ></b-form-datalist>
        <b-form-text>
          Puoi cancellare solo i file da te caricati.
        </b-form-text>

        <b-button type="submit" variant="danger">Elimina file</b-button>
      </b-form>
    </b-container>
    <Spinner :attesa="attesa" />
  </section>
</template>

<script>
import Spinner from "@/components/layout/Spinner";
import axios from "axios";
axios.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem(
  "jwtToken"
)}`;

export default {
  name: "Delete",
  components: { Spinner },
  props: ["potere", "attoriOptions", "fileOptions"],
  data() {
    return {
      username: "",
      fileId: "",
      attesa: false,
    };
  },
  methods: {
    deleteActor() {
      this.attesa = true;
      axios
        .delete(`${process.env.VUE_APP_APIROOT}/attori/delete/${this.username}`)
        .then((res) => {
          this.$emit("delete_username", this.username);
          this.username = "";
          this.$emit("delete", res.data);
        })
        .catch((err) => {
          this.$emit("delete", err.response.data);
        })
        .finally(() => {
          this.attesa = false;
        });
    },
    deleteFile() {
      this.attesa = true;
      axios
        .delete(`${process.env.VUE_APP_APIROOT}/files/delete/${this.fileId}`)
        .then((res) => {
          this.$emit("delete_file", this.fileId);
          this.fileId = "";
          this.$emit("delete", res.data);
        })
        .catch((err) => {
          this.$emit("delete", err.response.data);
        })
        .finally(() => {
          this.attesa = false;
        });
    },
  },
};
</script>
