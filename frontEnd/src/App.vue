<template>
  <v-app>
    <Header />

    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import Header from "./components/layout/Header.vue";
</script>
