<script setup>
import { inject, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const _USER = inject('_USER');
const _COUNT_CART = inject('_COUNT_CART');

onMounted(() => {
  _USER.value = null;
  _COUNT_CART.value = 0;
  localStorage.removeItem("_USER_ID");
  localStorage.removeItem("authToken");
  router.push({name: 'home'});
});
</script>
<template>
  <div></div>
</template>
