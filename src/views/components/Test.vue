<template>
    <div>
      <button @click="openModal">Mở Modal</button>
  
      <div v-if="isModalOpen" class="modal fade show" style="display: block;">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Chọn Thông Tin</h5>
              <button type="button" class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <div class="mb-3">
                <label for="facilitySelect" class="form-label">Cơ sở:</label>
                <select class="form-select" id="facilitySelect" v-model="selectedFacility" @change="loadDepartments">
                  <option value="" disabled>Chọn cơ sở</option>
                  <option :value="facility.id" :key="facility.id" v-for="facility in facilities">{{ facility.name }}</option>
                </select>
              </div>
  
              <div class="mb-3">
                <label for="departmentSelect" class="form-label">Phòng ban:</label>
                <select class="form-select" id="departmentSelect" v-model="selectedDepartment" :disabled="!selectedFacility" @change="loadMajors">
                  <option value="" disabled>Chọn phòng ban</option>
                  <option :value="department.id" :key="department.id" v-for="department in departments">{{ department.name }}</option>
                </select>
              </div>
  
              <div class="mb-3">
                <label for="majorSelect" class="form-label">Chuyên ngành:</label>
                <select class="form-select" id="majorSelect" v-model="selectedMajor" :disabled="!selectedDepartment">
                  <option value="" disabled>Chọn chuyên ngành</option>
                  <option :value="major.id" :key="major.id" v-for="major in majors">{{ major.name }}</option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="closeModal">Đóng</button>
              <button type="button" class="btn btn-primary" @click="submitSelection" :disabled="!selectedFacility || !selectedDepartment || !selectedMajor">Xác nhận</button>
            </div>
          </div>
        </div>
      </div>
  
      <div v-if="isModalOpen" class="modal-backdrop fade show"></div>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, computed, watch } from 'vue';
  
  // Dữ liệu mẫu (thay thế bằng dữ liệu thực tế của bạn)
  const facilities = ref([
    { id: 1, name: 'Hà Nội' },
    { id: 2, name: 'TP Hồ Chí Minh' },
    { id: 3, name: 'Đà Nẵng' }
  ]);
  
  const departmentsData = ref([
    { id: 101, facilityId: 1, name: 'Phòng Đào Tạo HN' },
    { id: 102, facilityId: 1, name: 'Phòng Khảo Thí HN' },
    { id: 201, facilityId: 2, name: 'Phòng Đào Tạo HCM' },
    { id: 301, facilityId: 3, name: 'Phòng Tuyển Sinh DN' },
    { id: 302, facilityId: 3, name: 'Phòng Hành Chính DN' }
  ]);
  
  const majorsData = ref([
    { id: 1001, departmentId: 101, name: 'CNTT Web HN' },
    { id: 1002, departmentId: 101, name: 'CNTT Mobile HN' },
    { id: 2001, departmentId: 201, name: 'QTKD Tổng Hợp HCM' },
    { id: 3001, departmentId: 301, name: 'Marketing DN' },
    { id: 3002, departmentId: 302, name: 'Kế Toán DN' }
  ]);
  
  // State cho modal
  const isModalOpen = ref(false);
  
  // State cho các combobox
  const selectedFacility = ref('');
  const selectedDepartment = ref('');
  const selectedMajor = ref('');
  
  // Dữ liệu lọc cho combobox phòng ban và chuyên ngành
  const departments = ref([]);
  const majors = ref([]);
  
  // Hàm mở modal
  const openModal = () => {
    isModalOpen.value = true;
    // Reset các lựa chọn khi mở lại modal (tùy chọn)
    selectedFacility.value = '';
    selectedDepartment.value = '';
    selectedMajor.value = '';
    departments.value = [];
    majors.value = [];
  };
  
  // Hàm đóng modal
  const closeModal = () => {
    isModalOpen.value = false;
  };
  
  // Hàm tải danh sách phòng ban dựa trên cơ sở đã chọn
  const loadDepartments = () => {
    departments.value = departmentsData.value.filter(dept => dept.facilityId === selectedFacility.value);
    // Reset lựa chọn phòng ban và chuyên ngành khi thay đổi cơ sở
    selectedDepartment.value = '';
    selectedMajor.value = [];
  };
  
  // Hàm tải danh sách chuyên ngành dựa trên phòng ban đã chọn
  const loadMajors = () => {
    majors.value = majorsData.value.filter(major => major.departmentId === selectedDepartment.value);
    // Reset lựa chọn chuyên ngành khi thay đổi phòng ban
    selectedMajor.value = '';
  };
  
  // Hàm xử lý khi người dùng xác nhận lựa chọn
  const submitSelection = () => {
    if (selectedFacility.value && selectedDepartment.value && selectedMajor.value) {
      console.log('Cơ sở đã chọn:', facilities.value.find(f => f.id === selectedFacility.value)?.name);
      console.log('Phòng ban đã chọn:', departments.value.find(d => d.id === selectedDepartment.value)?.name);
      console.log('Chuyên ngành đã chọn:', majors.value.find(m => m.id === selectedMajor.value)?.name);
      closeModal();
      // Thực hiện các hành động khác với dữ liệu đã chọn
    } else {
      alert('Vui lòng chọn đầy đủ thông tin.');
    }
  };
</script>
  
<style scoped>
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5); /* Màu nền mờ */
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .modal-dialog {
    background-color: white;
    border-radius: 5px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
    width: 500px; /* Điều chỉnh độ rộng modal */
  }
  
  .modal-header {
    padding: 1rem;
    border-bottom: 1px solid #dee2e6;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .modal-title {
    margin-bottom: 0;
  }
  
  .btn-close {
    cursor: pointer;
    border: none;
    background-color: transparent;
    font-size: 1.5rem;
    opacity: 0.5;
  }
  
  .btn-close:hover {
    opacity: 0.75;
  }
  
  .modal-body {
    padding: 1rem;
  }
  
  .modal-footer {
    padding: 1rem;
    border-top: 1px solid #dee2e6;
    display: flex;
    justify-content: flex-end;
  }
  
  .fade {
    transition: opacity 0.15s linear;
  }
  
  .show {
    opacity: 1;
  }
  
  .modal-backdrop {
    position: fixed;
    top: 0;
    left: 0;
    z-index: 1040;
    width: 100vw;
    height: 100vh;
    background-color: #000;
  }
  
  .modal-backdrop.fade {
    opacity: 0;
  }
  
  .modal-backdrop.show {
    opacity: 0.5;
  }
</style>