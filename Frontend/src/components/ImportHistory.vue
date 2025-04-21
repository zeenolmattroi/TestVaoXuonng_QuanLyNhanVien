<template>
    <div class="import-history-page">
      <h2>Lịch sử Import</h2>
      <div class="modal fade show" style="display: block;">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Lịch sử Import</h5>
              <button type="button" class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <table class="table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Tên File</th>
                    <th>Thời gian Import</th>
                    <th>Trạng thái</th>
                    <th>Số lượng Bản ghi</th>
                    <th>Lỗi</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="history in importHistory" :key="history.id">
                    <td>{{ history.id }}</td>
                    <td>{{ history.fileName }}</td>
                    <td>{{ formatDate(history.importDate) }}</td>
                    <td>{{ history.status }}</td>
                    <td>{{ history.recordCount }}</td>
                    <td>{{ history.errorMessage }}</td>
                  </tr>
                </tbody>
              </table>
              <div v-if="importHistory.length === 0">
                Không có lịch sử import nào.
              </div>
            </div>
            <div class="modal-footer">
                <RouterLink :to="{ name: 'Staff' }">
                <button class="btn">Đóng</button>
              </RouterLink>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-backdrop fade show"></div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import { format } from 'date-fns';
  
  const isVisible = ref(true); // Đặt isVisible ban đầu là true
  const importHistory = ref([]);
  const apiUrl = 'http://localhost:8080/staff';
  
  // Không cần hàm openModal nữa vì modal sẽ hiển thị ngay khi component được tải
  
  const closeModal = () => {
    // Tùy thuộc vào router của bạn, bạn có thể muốn điều hướng người dùng đi khi đóng modal
    // Ví dụ: router.push('/staff-management');
    isVisible.value = false;
  };
  
  const fetchImportHistory = async () => {
    try {
      console.log("Fetching import history...");
      const response = await axios.get(`${apiUrl}/import-history`);
      importHistory.value = response.data.result;
      console.log("Import history:", importHistory.value);
    } catch (error) {
      console.error('Lỗi khi tải lịch sử import:', error);
      alert('Không thể tải lịch sử import.');
    }
  };
  
  const formatDate = (dateTimeString) => {
    if (dateTimeString) {
      try {
        return format(new Date(dateTimeString), 'yyyy-MM-dd HH:mm:ss');
      } catch (error) {
        console.error('Lỗi định dạng ngày tháng:', error);
        return '';
      }
    }
    return '';
  };
  
  onMounted(() => {
    fetchImportHistory();
  });
  
  // Không cần defineExpose openModal nữa
  </script>
  
<style scoped>
  /* Style cho trang và modal */
  .import-history-page {
    /* Có thể thêm style cho trang chứa modal */
  }
  
  .modal-backdrop {
    background-color: rgba(0, 0, 0, 0.5);
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1050;
  }
  
  .modal {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 1055;
    display: flex;
    flex-direction: column;
    background-color: #fff;
    border-radius: 0.3rem;
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
    overflow: hidden;
  }
  
  .modal-dialog {
    width: auto;
    max-width: 800px;
    margin: 1.75rem auto;
  }
  
  .modal-content {
    display: flex;
    flex-direction: column;
    width: 100%;
    pointer-events: auto;
    background-color: #fff;
    background-clip: padding-box;
    border: 1px solid rgba(0, 0, 0, 0.2);
    border-radius: 0.3rem;
    outline: 0;
  }
  
  .modal-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1rem 1rem;
    border-bottom: 1px solid #dee2e6;
    border-top-left-radius: 0.3rem;
    border-top-right-radius: 0.3rem;
  }
  
  .modal-title {
    margin-bottom: 0;
    line-height: 1.5;
  }
  
  .btn-close {
    padding: 0.5rem 0.5rem;
    margin: -0.5rem -0.5rem -0.5rem auto;
    background-color: transparent;
    border: 0;
    border-radius: 0.3rem;
    opacity: 0.5;
    transition: opacity 0.15s ease-in-out;
  }
  
  .btn-close:hover {
    opacity: 0.75;
  }
  
  .modal-body {
    position: relative;
    padding: 1rem;
  }
  
  .modal-footer {
    display: flex;
    flex-shrink: 0;
    flex-wrap: wrap;
    align-items: center;
    justify-content: flex-end;
    padding: 0.75rem;
    border-top: 1px solid #dee2e6;
    border-bottom-right-radius: 0.3rem;
    border-bottom-left-radius: 0.3rem;
  }
  
  .btn {
    /* ... style cho nút ... */
  }
  
  .btn-secondary {
    /* ... style cho nút đóng ... */
  }
</style>
  

<!-- <style scoped>
.modal-backdrop {
    background-color: rgba(0, 0, 0, 0.5);
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1050;
}

.modal {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 1055;
    display: flex;
    flex-direction: column;
    background-color: #fff;
    border-radius: 0.3rem;
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
    overflow: hidden;
}

.modal-dialog {
    width: auto;
    max-width: 800px;
    /* Tùy chỉnh độ rộng tối đa */
    margin: 1.75rem auto;
}

.modal-content {
    display: flex;
    flex-direction: column;
    width: 100%;
    pointer-events: auto;
    background-color: #fff;
    background-clip: padding-box;
    border: 1px solid rgba(0, 0, 0, 0.2);
    border-radius: 0.3rem;
    outline: 0;
}

.modal-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 1rem 1rem;
    border-bottom: 1px solid #dee2e6;
    border-top-left-radius: 0.3rem;
    border-top-right-radius: 0.3rem;
}

.modal-title {
    margin-bottom: 0;
    line-height: 1.5;
}

.btn-close {
    padding: 0.5rem 0.5rem;
    margin: -0.5rem -0.5rem -0.5rem auto;
    background-color: transparent;
    border: 0;
    border-radius: 0.3rem;
    opacity: 0.5;
    transition: opacity 0.15s ease-in-out;
}

.btn-close:hover {
    opacity: 0.75;
}

.modal-body {
    position: relative;
    padding: 1rem;
}

.modal-footer {
    display: flex;
    flex-shrink: 0;
    flex-wrap: wrap;
    align-items: center;
    justify-content: flex-end;
    padding: 0.75rem;
    border-top: 1px solid #dee2e6;
    border-bottom-right-radius: 0.3rem;
    border-bottom-left-radius: 0.3rem;
}

.btn {}

.btn-secondary {}
</style> -->