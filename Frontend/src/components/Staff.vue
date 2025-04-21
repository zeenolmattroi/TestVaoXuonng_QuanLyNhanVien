<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

// State
const allEmployees = ref([]); // Khởi tạo là một mảng rỗng
const searchQuery = ref('');
const selectedStatus = ref('');
const currentPage = ref(1);
const itemsPerPage = ref(5);
const isImportModalOpen = ref(false);
const isAddModalOpen = ref(false);
const selectedFile = ref(null);
const apiUrl = 'http://localhost:8080/staff';

const newEmployee = ref({
  code: '',
  name: '',
  accountFPT: '',
  accountFE: '',
  // Giá trị mặc định (ví dụ: đang hoạt động)
});

const isEditModalOpen = ref(false);
const editingEmployee = ref({
  id: null,
  code: '',
  name: '',
  accountFPT: '',
  accountFE: '',
  code: '',
});

const errorMessage = ref('');

onMounted(() => {

  fetchEmployees();

});

// Methods
const fetchEmployees = async () => {
  try {
    const response = await axios.get(apiUrl);


    if (Array.isArray(response.data.result)) {
      allEmployees.value = response.data.result;

    } else {
      console.error('Dữ liệu nhân viên từ API không phải là một mảng:', response.data);
      allEmployees.value = [];
    }
  } catch (error) {
    console.error('Lỗi khi tải dữ liệu nhân viên:', error);
    allEmployees.value = [];
  }
};

const submitAddEmployeeForm = async () => {
  // Reset thông báo lỗi
  errorMessage.value = '';

  // Kiểm tra các trường không được để trống
  if (!newEmployee.value.code) {
    errorMessage.value = 'Vui lòng nhập Mã nhân viên.';
    return;
  }
  if (!newEmployee.value.name) {
    errorMessage.value = 'Vui lòng nhập Tên nhân viên.';
    return;
  }
  if (!newEmployee.value.accountFPT) {
    errorMessage.value = 'Vui lòng nhập Email FPT.';
    return;
  }
  if (!newEmployee.value.accountFE) {
    errorMessage.value = 'Vui lòng nhập Email FE.';
    return;
  }

  // Kiểm tra định dạng email (không chứa tiếng Việt và kết thúc bằng @fpt.edu.vn hoặc @fe.edu.vn - đã sửa lại domain theo chuẩn)
  const emailFPTRegex = /^[^\u00C0-\u1FFF\s]+@fpt\.edu\.vn$/i;
  const emailFERegex = /^[^\u00C0-\u1FFF\s]+@fe\.edu\.vn$/i;

  if (!emailFPTRegex.test(newEmployee.value.accountFPT)) {
    errorMessage.value = 'Email FPT không hợp lệ. Vui lòng không nhập tiếng Việt và kết thúc bằng @fpt.edu.vn';
    return;
  }

  if (!emailFERegex.test(newEmployee.value.accountFE)) {
    errorMessage.value = 'Email FE không hợp lệ. Vui lòng không nhập tiếng Việt và kết thúc bằng @fe.edu.vn';
    return;
  }
  console.log(" validat xong" + newEmployee.value.code)

  // Nếu tất cả validation đều thành công, gọi hàm addEmployee
  await addEmployee(newEmployee.value);
};


const submitUpdateEmployeeForm = async () => {
  // Reset thông báo lỗi
  errorMessage.value = '';

  // Kiểm tra các trường không được để trống
  if (!editingEmployee.value.code) {
    errorMessage.value = 'Vui lòng nhập Mã nhân viên.';
    return;
  }
  if (!editingEmployee.value.name) {
    errorMessage.value = 'Vui lòng nhập Tên nhân viên.';
    return;
  }
  if (!editingEmployee.value.accountFPT) {
    errorMessage.value = 'Vui lòng nhập Email FPT.';
    return;
  }
  if (!editingEmployee.value.accountFE) {
    errorMessage.value = 'Vui lòng nhập Email FE.';
    return;
  }

  // Kiểm tra định dạng email (không chứa tiếng Việt và kết thúc bằng @fpt.edu.vn hoặc @fe.edu.vn - đã sửa lại domain theo chuẩn)
  const emailFPTRegex = /^[^\u00C0-\u1FFF\s]+@fpt\.edu\.vn$/i;
  const emailFERegex = /^[^\u00C0-\u1FFF\s]+@fe\.edu\.vn$/i;

  if (!emailFPTRegex.test(editingEmployee.value.accountFPT)) {
    errorMessage.value = 'Email FPT không hợp lệ. Vui lòng không nhập tiếng Việt và kết thúc bằng @fpt.edu.vn';
    return;
  }

  if (!emailFERegex.test(editingEmployee.value.accountFE)) {
    errorMessage.value = 'Email FE không hợp lệ. Vui lòng không nhập tiếng Việt và kết thúc bằng @fe.edu.vn';
    return;
  }
  console.log(" validat xong" + editingEmployee.value.code)


  await updateEmployee(editingEmployee.value);
};


