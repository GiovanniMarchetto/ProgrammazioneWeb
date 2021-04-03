<template>
  <article>
    <Navbar
      :potere="ruolo"
      :nomePrimaLista="'Lista uploader'"
      :nomeSecondaLista="'File di un uploader'"
      @mostraSezione="showSezione"
      >Consumer page</Navbar
    >

    <section v-show="sezione === ''">
      <header>
        <h3>Lista Uploader con documenti</h3>
      </header>
      <Table
        :items="uploaders"
        :fields="fieldsListUploaders"
        :caricamentoDati="caricamentoDati"
        @mostraFiles="showFiles"
      />
    </section>

    <section v-show="sezione === 'secondaLista'">
      <header>
        <h3>Lista Files caricati da {{ uploaderScelto.name }}</h3>
      </header>

      <b-img
        id="logoPagina"
        rounded="circle"
        :src="`${uploaderScelto.logo}`"
        alt="Logo uploader"
      ></b-img>

      <b-form inline @submit.prevent="hashtagFilter">
        <b-form-input
          id="hashtag"
          name="hashtag"
          type="text"
          v-model="hashtag"
          placeholder="hashtag"
        />
        <b-button type="submit" variant="primary" size="sm"
          >Filtra hashtag
        </b-button>
        <b-button
          variant="danger"
          size="sm"
          @click="(hashtag = ''), hashtagFilter()"
        >
          Reset</b-button
        >
      </b-form>

      <Table
        :items="filesUploader"
        :fields="fieldsListFilesUploader"
        :caricamentoDati="caricamentoDati"
        @download-file="download"
      />
    </section>

    <ModInfo
      v-show="sezione === 'modInfo'"
      :potere="ruolo"
      :role="'consumer'"
      @modInfo="showMsg"
    />

    <Messages
      :msg_success="msg_success"
      :msg_error="msg_error"
      :msg_warning="msg_warning"
    />
  </article>
</template>

<script>
import Navbar from "@/components/layout/Navbar";
import Table from "@/components/layout/Table";
import ModInfo from "../components/functions/ModInfo";
import Messages from "../components/layout/Messages";

import { messagesMixin, sectionsMixin } from "../utils/utils";

import axios from "axios";
axios.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem(
  "jwtToken"
)}`;

export default {
  name: "Consumer_page",
  components: { Navbar, Table, ModInfo, Messages },
  mixins: [messagesMixin, sectionsMixin],
  data() {
    return {
      ruolo: "consumer",
      uploaderScelto: "",
      hashtag: "",
      uploaders: [],
      filesConsumer: [],
      filesUploader: [],
      fieldsListUploaders: ["logo", "name", "details", "files"],
      fieldsListFilesUploader: [
        "name",
        { key: "dataCaricamento", sortable: true },
        { key: "dataVisualizzazione", sortable: true },
        "hashtag",
        "scarica",
      ],
      caricamentoDati: false,
    };
  },
  watch: {
    uploaderScelto: function() {
      if (this.uploaderScelto != "")
        this.showFiles(this.uploaderScelto.username);
    },
  },
  methods: {
    showFiles(usernameUploader) {
      this.sezione = "secondaLista";
      this.filesUploader = this.filesConsumer.filter(
        (file) => file.usernameUpl === usernameUploader
      );
      this.uploaders.every((element) => {
        if (element.username === usernameUploader) {
          this.uploaderScelto = element;
          return false;
        }
        return true;
      });
    },

    ordinamentoFile() {
      this.filesConsumer.sort(function(a, b) {
        if ((a.dataVisualizzazione === "") ^ (b.dataVisualizzazione === "")) {
          if (a.dataVisualizzazione < b.dataVisualizzazione) return -1;
          if (a.dataVisualizzazione > b.dataVisualizzazione) return 1;
        }
        if (a.dataCaricamento < b.dataCaricamento) return 1;
        if (a.dataCaricamento > b.dataCaricamento) return -1;
        return 0;
      });
      this.filesUploader = this.filesConsumer.filter(
        (file) => file.usernameUpl === this.uploaderScelto.username
      );
    },

    download(fileId) {
      this.caricamentoDati = true;
      axios
        .get(`${process.env.VUE_APP_APIROOT}/files/download/${fileId}`, {
          responseType: "arraybuffer",
        })
        .then((res) => {
          let indexCurrent = this.filesConsumer.findIndex(
            (file) => file.id === fileId
          );

          // https://stackoverflow.com/questions/60915291/download-a-png-file-served-as-binary-octet-stream
          const link = document.createElement("a");
          link.style.display = "none";
          link.download = this.filesConsumer[indexCurrent].name;
          var blob = new Blob([res.data], { type: "application/octet-stream" });
          link.href = window.URL.createObjectURL(blob);
          document.body.appendChild(link);
          link.click();
          setTimeout(() => {
            window.URL.revokeObjectURL(link.href);
            link.remove();
          }, 5000);

          if (this.filesConsumer[indexCurrent].dataVisualizzazione === "") {
            let dataCorrente = new Date();
            let dataScritta =
              dataCorrente.toISOString().substring(0, 10) +
              " " +
              dataCorrente.getUTCHours() +
              dataCorrente.toLocaleTimeString().substring(2);
            this.filesConsumer[indexCurrent].dataVisualizzazione = dataScritta;
            this.ordinamentoFile();
          }
          this.showMsg("Download effettuato");
        })
        .catch((err) => {
          this.showMsg(err.response.data);
        })
        .finally(() => {
          this.caricamentoDati = false;
        });
    },

    hashtagFilter() {
      this.filesUploader = null;
      this.filesUploader = this.filesConsumer.filter(
        (file) =>
          file.hashtag.includes(this.hashtag) &&
          file.usernameUpl === this.uploaderScelto.username
      );
    },
  },
  created() {
    this.caricamentoDati = true;
    axios
      .get(`${process.env.VUE_APP_APIROOT}/list/uploaders`)
      .then((res) => {
        this.uploaders = res.data;
        if (this.uploaders.length === 1)
          this.uploaderScelto = this.uploaders[0];
      })
      .catch((err) => this.showMsg(err.toString()));

    axios
      .get(`${process.env.VUE_APP_APIROOT}/list/filesConsumer`)
      .then((res) => {
        this.filesConsumer = res.data;
        if (this.uploaderScelto != "")
          this.showFiles(this.uploaderScelto.username);
      })
      .catch((err) => this.showMsg(err.toString()))
      .finally(() => {
        this.caricamentoDati = false;
      });
  },
};
</script>
