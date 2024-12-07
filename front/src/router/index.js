import { createRouter, createWebHashHistory } from 'vue-router';
import LoginPage from '../views/Login.vue';
import CustomerPage from '../views/CustomerPage.vue';
import MerchantPage from '../views/MerchantPage.vue';

const routes = [
  {
    path: '/',
    redirect: '/eShopF/login',
  },
  {
    path: '/eShopF/login',
    name: 'LoginPage',
    component: LoginPage,
  },
  {
    path: '/eShopF/customer-dashboard',
    name: 'CustomerDashboard',
    component: CustomerPage,
  },
  {
    path: '/eShopF/merchant-dashboard',
    name: 'MerchantDashboard',
    component: MerchantPage,
  }
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
