<template>
  <article>
    <Navbar
      :potere="ruolo"
      :nomePrimaLista="'Lista consumer'"
      :nomeSecondaLista="'File di un consumer'"
      @mostraSezione="showSezione"
      @ruoloForm="modificaRuoloForm"
      >Uploader page</Navbar
    >

    <section v-show="sezione === ''">
      <header><h3>Lista Consumer</h3></header>
      <Table
        :items="consumers"
        :fields="fieldsListConsumers"
        :caricamentoDati="caricamentoDati"
        @mostraFiles="showFiles"
      />
    </section>

    <section v-show="sezione === 'secondaLista'">
      <header>
        <h3>Lista Files di {{ consumerScelto }}</h3>
      </header>
      <Table
        :items="filesConsumer"
        :fields="fieldsListFileConsumer"
        :caricamentoDati="caricamentoDati"
      />
    </section>

    <Registration
      v-show="sezione === 'registration'"
      :potere="ruolo"
      :role="'consumer'"
      @registrazione_utente="registrazione_utente_home"
      @registrazione="showMsg"
    />

    <ModInfo
      v-show="sezione === 'modInfo'"
      :potere="ruolo"
      :role="ruoloForm"
      @modInfo_utente="modInfo_utente_home"
      @modInfo="showMsg"
    />

    <Upload
      v-show="sezione === 'upload'"
      @upload_consumer="upload_consumer_home"
      @upload_file="upload_file_home"
      @upload="showMsg"
    />

    <Delete
      v-show="sezione === 'delete'"
      :potere="ruolo"
      :attoriOptions="attoriOptions"
      :fileOptions="fileOptions"
      @delete_file="delete_file_home"
      @delete_username="delete_username_home"
      @delete="showMsg"
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
import Upload from "../components/functions/Upload";
import Registration from "../components/functions/Registration";
import ModInfo from "../components/functions/ModInfo";
import Delete from "../components/functions/Delete";
import Messages from "../components/layout/Messages";

import { messagesMixin, sectionsMixin } from "../utils/utils";

import axios from "axios";
axios.defaults.headers["Authorization"] = `Bearer ${localStorage.getItem(
  "jwtToken"
)}`;

export default {
  name: "Uploader_page",
  components: {
    Navbar,
    Table,
    Upload,
    Registration,
    ModInfo,
    Delete,
    Messages,
  },
  mixins: [messagesMixin, sectionsMixin],
  data() {
    return {
      ruolo: "uploader",
      ruoloForm: "",
      consumerScelto: null,
      consumers: [],
      filesUploader: [],
      filesConsumer: [],
      fieldsListConsumers: ["name", "files", "details"],
      fieldsListFileConsumer: [
        "id",
        "name",
        { key: "dataCaricamento", sortable: true },
        { key: "dataVisualizzazione", sortable: true },
        "indirizzoIP",
        "hashtag",
      ],
      caricamentoDati: false,
    };
  },
  computed: {
    attoriOptions: function() {
      let listAttori = [];
      this.consumers.forEach((cons) => {
        listAttori.push(cons.username);
      });
      return listAttori;
    },
    fileOptions: function() {
      let listFiles = [];
      this.filesUploader.forEach((file) => {
        listFiles.push(file.id);
      });
      return listFiles;
    },
  },
  methods: {
    modificaRuoloForm(ruoloInput) {
      this.ruoloForm = ruoloInput;
    },
    showFiles(consUsername) {
      this.consumerScelto = consUsername;
      this.filesConsumer = null;
      this.filesConsumer = this.filesUploader.filter(
        (file) => file.usernameCons === consUsername
      );
      this.sezione = "secondaLista";
    },

    registrazione_utente_home(nuovoConsumer) {
      const { username, name, email, logo } = nuovoConsumer;
      const infoConsumer = {
        username: username,
        name: name,
        email: email,
        logo: logo,
      };
      this.consumers.push(infoConsumer);
    },

    modInfo_utente_home(modConsumer) {
      const { utente, role } = modConsumer;
      if (role === "consumer") {
        this.consumers = this.consumers.filter(
          (cons) => cons.username !== utente.username
        );
        this.consumers.push(utente);
      }
    },

    upload_consumer_home(nuovoConsumer) {
      const { username } = nuovoConsumer;
      if (
        this.consumers.findIndex((el) => el.username === username) === -1
      ) {
        this.consumers.push(nuovoConsumer);
      }
    },
    upload_file_home(fileCaricato) {
      this.filesUploader.push(fileCaricato);
      if (fileCaricato.usernameCons === this.consumerScelto)
        this.filesConsumer.push(fileCaricato);
      this.ordinamentoFile();
    },

    delete_file_home(fileIdDel) {
      this.filesUploader = this.filesUploader.filter(
        (file) => file.id !== fileIdDel
      );
      this.filesConsumer = this.filesConsumer.filter(
        (file) => file.id !== fileIdDel
      );
    },
    delete_username_home(usernameDel) {
      this.consumers = this.consumers.filter(
        (cons) => cons.username !== usernameDel
      );
      this.filesUploader = this.filesUploader.filter(
        (file) => file.usernameCons !== usernameDel
      );
      if (usernameDel === this.consumerScelto) {
        this.consumerScelto = "";
        this.filesConsumer = [];
      }
    },

    ordinamentoFile() {
      this.filesUploader.sort(function(a, b) {
        if ((a.dataVisualizzazione === "") ^ (b.dataVisualizzazione === "")) {
          if (a.dataVisualizzazione < b.dataVisualizzazione) return -1;
          if (a.dataVisualizzazione > b.dataVisualizzazione) return 1;
        }
        if (a.dataCaricamento < b.dataCaricamento) return 1;
        if (a.dataCaricamento > b.dataCaricamento) return -1;
        return 0;
      });
      this.filesConsumer = this.filesUploader.filter(
        (file) => file.usernameCons === this.consumerScelto
      );
    },
  },
  created() {
    this.caricamentoDati = true;
    axios
      .get(`${process.env.VUE_APP_APIROOT}/list/consumers`)
      .then((res) => (this.consumers = res.data))
      .catch((err) => this.showMsg(err.toString()));

    axios
      .get(`${process.env.VUE_APP_APIROOT}/list/filesUploader`)
      .then((res) => (this.filesUploader = res.data))
      .catch((err) => this.showMsg(err.toString()))
      .finally(() => {
        this.caricamentoDati = false;
      });
  },
};
</script>
