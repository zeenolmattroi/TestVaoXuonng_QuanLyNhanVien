<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

const route = useRoute();
const employeeId = ref(route.params.employeeId);

const apiUrl = 'http://localhost:8080/quan-ly/department-major';

const employeeInfo = ref({
  code: '',
  name: '',
  accountFPT: '',
  accountFe: '',
});

const bases = ref([]);
const departmentsData = ref([]); // Để chứa tất cả phòng ban
const majorsData = ref([]);     // Để chứa tất cả chuyên ngành

const isAddEditModalOpen = ref(false);
const isEditMode = ref(false);
const modalData = ref({
  id: null,
  baseId: null,
  departmentId: null,
  majorId: null,
  employeeId: employeeId.value,
});
const modalError = ref('');

const subjectMajorList = ref([]);

const fetchEmployeeInfo = async () => {
  try {
    const getInfo = `http://localhost:8080/staff/${employeeId.value}`;

    const response = await axios.get(getInfo);
    employeeInfo.value = response.data.result;
  } catch (error) {
    console.error('Lỗi khi tải nhân viên:', error);
  }
};

const fetchEmployeeSubjectMajors = async () => {
  try {
    const getDetail = `${apiUrl}/${employeeId.value}`;
    const response = await axios.get(getDetail);
    subjectMajorList.value = response.data.result;
  } catch (error) {
    console.error('Lỗi khi tải danh sách cơ sở/phòng ban/chuyên ngành:', error);
  }
};

const fetchBases = async () => {
  try {
    const get = `${apiUrl}/facility/${employeeId.value}`;
    console.log(get)
    const response = await axios.get(get);
    bases.value = response.data.result;
  } catch (error) {
    console.error('Lỗi khi tải danh sách cơ sở:', error);
  }
};

const fetchDepartmentsByFac = async () => {
  try {
    const getApartment = `${apiUrl}/department/${modalData.value.baseId}`;
    const response = await axios.get(getApartment);
    departmentsData.value = response.data.result;
  } catch (error) {
    console.error('Lỗi khi tải danh sách phòng ban:', error);
  }
};

const fetchMajorsByDe = async () => {
  try {
    const get = `${apiUrl}/major/${modalData.value.baseId}/${modalData.value.departmentId}`;
    const response = await axios.get(get);
    majorsData.value = response.data.result;
  } catch (error) {
    console.error('Lỗi khi tải danh sách chuyên ngành:', error);
  }
};

const departments = computed(() => {
  if (modalData.value.baseId) {
    return departmentsData.value.filter(dept => dept.facilityId === modalData.value.baseId);
  }
  return [];
});

const filteredMajors = computed(() => {
  if (modalData.value.departmentId) {
    return majorsData.value.filter(major => major.departmentId === modalData.value.departmentId);
  }
  return [];
});

const openAddSubjectMajorModal = () => {
  isAddEditModalOpen.value = true;
  isEditMode.value = false;
  modalData.value = { id: null, baseId: null, departmentId: null, majorId: null, employeeId: employeeInfo.value.code };
  modalError.value = '';
};

const openEditSubjectMajorModal = (item) => {
  isAddEditModalOpen.value = true;
  isEditMode.value = true;
  modalData.value = { ...item };
  modalError.value = '';
};

const closeAddEditModal = () => {
  isAddEditModalOpen.value = false;
};

// const loadDepartmentsByFacility = () => {

//   modalData.value.departmentId = null;
//   modalData.value.majorId = null;
//   console.log("đã chọn co so")
// };

// const loadMajors = () => {
//   modalData.value.majorId = null;
// };

