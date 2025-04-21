<script setup>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { inject, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const _DB = inject('_DB');
const _USER = inject('_USER');
const router = useRouter();

const msgSuccess = ref(null);
const msgError = ref(null);

const model = ref({
  fullname: _USER.value?.fullname,
  phone: _USER.value?.phone,
  currentPassword: null,
  newPassword: null,
  reNewPassword: null,
});


const handleSubmit_form_AI = () => {
  msgSuccess.value = null;
  msgError.value = null;

  if (!model.value.phone) {
    return msgError.value = 'Number phone cannot be left blank';
  }
  const token = localStorage.getItem("authToken");
  const decode = jwtDecode(token);

  console.log(decode);
  axios.post("http://localhost:8080/users/check-password", {
    username: decode.sub,
    password: model.value.currentPassword
  }, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  }).then(
    response => {
      if (response.data.result) {
        console.log("pass cũ đã đúng");

        const updatePayload = {
          username: decode.sub,
          fullname: model.value.fullname,
          phone: model.value.phone,
        };

        
        if (model.value.newPassword) {
          if (model.value.newPassword !== model.value.reNewPassword || model.value.newPassword==null ) {
            return msgError.value = 'Re-entered new password is incorrect';
          }
          updatePayload.password = model.value.newPassword;
        }

        axios.post("http://localhost:8080/users/change-infomation", updatePayload, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }).then(
          response => {
            if (response.data.result) {
              msgSuccess.value = 'Saved successfully';
            } else {
              return msgError.value = 'An error occurred, please try again in a few minutes!';
            }
          },
          error => {
            console.error("Lỗi cập nhật thông tin:", error);
            msgError.value = 'Failed to update information. Please try again.';
          }
        );
      } else {
        console.log(" sai mẹ pass");
        return msgError.value = 'Current password is incorrect';
      }
    },
    error => {
      console.error("Lỗi kiểm tra mật khẩu:", error);
      msgError.value = 'Failed to check current password. Please try again.';
    }
  ).finally(() => {
    model.value.currentPassword = null;
    model.value.newPassword = null;
    model.value.reNewPassword = null;
  });
};

const handleSubmit = () => {
  msgSuccess.value = null;
  msgError.value = null;

  if (!model.value.phone) {
    return msgError.value = 'Number phone cannot be left blank';
  }
  if(!model.value.newPassword){
    return msgError.value = 'New password cannot be left blank';
  }
  if(!model.value.currentPassword){
    return msgError.value = 'currentPassword cannot be left blank';
  }

  const token = localStorage.getItem("authToken")
  const decode = jwtDecode(token)

  console.log(decode)
  axios.post("http://localhost:8080/users/check-password", {
    username: decode.sub,
    password: model.value.currentPassword
  }, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  }).then(
    response => {
      if (response.data.result) {
        console.log("pass cũ đã đúng")
        if (model.value.reNewPassword !== model.value.newPassword) {
          return msgError.value = 'Re-entered new password is incorrect';
        } else {
          axios.post("http://localhost:8080/users/change-infomation", {
            username: decode.sub,
            fullname: model.value.fullname,
            phone: model.value.phone,
            password: model.value.newPassword,
          }, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }).then(
            response => {
              if (response.data.result) {
                msgSuccess.value = 'Saved successfully';
              } else {
                return msgError.value = 'An error occurred, please try again in a few minutes!';
              }
            }
          )
        }
      } else {
        console.log(" sai mẹ pass")
        return msgError.value = 'Current password is incorrect';
      }
    }
  )
  model.value.currentPassword = null;
  model.value.newPassword = null;
  model.value.reNewPassword = null;
};

onMounted(() => {
  if (!_USER.value) {
    return router.push({ name: 'home' });
  }
  
});

</script>

<template>
  <div class="container py-3">
    <nav
      style="--bs-breadcrumb-divider: url(&#34;data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='8' height='8'%3E%3Cpath d='M2.5 0L1 1.5 3.5 4 1 6.5 2.5 8l4-4-4-4z' fill='%236c757d'/%3E%3C/svg%3E&#34;);"
      aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li class="breadcrumb-item">
          <RouterLink :to="{ name: 'home' }">Home</RouterLink>
        </li>
        <li class="breadcrumb-item active" aria-current="page">Account</li>
      </ol>
    </nav>
    <div class="row">
      <div class="col-lg-2 col-md-3">
        <div class="position-relative">
          <h2>{{ _USER?.username }}</h2>
          <ul class="nav flex-column">
            <li class="nav-item">
              <RouterLink class="nav-link px-0 active" aria-current="page" :to="{ name: 'account-information' }">Login
                information</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink class="nav-link px-0" :to="{ name: 'account-purchase' }">Purchase History</RouterLink>
            </li>
          </ul>
        </div>
      </div>
      <div class="col-lg-10 col-md-9">
        <div class="card border-0 rounded-3">
          <div class="card-body p-4">
            <h5 class="card-title text-primary">Login information</h5>
            <div class="card-text">
              <div class="alert alert-danger my-3" v-show="msgError">{{ msgError }}</div>
              <div class="alert alert-success my-3" v-show="msgSuccess">{{ msgSuccess }}</div>
              <div class="mt-4">
                <input class="form-control rounded-5" type="text" placeholder="Fullname" v-model.trim="model.fullname"
                  @keydown.enter="handleSubmit">
              </div>
              <div class="mt-4">
                <input class="form-control rounded-5" type="text" placeholder="Number phone" v-model.trim="model.phone"
                  @keydown.enter="handleSubmit">
              </div>
              <h5 class="card-title text-primary mt-3">Change password</h5>
              <div class="mt-4">
                <input class="form-control rounded-5" type="password" placeholder="Current password"
                  v-model.trim="model.currentPassword" @keydown.enter="handleSubmit">
              </div>
              <div class="mt-4">
                <input class="form-control rounded-5" type="password" placeholder="New password"
                  v-model.trim="model.newPassword" @keydown.enter="handleSubmit">
              </div>
              <div class="mt-4">
                <input class="form-control rounded-5" type="password" placeholder="Re-entered new password"
                  v-model.trim="model.reNewPassword" @keydown.enter="handleSubmit">
              </div>
            </div>
            <div class="mt-4 d-flex justify-content-end">
              <button class="btn btn-primary rounded-5" @click="handleSubmit_form_AI">SAVE</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
