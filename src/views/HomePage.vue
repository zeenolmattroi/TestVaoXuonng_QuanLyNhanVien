<script setup>
import { inject, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';


const lstDataNews = ref(null);
const lstDataSells = ref(null);


const getListSP = () => {
  axios.get("http://localhost:8080/products")
    .then(response => {
      if (response.data) {
        lstDataNews.value = response.data.result.content
        lstDataSells.value = response.data.result.content
        console.log(lstDataNews.value)
      }
    })
}

onMounted(() => {
  const authToken = localStorage.getItem("authToken");

  if (authToken) {
    try {
      const decodedToken = jwtDecode(authToken);
      const currentTime = Math.floor(Date.now() / 1000);
      if (decodedToken && decodedToken.exp && decodedToken.exp < currentTime) {
        console.log("Token đã hết hạn. Xóa token và thông tin người dùng.");
        localStorage.removeItem("_USER_ID");
        localStorage.removeItem("authToken");
      } else {
        console.log("Token vẫn còn hợp lệ.");
        getListSP();
      }
    } catch (error) {
      console.error("Lỗi khi giải mã token:", error);
      localStorage.removeItem("authToken");
      localStorage.removeItem("_USER_ID");
    }
  } else {
    console.log("Không tìm thấy token.");
    getListSP();
  }
});
</script>

<template>
  <div class="container py-3">
    <RouterLink :to="{ name: 'categories' }">
      <img class="img-responsive rounded" src="@/assets/images/banner.png">
    </RouterLink>

    <h2 class="text-primary text-center py-3">NEW PRODUCTS</h2>

    <div class="row g-3">
      <div class="col-lg-3 col-md-6 col-6 d-flex align-content-stretch" v-for="o in lstDataNews" :key="o.id">
        <RouterLink class="card border-0 shadow-sm w-100" :to="{ name: 'product', params: { id: o.id } }">
          <img :src="o.image" class="card-img-top card-img-product">
          <div class="card-body d-flex flex-column">
            <h6 class="card-title  flex-grow-1">{{ o.name }}</h6>
            <p class="card-text d-flex justify-content-between align-items-center">
              <b class="text-primary">{{ o.price }}$</b>
              <small class="text-secondary">{{ o.sell }} sold</small>
            </p>
          </div>
        </RouterLink>
      </div>
    </div>

    <h2 class="text-primary text-center py-3 text-uppercase">top best selling</h2>

    <div class="row g-3">
      <div class="col-lg-3 col-md-6 col-6 d-flex align-content-stretch" v-for="o in lstDataSells" :key="o.id">
        <RouterLink class="card border-0 shadow-sm w-100" :to="{ name: 'product', params: { id: o.id } }">
          <img :src="o.image" class="card-img-top card-img-product">
          <div class="card-body d-flex flex-column">
            <h6 class="card-title  flex-grow-1">{{ o.name }}</h6>
            <p class="card-text d-flex justify-content-between align-items-center">
              <b class="text-primary">{{ o.price }}$</b>
              <small class="text-secondary">{{ o.sell }} sold</small>
            </p>
          </div>
        </RouterLink>
      </div>
    </div>
    <RouterLink class="navbar-brand" :to="{ name: 'Test' }">
      <h1>Test</h1>
    </RouterLink>



  </div>


</template>