const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
};

const openImportModal = () => {
  isImportModalOpen.value = true;
};

const closeImportModal = () => {
  isImportModalOpen.value = false;
  selectedFile.value = null;
};

const handleFileUpload = (event) => {
  selectedFile.value = event.target.files[0];
};

// const importEmployees = async () => {
//   // if (selectedFile.value) {
//   //   const formData = new FormData();
//   //   formData.append('file', selectedFile.value);

//   //   try {
//   //     const response = await axios.post(importApiUrl, formData, {
//   //       headers: {
//   //         'Content-Type': 'multipart/form-data',
//   //       },
//   //     });
//   //     console.log('Import thành công:', response.data);
//   //     closeImportModal();
//   //     fetchEmployees(); // Tải lại danh sách nhân viên sau khi import
//   //     // Hiển thị thông báo thành công cho người dùng
//   //   } catch (error) {
//   //     console.error('Lỗi import nhân viên:', error);
//   //     // Xử lý lỗi (ví dụ: hiển thị thông báo lỗi cho người dùng)
//   //   }
//   // } else {
//   //   alert('Vui lòng chọn file để import.');
//   // }
// };

const downloadTemplate = () => {
  const apiUrln = `${apiUrl}/export-data-to-excel`;
  window.open(apiUrln, '_blank');
  console.log(apiUrln)
  console.log('Yêu cầu tải file Excel đã được gửi...');
};


// const uploadExcelFileWithAxios = async (event) => {
//   const file = event.target.files[0];
//   if (!file) {
//     console.warn('Chưa chọn file.');
//     return;
//   }
//   const formData = new FormData();
//   formData.append('file', file);
//   try {
//     const response = await axios.post(`${apiUrl}/import-data-to-excel`, formData, {
//       headers: {
//         'Content-Type': 'multipart/form-data',
//       },
//     });
//     console.log('Kết quả import:', response.data.result);
//     alert(response.data.result || 'Đã import dữ liệu thành công.');
//     isImportModalOpen.value = false;
//     fetchEmployees();
//   } catch (error) {
//     console.error('Lỗi import:', error);
//     alert(error.response?.data?.message || 'Có lỗi xảy ra trong quá trình import.');
//   }
// };

const uploadExcelFileWithAxios = async () => {
  if (!selectedFile.value) {
    alert('Vui lòng chọn file Excel trước khi import.');
    return;
  }
  const formData = new FormData();
  formData.append('file', selectedFile.value);
  try {
    const response = await axios.post(`${apiUrl}/import-data-to-excel`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    console.log('Kết quả import:', response.data.result);
    alert(response.data.result || 'Đã import dữ liệu thành công.');
    isImportModalOpen.value = false;
    fetchEmployees();
  } catch (error) {
    console.error('Lỗi import:', error);
    alert(error.response?.data?.message || 'Có lỗi xảy ra trong quá trình import.');
  } finally {
    selectedFile.value = null;
  }
};



const openAddModal = () => {
  isAddModalOpen.value = true;
};

const closeAddModal = () => {
  isAddModalOpen.value = false;
};

const addEmployee = async (newEmployeeData) => {
  try {
    const response = await axios.post(apiUrl, newEmployeeData);
    fetchEmployees();
    closeAddModal();
  } catch (error) {
    errorMessage.value = 'Thêm thất bại. Vui lòng kiểm tra lại thông tin.';
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message;
    }
  }
};

