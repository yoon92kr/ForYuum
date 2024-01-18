export default {
  state: {
    dataFromSpringBoot: null,
  },
  mutations: {
    setDataFromSpringBoot(state, data) {
      state.dataFromSpringBoot = data;
    },
  },
  actions: {
    fetchDataFromSpringBoot({ commit }) {
      // Spring Boot에서 데이터를 가져와서 Vuex 스토어에 삽입
      // 예시로 axios를 사용한 비동기 요청
      axios
        .get("/api/data-from-spring-boot")
        .then((response) => {
          commit("setDataFromSpringBoot", response.data);
        })
        .catch((error) => {
          console.error("Error fetching data from Spring Boot:", error);
        });
    },
  },
};
