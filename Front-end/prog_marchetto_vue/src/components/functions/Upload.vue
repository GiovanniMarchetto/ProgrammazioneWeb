<template>
  <section>
    <header>
      <h3>Caricamento file</h3>
    </header>

    <b-form @submit.prevent="upload" @reset.prevent="reset">
      <FileInput
        :file.sync="file"
        :nameFile.sync="nameFile"
        :hashtag.sync="hashtag"
        :extension.sync="extension"
      />

      <b-container fluid>
        <b-form-group
          id="usernameCons-group"
          label="Username consumer:"
          label-for="usernameCons"
        >
          <b-form-input
            id="usernameCons"
            type="text"
            name="usernameCons"
            maxlength="100"
            v-model="usernameCons"
            placeholder="username consumer"/></b-form-group
      ></b-container>
      <UserInfo
        :required="false"
        :name.sync="nameCons"
        :email.sync="emailCons"
      />

      <b-button type="submit" variant="success">Upload</b-button>
      <b-button type="reset" variant="danger">Reset</b-button>
    </b-form>

    <Spinner :attesa="attesa" />
  </section>
</template>

<script>
import Spinner from "@/components/layout/Spinner";
import FileInput from "../input/FileInput";
import UserInfo from "../input/UserInfo";

import axios from "axios";
if (localStorage.getItem("jwtToken") != null)
  axios.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem(
    "jwtToken"
  )}`;

export default {
  name: "Upload",
  components: {
    FileInput,
    UserInfo,
    Spinner,
  },
  data() {
    return {
      file: "",
      nameFile: "",
      hashtag: "",
      extension: "",
      usernameCons: "",
      nameCons: "",
      emailCons: "",
      attesa: false,
    };
  },
  methods: {
    reset() {
      this.file = "";
      this.extension = "";
      this.nameFile = "";
      this.hashtag = "";
      this.usernameCons = "";
      this.nameCons = "";
      this.emailCons = "";
    },
    upload() {
      this.attesa = true;
      axios
        .post(`${process.env.VUE_APP_APIROOT}/files/upload`, {
          file: this.file,
          nameFile: this.nameFile + this.extension,
          hashtag: this.hashtag,
          usernameCons: this.usernameCons,
          nameCons: this.nameCons,
          emailCons: this.emailCons,
        })
        .then((res) => {
          const consumer = {
            username: this.usernameCons,
            name: this.nameCons,
            email: this.emailCons,
            logo: "",
          };
          this.$emit("upload_consumer", consumer);

          this.$emit("upload_file", res.data);
          this.$emit(
            "upload",
            "Upload file completato (" + this.nameFile + ")"
          );
          this.reset();
        })
        .catch((err) => {
          this.$emit("upload", err.response.data);
        })
        .finally(() => {
          this.attesa = false;
        });
    },
  },
};
</script>

<style></style>
