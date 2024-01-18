<template>
  <div>
    <h2>Login</h2>
    <form @submit.prevent="login">
      <label for="username">Username:</label>
      <input type="text" v-model="username" required />
      <br />
      <label for="password">Password:</label>
      <input type="password" v-model="password" required />
      <br />
      <button type="submit">Login</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    login() {
      // 로그인 로직을 여기에 구현
      console.log("Username:", this.username);
      console.log("Password:", this.password);
      // 실제 로그인 기능을 추가하고 로그인이 성공하면 다음 페이지로 이동하도록 구현할 수 있습니다.
    },
  },
};
</script>
