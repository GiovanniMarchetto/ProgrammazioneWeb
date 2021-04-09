<template>
  <article>
    <header><h2>Pagina d'accesso</h2></header>

    <section v-show="sezione === ''">
      <Login @login="showMsg" @login_username="login_username_home" />
      <b-button @click="sezione = 'registration'"
        >Non hai ancora un account</b-button
      >
    </section>

    <section v-show="sezione === 'registration'">
      <Registration
        :potere="'consumer'"
        :role="'consumer'"
        @registrazione="registrazione_home"
      />
      <b-button @click="sezione = ''">Ho già un account!</b-button>
    </section>

    <Messages
      :msg_success="msg_success"
      :msg_error="msg_error"
      :msg_warning="msg_warning"
    />
  </article>
</template>

<script>
import Login from "../components/functions/Login";
import Registration from "../components/functions/Registration";
import Messages from "../components/layout/Messages";

import { messagesMixin, sectionsMixin } from "../utils/utils";

export default {
  name: "Login_page",
  components: { Login, Registration, Messages },
  mixins: [messagesMixin, sectionsMixin],
  methods: {
    login_username_home(username) {
      setTimeout(() => {
        if (username.length === 4) {
          this.$router.push("/uploader_page");
        } else if (username.includes("@")) {
          this.$router.push("/administrator_page");
        } else if (username.length === 16) {
          this.$router.push("/consumer_page");
        } else {
          this.showMsg("Error: Username non riscontrato...");
        }
      }, 1000);
    },
    registrazione_home(frase) {
      this.showMsg(frase);
      this.sezione = "";
    },
  },
  created() {
    console.log(process.env.VUE_APP_APIROOT);
  },
};
</script>