const saveSubjectMajor = async () => {
  modalError.value = '';
  if (!modalData.value.baseId || !modalData.value.departmentId || !modalData.value.majorId) {
    modalError.value = 'Vui lòng chọn đầy đủ Cơ sở, Phòng ban và Chuyên ngành.';
    return;
  }
  try {
    const apiUrl = isEditMode.value
      ? `http://localhost:8080/quan-ly/department-major/${modalData.value.id}` // API sửa
      : `http://localhost:8080/quan-ly/department-major`; // API thêm
    const method = isEditMode.value ? 'put' : 'post';

    const payload = {
      // id: modalData.value.id,
      idFacility: modalData.value.baseId,
      idDepartment: modalData.value.departmentId,
      idMajor: modalData.value.majorId,
      idStaff: employeeId.value,
    };
    console.log(payload)
    const response = await axios[method](apiUrl, payload);
    console.log(isEditMode.value ? 'Cập nhật thành công:' : 'Thêm thành công:', response.data.result);
    closeAddEditModal();
    fetchEmployeeSubjectMajors();
    fetchBases()
  } catch (error) {
    console.error(isEditMode.value ? 'Lỗi cập nhật:' : 'Lỗi thêm:', error);
    modalError.value = isEditMode.value ? 'Lỗi cập nhật cơ sở/phòng ban/chuyên ngành.' : 'Lỗi thêm cơ sở/phòng ban/chuyên ngành.';
    if (error.response && error.response.data && error.response.data.message) {
      modalError.value = error.response.data.message;
    }
  }
};

const deleteSubjectMajor = async (idFacilitydelete) => {
  if (confirm('Bạn có chắc chắn muốn xóa thông tin này?')) {
    try {
      const deleteUri = `http://localhost:8080/quan-ly/department-major/delete/${employeeId.value}/${idFacilitydelete}`
      console.log(deleteUri)
      await axios.delete(deleteUri);
      console.log('Xóa thành công ID:', idFacilitydelete);
      fetchEmployeeSubjectMajors()
    } catch (error) {
      console.error('Lỗi xóa:', error);
      alert('Lỗi khi xóa thông tin.');
    }
  }
};

const getBaseName = (baseId) => {
  const base = bases.value.find(b => b.id === baseId);
  return base ? base.name : '';
};

const getDepartmentName = (departmentId) => {
  const department = departmentsData.value.find(d => d.id === departmentId);
  return department ? department.name : '';
};

const getMajorName = (majorId) => {
  const major = majorsData.value.find(m => m.id === majorId);
  return major ? major.name : '';
};

onMounted(() => {
  fetchEmployeeInfo();
  fetchEmployeeSubjectMajors();
  fetchBases();
  // fetchDepartments();
  // fetchMajors();
});
</script>


