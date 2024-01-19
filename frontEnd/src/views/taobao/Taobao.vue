<template>
  <v-container>
    <v-row>
      <v-col cols="12" sm="6">
        <v-text-field
          v-model="userId"
          class="mx-2"
          label="타오바오 ID"
          rows="1"
          prepend-icon="mdi-pig"
        ></v-text-field>
      </v-col>
      <v-col cols="12" sm="6">
        <v-text-field
          v-model="userPw"
          prepend-icon="mdi-key-variant"
          class="mx-2"
          label="타오바오 PW"
          rows="1"
          type="password"
        >
        </v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" sm="10">
        <v-file-input
          accept="image/png, image/jpeg, image/bmp"
          prepend-icon="mdi-camera"
          label="이미지"
          v-model="uploadImg"
        ></v-file-input>
      </v-col>
      <v-col cols="12" sm="2">
        <v-btn
          @click="searchImg"
          height="50"
          min-width="130"
          color="deep-purple-darken-2"
        >
          이미지 검색
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
export default {
  data() {
    return {
      userId: null,
      userPw: null,
      uploadImg: null,
    };
  },

  methods: {
    async searchImg() {
      if (this.userId == null || this.userPw == null) {
        alert("아이디랑 비밀번호 입력해야죠!");
      } else {
        try {
          const response = await fetch(
            "http://localhost:8080/serachTaobaoImage",
            {
              method: "POST",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify({
                userId: this.userId,
                userPw: this.userPw,
                uploadImg: this.uploadImg,
              }),
            }
          );

          if (response.ok) {
            const responseData = await response.json();
            console.log("Server Response:", responseData);
          } else {
            console.error("Error:", response.statusText);
          }
        } catch (error) {
          console.error("Fetch Error:", error);
        }
      }
    },
  },
};
</script>
