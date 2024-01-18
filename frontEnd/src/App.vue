<script setup>
import { RouterLink, RouterView } from "vue-router";
import Header from "./components/layout/Header.vue";
</script>

<template>
  <div id="app">
    <Header />
    <div id="content">
      <RouterView />
    </div>
  </div>
</template>
