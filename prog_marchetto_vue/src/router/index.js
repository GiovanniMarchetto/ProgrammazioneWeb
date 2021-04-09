import Vue from "vue";
import VueRouter from "vue-router";
import Login_page from "../views/Login_page.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Login_page",
    component: Login_page,
  },
  {
    path: "/consumer_page",
    name: "Consumer_page",
    meta: { requiresAuth: true },
    component: () =>
      import(
        /* webpackChunkName: "Consumer_page" */ "../views/Consumer_page.vue"
      ),
  },
  {
    path: "/uploader_page",
    name: "Uploader_page",
    meta: { requiresAuth: true },
    component: () =>
      import(
        /* webpackChunkName: "Uploader_page" */ "../views/Uploader_page.vue"
      ),
  },
  {
    path: "/administrator_page",
    name: "Administrator_page",
    meta: { requiresAuth: true },
    component: () =>
      import(
        /* webpackChunkName: "Administrator_page" */ "../views/Administrator_page.vue"
      ),
  },
  {
    path: "*",
    redirect: "/",
  },
];

const router = new VueRouter({
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!localStorage.getItem("jwtToken")) {
      next({
        path: "/",
        query: { redirect: to.fullPath },
      });
    } else if (from.matched.some((record) => record.meta.requiresAuth)) {
      if (localStorage.getItem("nomeUtente").length === 4) {
        this.$router.push("/uploader_page");
      } else if (localStorage.getItem("nomeUtente").includes("@")) {
        this.$router.push("/administrator_page");
      } else if (localStorage.getItem("nomeUtente").length === 16) {
        this.$router.push("/consumer_page");
      } else {
        this.showMsg("ERR- Username non riscontrato...");
      }
    } else {
      next();
    }
  } else {
    next(); // make sure to always call next()!
  }
});

export default router;