const updateEmployee = async (newEmployee) => {
  try {
    const response = await axios.put(apiUrl, newEmployee);
    fetchEmployees();
    closeEditModal();
  } catch (error) {
    errorMessage.value = 'Cập nhật. Vui lòng kiểm tra lại thông tin.';
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage.value = error.response.data.message;
    }
  }
};



const updateEmployeeStatus = async (employeeId) => {
  const employeeToUpdate = allEmployees.value.find(emp => emp.id === employeeId);
  const currentStatus = employeeToUpdate?.trangThai == 1 ? 'Ngừng hoạt động' : 'Đang hoạt động';
  if (confirm(`Bạn có chắc chắn muốn thay đổi trạng thái của nhân viên này thành "${currentStatus}"?`)) {
    try {
      const updateStatusUrl = `${apiUrl}/update-status/${employeeId}`;
      const response = await axios.post(updateStatusUrl);
      const updatedEmployeeIndex = allEmployees.value.findIndex(emp => emp.id === employeeId);
      if (updatedEmployeeIndex !== -1) {
        allEmployees.value[updatedEmployeeIndex] = response.data.result;
      }
    } catch (error) {
      console.error('Lỗi cập nhật trạng thái cho ID:', employeeId, error);
      alert('Lỗi khi cập nhật trạng thái. Vui lòng thử lại.');
    }
  }
};

const openEditModal = (employee) => {
  editingEmployee.value = { ...employee };
  isEditModalOpen.value = true;
  errorMessage.value = '';
};
const closeEditModal = () => {
  isEditModalOpen.value = false;
};



const viewImportHistory = () => {

  console.log('Xem lịch sử import...');
};
</script>