<template>
  <div class="manage-subject-major">
    <h2>Quản lý bộ môn / chuyên ngành</h2>

    <div class="employee-info">
      <h3><span class="step-number">1</span> Thông tin nhân viên</h3>
      <div class="form-row">
        <div class="form-group">
          <label for="employeeCode">Mã:</label>
          <input type="text" id="employeeCode" v-model="employeeInfo.code" readonly>
        </div>
        <div class="form-group">
          <label for="employeeName">Tên:</label>
          <input type="text" id="employeeName" v-model="employeeInfo.name" readonly>
        </div>
        <div class="form-group">
          <label for="employeeEmailFPT">Email FPT:</label>
          <input type="email" id="employeeEmailFPT" v-model="employeeInfo.accountFPT" readonly>
        </div>
        <div class="form-group">
          <label for="employeeEmailFE">Email FE:</label>
          <input type="email" id="employeeEmailFE" v-model="employeeInfo.accountFE" readonly>
        </div>
      </div>
    </div>

    <div class="subject-major-list">
      <h3><i class="bi bi-list-ul"></i> Danh sách bộ môn, chuyên ngành theo cơ sở</h3>
      <div class="list-actions">
        <button class="btn btn-primary" @click="openAddSubjectMajorModal">
          <i class="bi bi-plus-lg"></i> Thêm bộ môn / chuyên ngành
        </button>
      </div>

      <table class="table">
        <thead>
          <tr>
            <th>STT</th>
            <th>Cơ sở</th>
            <th>Phòng ban</th>
            <th>Chuyên ngành</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in subjectMajorList" :key="item.id">
            <td>{{ index + 1 }}</td>
            <td>{{ item.nameFacility }}</td>
            <td>{{ item.nameDepartment }}</td>
            <td>{{ item.nameMajor }}</td>
            <td>
              <button class="btn btn-sm btn-warning me-2" @click="openEditSubjectMajorModal(item)">
                <i class="bi bi-pencil-square"></i> Sửa
              </button>
              <button class="btn btn-sm btn-danger" @click="deleteSubjectMajor(item.idFacility)">
                <i class="bi bi-trash"></i> Xóa
              </button>
            </td>
          </tr>
          <tr v-if="!subjectMajorList.length">
            <td colspan="5" class="text-center">Nhân viên này chưa có bộ môn hoặc chuyên ngành nào.</td>
          </tr>
        </tbody>
      </table>

      <p class="note">
        <span class="text-danger fw-bold">*** Chú ý:</span> Nhân viên chỉ có một bộ môn và một chuyên ngành trong một cơ
        sở ***
      </p>
    </div>

    <div v-if="isAddEditModalOpen" class="modal fade show" style="display: block;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ isEditMode ? 'Sửa' : 'Thêm' }} Thông Tin</h5>
            <button type="button" class="btn-close" @click="closeAddEditModal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="saveSubjectMajor">
              <div class="mb-3">
                <label for="base">Cơ sở:</label>
                <select class="form-select" id="base" v-model="modalData.baseId" @change="fetchDepartmentsByFac()">
                  <option value="" disabled>Chọn cơ sở</option>
                  <option v-for="base in bases" :value="base.id" :key="base.id">{{ base.name }}</option>
                </select>
              </div>
              <div class="mb-3">
                <label for="departmentSelect" class="form-label">Phòng ban:</label>
                <select class="form-select" id="departmentSelect" v-model="modalData.departmentId" @change="fetchMajorsByDe()"
                  :disabled="!modalData.baseId">
                  <option value="" disabled>Chọn phòng ban</option>
                  <option :value="department.id" :key="department.id" v-for="department in departmentsData">{{
                    department.name }}</option>
                </select>
              </div>
              <div class="mb-3">
                <label for="major">Chuyên ngành:</label>
                <select class="form-select" id="major" v-model="modalData.majorId" :disabled="!modalData.departmentId">
                  <option value="" disabled>Chọn chuyên ngành</option>
                  <option :value="major.id" :key="major.id" v-for="major in majorsData">{{ major.name }}</option>
                </select>
              </div>
              <div class="alert alert-danger" v-if="modalError" role="alert">
                {{ modalError }}
              </div>
              <button type="submit" class="btn btn-primary">{{ isEditMode ? 'Lưu' : 'Thêm' }}</button>
              <button type="button" class="btn btn-secondary" @click="closeAddEditModal">Đóng</button>
            </form>
          </div>
        </div>
      </div>
    </div>

    <div v-if="isAddEditModalOpen" class="modal-backdrop fade show"></div>
  </div>
</template>



<style scoped>
.manage-subject-major {
  padding: 20px;
}

.employee-info {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 5px;
  margin-bottom: 20px;
}

.employee-info h3 {
  margin-top: 0;
  margin-bottom: 10px;
}

.form-row {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.form-group {
  flex: 1 0 calc(25% - 20px);
  margin-bottom: 10px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input[type="text"],
.form-group input[type="email"],
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ced4da;
  border-radius: 4px;
}

.subject-major-list h3 {
  margin-top: 0;
  margin-bottom: 15px;
}

.list-actions {
  margin-bottom: 15px;
  display: flex;
  justify-content: flex-end;
}

.table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 15px;
}

.table th,
.table td {
  border: 1px solid #dee2e6;
  padding: 8px;
  text-align: left;
}

.table th {
  background-color: #e9ecef;
  font-weight: bold;
}

.note {
  font-size: 0.9em;
  color: #6c757d;
}

.modal-title {
  margin: 0;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid #dee2e6;
}

.modal-body {
  padding: 1rem;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  padding: 1rem;
  border-top: 1px solid #dee2e6;
}

.modal-footer button {
  margin-left: 0.5rem;
}

.step-number {
  background-color: #007bff;
  color: white;
  border-radius: 50%;
  padding: 2px 8px;
  font-size: 0.8em;
  margin-right: 5px;
}
</style>