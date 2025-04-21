
<script setup>
import { inject, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode'; // Import jwtDecode

const _USER = inject('_USER');
const _COUNT_CART = inject('_COUNT_CART');

const routes = useRoute();
const router = useRouter();

const msgSuccess = ref(null);
const msgError = ref(null);

const product = ref(null);
const category = ref(null);

const model = ref({
  productId: null,
  userId: null, // Sẽ được cập nhật từ token
  quantity: 1,
  price: 0,
});

const setQuantity = (num) => {
  num = parseInt(model.value.quantity) + num;
  model.value.quantity = Math.max(1, num);
};

const handleBuyNow = async () => {
  if (!_USER.value) {
    return router.push({ name: 'login' });
  }

  if (await handleAddToCart()) {
    router.push({ name: 'cart' });
  }
};

const handleAddToCart = async () => {
  if (!_USER.value) {

    return router.push({ name: "login" });

    
  }
  try {
    console.log("gửi yêu cầu POST đến /cart");
    const response = await axios.post("http://localhost:8080/cart", {
      productId: model.value.productId,
      userId: model.value.userId, 
      quantity: model.value.quantity,
    });
    if (response.status === 200) {
      msgSuccess.value = "Add to cart successfully";

      updateCountCart()
      return router.push({ name: 'cart' });
    } else {
      msgError.value = "Add to cart failed";
      return false;
    }
  } catch (error) {
    console.error("Lỗi khi thêm vào giỏ hàng:", error);
    msgError.value = "Add to cart failed";
    return false;
  }
};

const updateCountCart = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/cart/count/${_USER.value?.id}`);
    _COUNT_CART.value = response.data.result;
  } catch (error) {
    console.error('Lỗi khi cập nhật số lượng giỏ hàng:', error);
  }
};

onMounted(async () => {
  try {
    
    const token = localStorage.getItem('authToken');
    if (token) {
      const decoded = jwtDecode(token);
      _USER.value = {
        id: decoded.userId, 
        username: decoded.username, 
        role: decoded.scope,
      };
      model.value.userId = _USER.value.id; 
    }
    console.log("số sản phẩm trong giỏ hàng của userid: "+ model.value.userId + "là: "+_COUNT_CART.value)

    const response = await axios.get(`http://localhost:8080/products/${routes.params.id}`);
    product.value = response.data.result;

    if (!product.value) {
      return router.push({ name: 'categories' });
    }
    const categoryResponse = await axios.get(`http://localhost:8080/categories/${product.value.categoryId}`);
    category.value = categoryResponse.data;
    model.value.productId = product.value.id;
    model.value.price = product.value.price;
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu sản phẩm:', error);
    router.push({ name: 'categories' });
  }
});
</script>
<template>
  <div class="container my-3">
    <nav style="--bs-breadcrumb-divider: url(&#34;data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='8' height='8'%3E%3Cpath d='M2.5 0L1 1.5 3.5 4 1 6.5 2.5 8l4-4-4-4z' fill='%236c757d'/%3E%3C/svg%3E&#34;);" aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item"><RouterLink :to="{name: 'home'}">Home</RouterLink></li>
        <li class="breadcrumb-item"><RouterLink :to="{name: 'categories'}">List products</RouterLink></li>
        <li class="breadcrumb-item active">{{ product?.name }}</li>
      </ol>
    </nav>
    <div class="row g-3">
      <div class="col-md-6">
        <img class="img-responsive rounded-2 shadow-sm" :src="product?.image" />
      </div>
      
      <div class="col-md-6 p-5">
        <div class="alert alert-danger my-3" v-show="msgError">{{ msgError }}</div>
        <div class="alert alert-success my-3" v-show="msgSuccess">{{ msgSuccess }}</div>

        <h2>{{ product?.name }}</h2>
        <div class="fs-1 text-primary fw-bold">${{ product?.price }}</div>
        <p>Category: <b>{{ category?.name }}</b></p>
        <div class="mt-5 d-flex align-items-center">
          <span>Quantity</span>
          <span class="input-group flex-nowrap mx-3">
              <button class="btn btn-outline-secondary" @click="setQuantity(-1)"><i class="fa-solid fa-minus"></i></button>
              <input type="text" class="form-control" style="max-width: 80px; min-width: 80px" v-model.trim="model.quantity" readonly>
              <button class="btn btn-outline-secondary" @click="setQuantity(1)"><i class="fa-solid fa-plus"></i></button>
          </span>
        </div>
        <p class="my-3">{{ product?.quantity }} pieces available</p>
        <div class="mx-3">
          <button class="btn btn-outline-primary rounded-5 w-100 mb-3" @click="handleAddToCart"><i class="fa-solid fa-cart-plus"></i> ADD TO CART</button>
          <button class="btn btn-primary rounded-5 w-100" @click="handleBuyNow">BUY IT NOW</button>
        </div>
      </div>
      <div class="col-md-6">
        <h6>Description:</h6>
        {{ product?.description }}
      </div>
      <div class="col-md-6">
        <h6>Detail:</h6>
        {{ product?.detail }}
      </div>
    </div>
  </div>
</template>
