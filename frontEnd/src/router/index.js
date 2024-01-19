import { createRouter, createWebHistory } from "vue-router";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      component: () => import("../views/Main.vue"),
    },
    {
      path: "/main",
      name: "main",
      component: () => import("../views/Main.vue"),
    },
    {
      path: "/taobao",
      name: "taobao",
      component: () => import("../views/taobao/Taobao.vue"),
    },
    {
      path: "/asset",
      name: "asset",
      component: () => import("../views/asset/Asset.vue"),
    },
  ],
});

export default router;