<template>
  <div class="employee-management">
    <h1>Quản lý nhân viên</h1>

    <div class="filter-section">
      <div class="search-box">
        <label for="search">Tìm kiếm:</label>
        <input type="text" id="search" v-model="searchQuery" placeholder="Nhập mã, tên, email...">
      </div>
      <div class="status-filter">
        <label for="status">Trạng thái:</label>
        <select id="status" v-model="selectedStatus">
          <option value="">Tất cả</option>
          <option value="Đang hoạt động">Đang hoạt động</option>
          <option value="Ngừng hoạt động">Ngừng hoạt động</option>
        </select>
      </div>
    </div>

    <div class="employee-actions">
      <button class="btn" @click="openImportModal()">Import nhân viên</button>
      <button class="btn" @click="downloadTemplate()">Download Template</button>
      <button class="btn" @click="openAddModal()">Thêm nhân viên</button>
      <RouterLink :to="{ name: 'ImportHistory' }">
        <button class="btn">Xem lịch sử import</button>
      </RouterLink>
    </div>

    <div class="employee-list">
      <table>
        <thead>
          <tr>
            <th>STT</th>
            <th>Mã nhân viên</th>
            <th>Tên nhân viên</th>
            <th>Email FPT</th>
            <th>Email FE</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(employee, index) in allEmployees" :key="employee?.id">
            <td>{{ index + 1 }}</td>
            <td>{{ employee?.code }}</td>
            <td>{{ employee?.name }}</td>
            <td>{{ employee?.accountFPT }}</td>
            <td>{{ employee?.accountFE }}</td>
            <td>{{ employee?.status == 1 ? 'Đang hoạt động' : 'Ngừng hoạt động' }}</td>
            <td>
              <button @click="openEditModal(employee)">Sửa</button>
              <button @click="updateEmployeeStatus(employee?.id)">Thay đổi trạng thái</button>
              <!-- <button @click="detail(employee)">Chi tiết</button> -->
              <RouterLink :to="{ name: 'StaffDetail', params: { employeeId: employee.id } }">
                <button>Chi tiết</button>
              </RouterLink>
            </td>
          </tr>
          <tr v-if="!allEmployees.length">
            <td colspan="7">Không có nhân viên nào.</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-if="isImportModalOpen" class="modal fade show" style="display: block;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Import Nhân viên</h5>
            <button type="button" class="btn-close" @click="closeImportModal"></button>
          </div>
          <div class="modal-body">
            <input type="file" accept=".xlsx, .xls" @change="handleFileUpload">
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeImportModal">Đóng</button>
            <button type="button" class="btn btn-primary" @click="uploadExcelFileWithAxios" :disabled="!selectedFile">
              Import
            </button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="isImportModalOpen" class="modal-backdrop fade show"></div>

    <div v-if="isAddModalOpen" class="modal fade show" style="display: block;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Thêm Nhân viên</h5>
            <button type="button" class="btn-close" @click="closeAddModal"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="submitAddEmployeeForm">
              <div class="mb-3">
                <label for="maNhanVien" class="form-label">Mã nhân viên:</label>
                <input type="text" class="form-control" id="maNhanVien" v-model="newEmployee.code">
              </div>
              <div class="mb-3">
                <label for="tenNhanVien" class="form-label">Tên nhân viên:</label>
                <input type="text" class="form-control" id="tenNhanVien" v-model="newEmployee.name">
              </div>
              <div class="mb-3">
                <label for="emailFPT" class="form-label">Email FPT:</label>
                <input type="email" class="form-control" id="emailFPT" v-model="newEmployee.accountFPT">
              </div>
              <div class="mb-3">
                <label for="emailFE" class="form-label">Email FE:</label>
                <input type="email" class="form-control" id="emailFE" v-model="newEmployee.accountFE">
              </div>
              <div class="alert alert-danger" v-if="errorMessage" role="alert">
                {{ errorMessage }}
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeAddModal()">Đóng</button>
            <button type="submit" class="btn btn-primary" @click="submitAddEmployeeForm()">Thêm</button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="isAddModalOpen" class="modal-backdrop fade show"></div>




    <div v-if="isEditModalOpen" class="modal fade show" style="display: block;">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Chỉnh Sửa Nhân viên</h5>
            <button type="button" class="btn-close" @click="closeEditModal()"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="submitEditEmployeeForm">
              <div class="mb-3">
                <label for="editMaNhanVien" class="form-label">Mã nhân viên:</label>
                <input type="text" class="form-control" id="editMaNhanVien" v-model="editingEmployee.code">
              </div>
              <div class="mb-3">
                <label for="editTenNhanVien" class="form-label">Tên nhân viên:</label>
                <input type="text" class="form-control" id="editTenNhanVien" v-model="editingEmployee.name">
              </div>
              <div class="mb-3">
                <label for="editEmailFPT" class="form-label">Email FPT:</label>
                <input type="email" class="form-control" id="editEmailFPT" v-model="editingEmployee.accountFPT">
              </div>
              <div class="mb-3">
                <label for="editEmailFE" class="form-label">Email FE:</label>
                <input type="email" class="form-control" id="editEmailFE" v-model="editingEmployee.accountFE">
              </div>
              <div class="mb-3">
                <label for="editTrangThai" class="form-label">Trạng thái:</label>
                <select class="form-select" id="editTrangThai" v-model="editingEmployee.status">
                  <option value="1">Đang hoạt động</option>
                  <option value="0">Ngừng hoạt động</option>
                </select>
              </div>
              <div class="alert alert-danger" v-if="errorMessage" role="alert">
                {{ errorMessage }}
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="closeEditModal()">Đóng</button>
            <button type="submit" class="btn btn-primary"
              @click="submitUpdateEmployeeForm(editingEmployee.value)">Lưu</button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="isEditModalOpen" class="modal-backdrop fade show"></div>







  </div>
</template>


<style scoped>
.employee-management {
  padding: 20px;
}

.filter-section {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
  align-items: center;
}

.search-box,
.status-filter {
  display: flex;
  align-items: center;
}

.search-box label,
.status-filter label {
  margin-right: 5px;
}

.search-box input[type="text"],
.status-filter select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.employee-actions {
  margin-bottom: 15px;
}

.employee-actions button {
  margin-right: 10px;
  padding: 8px 15px;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
}

.employee-list {
  width: 100%;
  overflow-x: auto;
  margin-bottom: 15px;
}

.employee-list table {
  width: 100%;
  border-collapse: collapse;
}

.employee-list th,
.employee-list td {
  padding: 8px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}

.employee-list th {
  background-color: #f2f2f2;
}

.employee-list tbody tr:hover {
  background-color: #f5f5f5;
}

.employee-list button {
  margin-right: 5px;
  padding: 5px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
}

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}

.pagination button {
  padding: 8px 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-number {
  padding: 8px;
  cursor: pointer;
  margin-right: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.page-number.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-dialog {
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  width: 600px;
  /* Điều chỉnh độ rộng modal */
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