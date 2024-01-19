<template>
  <v-card>
    <v-tabs bg-color="deep-purple-accent-4" centered stacked>
      <v-tab @click="clickTab('/main')">
        <v-icon>mdi-home</v-icon>
        Home
      </v-tab>
      <v-tab @click="clickTab('/taobao')">
        <v-icon>mdi-monitor-eye</v-icon>
        타오바오 검색
      </v-tab>
      <v-tab @click="clickTab('/asset')">
        <v-icon>mdi-piggy-bank-outline</v-icon>
        자산 관리
      </v-tab>
    </v-tabs>
  </v-card>
</template>
<script setup>
import { useRoute, useRouter } from "vue-router";

const router = useRouter();
function clickTab(tab) {
  router.push(tab);
}
</script>
