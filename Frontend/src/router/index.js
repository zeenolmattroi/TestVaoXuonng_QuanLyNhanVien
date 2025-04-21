import { createRouter, createWebHistory } from 'vue-router'
import Staff from '@/components/Staff.vue';
import StaffDetail from '@/components/StaffDetail.vue';

const routes = [
    {
        path: "/",
        name: "Staff",
        component: Staff,
    },
    // {
    //     path: "/detail",
    //     name: "Detail",
    //     component: StaffDetail,
    // },
    {
        path: '/staff/detail/:employeeId', // Định nghĩa route có tham số ID
        name: 'StaffDetail',
        component: StaffDetail,
        props: true, // Truyền tham số route dưới dạng props cho component
    },

];

const router = createRouter({
    history: createWebHistory(),
    routes,
    // scrollBehavior() {
    //   document.getElementById("app").scrollIntoView({ behavior: "smooth" });
    // }
});



export default router
