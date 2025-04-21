
<script setup>
import { inject, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const _USER = inject('_USER');
const _COUNT_CART = inject('_COUNT_CART');

const msgSuccess = ref(null);
const msgError = ref(null);

const lstData = ref(null);
const itemsTotal = ref(0);
const priceTotal = ref(0);

const getFormattedDate = () => {
  return new Date();
};

const model = ref({
  taiKhoan: _USER.value ? { id: _USER.value.id } : null, 
  hoaDonChiTiets: [], 
  status: 0,
  price: 0,
  fullname: _USER.value?.fullname,
  phone: _USER.value?.phone,
  address: null,
  date: getFormattedDate(),
});

const loadData = async () => {
  try {
    if (_USER.value && _USER.value.id) {
      const response = await axios.get(`http://localhost:8080/cart/user/${_USER.value.id}`);
      lstData.value = response.data.result;
      _COUNT_CART.value = lstData.value.length;
      itemsTotal.value = lstData.value.reduce((a, b) => a += b.quantity, 0);
      priceTotal.value = lstData.value.reduce((a, b) => a += b.price, 0);
      model.value.price = priceTotal.value;
      model.value.hoaDonChiTiets = lstData.value.map(o => ({
        sanPham: o.productId, // Đối tượng SanPham
        quantity: o.quantity,
        price: o.price,
      }));
    } else {
      console.log('Không có userId, không thể tải dữ liệu giỏ hàng.');
      msgError.value = 'Không có userId, không thể tải dữ liệu giỏ hàng.';
    }
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu giỏ hàng:', error);
    msgError.value = 'Lỗi khi tải dữ liệu giỏ hàng.';
  }
};

const handleSubmit = async () => {
  msgError.value = null;
  msgSuccess.value = null;

  if (!model.value.fullname) {
    return (msgError.value = 'Tên người nhận không được để trống');
  }

  if (!/^(03|05|07|08|09)\d{8}$/.test(model.value.phone)) {
    return (msgError.value = 'Số điện thoại không hợp lệ');
  }

  if (!model.value.address) {
    return (msgError.value = 'Địa chỉ người nhận không được để trống');
  }

  try {

    const token = localStorage.getItem('authToken');

    if (!token) {
      return (msgError.value = 'Bạn chưa đăng nhập');
    }
    const response = await axios.post(
      'http://localhost:8080/orders',
      model.value,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    _COUNT_CART.value = 0;
    await axios.get(`http://localhost:8080/cart/delete-user/${model.value.taiKhoan.id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    console.log("mua hàng ccc")
    msgSuccess.value = 'Mua hàng thành công';
    router.push({ name: 'account-purchase' });
  } catch (error) {
    console.error('Lỗi khi tạo đơn hàng:', error);
    msgError.value = 'Mua hàng thất bại';
  }
};

onMounted(() => {
  if (!_USER.value) {
    return router.push({ name: 'login' });
  }
  loadData();
});
</script>

<template>
  <div class="container my-3">
    <nav
      style="--bs-breadcrumb-divider: url(&#34;data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='8' height='8'%3E%3Cpath d='M2.5 0L1 1.5 3.5 4 1 6.5 2.5 8l4-4-4-4z' fill='%236c757d'/%3E%3C/svg%3E&#34;);"
      aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <RouterLink :to="{ name: 'home' }">Home</RouterLink>
        </li>
        <li class="breadcrumb-item">
          <RouterLink :to="{ name: 'cart' }">Cart</RouterLink>
        </li>
        <li class="breadcrumb-item active">Checkout</li>
      </ol>
    </nav>
    <div class="alert alert-danger my-3" v-show="msgError">{{ msgError }}</div>
    <div class="alert alert-success my-3" v-show="msgSuccess">{{ msgSuccess }}</div>
    <div class="row g-3">
      <div class="col-md-8">
        <div class="card border-0 rounded-3">
          <div class="card-body p-4">
            <div class="card-text">
              <div class="row g-4">
                <div class="col-md-6">
                  <h4>Receiving information</h4>
                  <div class="col-12">
                    <label for="fullname" class="form-label">Fullname</label>
                    <input type="text" class="form-control" id="fullname" v-model.trim="model.fullname">
                  </div>
                  <div class="col-12">
                    <label for="phone" class="form-label">Number phone</label>
                    <input type="text" class="form-control" id="phone" v-model.trim="model.phone">
                  </div>
                  <div class="col-12">
                    <label for="address" class="form-label">Address</label>
                    <input type="text" class="form-control" id="address" v-model.trim="model.address">
                  </div>
                </div>
                <div class="col-md-6">
                  <h4>Payment method</h4>
                  <div class="card">
                    <div class="card-body">
                      <span class="card-title">
                        <div class="form-check">
                          <input class="form-check-input" type="radio" id="flexRadioDefault1" checked>
                          <label class="form-check-label" for="flexRadioDefault1">
                            Payment upon delivery
                          </label>
                        </div>
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card border-0 rounded-3">
          <div class="card-body p-4">
            <div class="card-text">
              <div class="table-responsive">
                <table class="table">
                  <thead>
                    <tr>
                      <th></th>
                      <th>Product</th>
                      <th>Quantity</th>
                      <th>Price</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-if="lstData?.length < 1">
                      <td colspan="5" class="text-center">List empty.</td>
                    </tr>
                    <template v-else>
                      <tr v-for="o in lstData" :key="o.id">
                        <td class="align-middle">
                          <img class="img-responsive rounded-2 border-1" width="120px" height="120px"
                            :src="o?.productId?.image" />
                        </td>
                        <td class="align-middle">{{ o?.productId?.name }}</td>
                        <td class="align-middle">{{ o?.quantity }}</td>
                        <td class="align-middle">{{ o?.price * o?.quantity }}$</td>
                      </tr>
                    </template>
                  </tbody>
                </table>
              </div>
              <div class="d-flex justify-content-between">
                <span>Item(s) total</span>
                <span>${{ priceTotal }}</span>
              </div>
              <div class="d-flex justify-content-between border-bottom py-3">
                <span>Shipping</span>
                <span>Free</span>
              </div>
              <div class="d-flex justify-content-between py-3 fw-bold">
                <span>Total ({{ itemsTotal }} items)</span>
                <span>${{ priceTotal }}</span>
              </div>
            </div>
            <div class="mt-4 d-flex">
              <button class="btn btn-primary rounded-5 w-100" v-if="lstData?.length > 0" @click="handleSubmit">ORDER
                NOW</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
