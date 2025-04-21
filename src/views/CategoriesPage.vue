
<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios'; 

const routes = useRoute();

const pageSize = 3;
const currentPage = ref(1);
const maxPage = ref(1);

const currentCategory = ref(null);

const filterPrice = ref({
  min: null,
  max: null,
});

const lstData = ref(null);
const lstCategories = ref([]); // Khởi tạo mảng rỗng để lưu trữ danh mục từ API

// Thay thế loadPage() bằng loadDataFromApi()
const loadDataFromApi = async (page) => {
  try {
    const response = await axios.get('http://localhost:8080/products', {
      params: {
        page: page,
        pageSize: pageSize.value,
        categoryId: currentCategory.value?.id,
        minPrice: filterPrice.value.min,
        maxPrice: filterPrice.value.max,
      },
    });

    console.log("response.data: ", response.data); // Thêm dòng này
    lstData.value = response.data.result.content;
    console.log("data")
    console.log( lstData.value);
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu từ API:', error);
  }
};



const allProduct = async () => {
  try {
    const response = await axios.get('http://localhost:8080/products');
    lstData.value = response.data.result.content;
    console.log(lstData.value);
  } catch (error) {
    console.error('Lỗi khi tải tất cả sản phẩm:', error);
  }
};

// Thay thế loadCategories() bằng loadCategoriesFromApi()
const loadCategoriesFromApi = async () => {
  try {
    const response = await axios.get('http://localhost:8080/categories'); // Thay đổi URL API
    lstCategories.value = response.data.result; // Điều chỉnh theo cấu trúc dữ liệu API
  } catch (error) {
    console.error('Lỗi khi tải danh mục từ API:', error);
  }
};

// Thay thế setCategory() bằng fetchCategoryAndLoadData()
const fetchCategoryAndLoadData = async (id) => {
  try {
    const response = await axios.get(`http://localhost:8080/categories/${id}`); // Thay đổi URL API
    currentCategory.value = response.data.result; 
    console.log("category"+currentCategory.value.name+currentCategory.value.id) // Điều chỉnh theo cấu trúc dữ liệu API
    await loadDataFromApi(currentPage.value-1); 
  } catch (error) {
    console.error('Lỗi khi tải danh mục từ API:', error);
  }
};


const handleFilter = () => {
  loadDataFromApi(currentPage.value-1);
};

onMounted(async () => {
  await loadCategoriesFromApi(); // Tải danh mục khi component được mount
  await allProduct() // Tải danh mục và dữ liệu khi component được mount
});
</script>


<template>
  <div class="container my-3">
    <nav style="--bs-breadcrumb-divider: url(&#34;data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='8' height='8'%3E%3Cpath d='M2.5 0L1 1.5 3.5 4 1 6.5 2.5 8l4-4-4-4z' fill='%236c757d'/%3E%3C/svg%3E&#34;);" aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><RouterLink :to="{name: 'home'}">Home</RouterLink></li>
        <li class="breadcrumb-item active" aria-current="page">List products</li>
      </ol>
    </nav>
    <div class="row g-3">
      <div class="col-lg-3 col-md-12">
        <div class="position-relative">
          <h4>Filter price</h4>
          <div class="row g-3">
            <div class="col-6 col-lg-12">
              <input type="number" class="form-control" v-model.number.trim="filterPrice.min" placeholder="Min price" />
            </div>
            <div class="col-6 col-lg-12">
              <input type="number" class="form-control" v-model.number.trim="filterPrice.max" placeholder="Max price" />
            </div>
            <div class="mb-3 col-12">
              <button class="btn btn-primary" @click="handleFilter">Filter</button>
            </div>
          </div>
          <h4>Filter category</h4>
          <ul class="nav flex-column">
            <li class="nav-item">
              <RouterLink class="nav-link px-0" :class="{active: !routes.params?.id}" :to="{name: 'categories'}" @click=allProduct()>All categories</RouterLink>
            </li>
            <li class="nav-item" v-for="o in lstCategories" :key="o.id">
              <RouterLink class="nav-link px-0" :class="{active: o.id == currentCategory?.id}" :to="{name: 'category', params: {id: o.id}}" @click="fetchCategoryAndLoadData(o.id)">{{ o.name }}</RouterLink>
            </li>
          </ul>
        </div>
      </div>
      <div class="col-lg-9 col-md-12">
        <div class="row g-3">
          <div class="col-6 col-lg-3 col-md-4 d-flex align-items-stretch" v-for="o in lstData" :key="o.id">
            <RouterLink class="card border-0 shadow-sm w-100" :to="{ name: 'product', params: {id: o.id}}">
              <img :src="o.image" class="card-img-top card-img-product" alt="...">
              <div class="card-body d-flex flex-column">
                <h6 class="card-title  flex-grow-1">{{ o.name }}</h6>
                <p class="card-text d-flex justify-content-between align-items-center">
                  <b class="text-primary">{{ o.price }}$</b>
                  <small class="text-secondary">{{ o.sell }} sold</small>
                </p>
              </div>
            </RouterLink>
          </div>
          <div class="col-12">
            <div class="d-flex align-items-center justify-content-center py-4">
              <button class="btn btn-primary rounded-5" @click="loadDataFromApi(currentPage - 1)"><i class="fa-solid fa-chevron-left"></i></button>
              <span class="px-4">Page {{ currentPage }} / {{ maxPage }}</span>
              <button class="btn btn-primary rounded-5" @click="loadDataFromApi(currentPage + 1)"><i class="fa-solid fa-chevron-right"></i></button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style>
.card-img-product {
  min-height: 200px;
  max-height: 200px;
}
</style>
