<template>
  <div class="app-container">
    <!-- 侧边悬浮栏 -->
    <div class="sidebar">
      <div class="sidebar-item" @click="showCartDialog">
        <img src="/icon/cart (1).png" alt="购物车" class="icon" />
        <span class="badge">{{ cartCount }}</span>
        <p>购物车</p>
      </div>
      <div class="sidebar-item" @click="toggleMyOptions">
        <img src="/icon/my.png" alt="我的" class="icon" />
        <p>我的</p>
        <!-- 浮动选项 -->
        <div v-if="showMyOptions" class="floating-options">
          <button @click="openAddressDialogForManagement">地址</button>
          <button @click="showOrderDialog = true; showMyOptions = false; fetchOrders()">订单</button>
        </div>
      </div>
      <div class="sidebar-item" @click="showService">
        <img src="/icon/service.png" alt="客服" class="icon" />
        <p>客服</p>
      </div>
      <div class="sidebar-item" @click="showFeedbackDialog = true">
        <img src="/icon/post.png" alt="反馈" class="icon" />
        <p>反馈</p>
      </div>
      <div class="sidebar-item" @click="scrollToTop">
        <img src="/icon/top (1).png" alt="回顶部" class="icon" />
        <p>回顶部</p>
      </div>
    </div>

    <!-- 客服对话框 -->
    <el-dialog v-model="showServiceDialog" title="客服" :width="400">
      <img src="wechatma/server.jpg" alt="客服图片" class="service-image" />
    </el-dialog>

    <!-- 反馈对话框 -->
    <el-dialog v-model="showFeedbackDialog" title="反馈" :width="400">
      <el-form :model="feedbackForm">
        <el-form-item>
          <el-input v-model="feedbackForm.srcemail" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="feedbackForm.content"
            type="textarea"
            placeholder="反馈意见"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitFeedback">提交反馈</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 其他内容 -->
    <div class="sticky-header">
      <div class="header">
        <img src="@/assets/image.png" alt="Logo" class="logo" />
        <div class="search-bar">
          <input
            type="text"
            v-model="searchQuery"
            placeholder="输入商品名"
            class="search-input"
          />
          <button @click="searchProduct" class="search-button">搜索</button>
        </div>
      </div>
      <div class="header-divider"></div>
      <div class="category-bar">
        <div v-for="(category, index) in categories" :key="category" class="category-item">
          <button @click="fetchProductsByCategory(category)">
            {{ category }}
          </button>
          <span v-if="index < categories.length - 1" class="divider"></span>
        </div>
      </div>
    </div>
    <div class="product-container">
      <div class="product-grid">
        <div v-for="product in products" :key="product.id" class="product-card" @click="showProductDetails(product)">
          <div class="product-image-wrapper">
            <img :src="formatPath(product.purl)" alt="商品图片" class="product-image" />
          </div>
          <div class="product-info">
            <h3 class="product-name">{{ product.pname }}</h3>
            <hr class="product-divider" />
            <p class="product-price">{{ product.ppri }}</p>
          </div>
        </div>
      </div>

      <el-dialog v-model="showDetails" title="商品详情" :width="600">
        <img :src="formatPath(selectedProduct.purl)" alt="商品图片" class="product-image-large" />
        <p>名称: {{ selectedProduct.pname }}</p>
        <p>类型: {{ selectedProduct.ptype }}</p>
        <p>描述: {{ selectedProduct.pdes }}</p>
        <p>库存: {{ selectedProduct.pcount }}</p>
        <p>价格: ¥{{ selectedProduct.ppri }}</p>

        <!-- 数量选择器 -->
        <div class="quantity-control">
          <button @click="decreaseQuantity">-</button>
          <input type="number" v-model.number="purchaseQuantity" min="1" class="quantity-input" />
          <button @click="increaseQuantity">+</button>
        </div>

        <!-- 按钮 -->
        <div class="button-group">
          <button @click="addToCart" class="add-to-cart-button">加入购物车</button>
          <!-- <button @click="openAddressDialogForCheckout" class="buy-now-button">立即购买</button> -->
        </div>
      </el-dialog>

      <!-- 购物车弹窗 -->
      <el-dialog v-model="showCart" title="购物车" :width="600">
        <div class="cart-content">
          <div v-for="item in cartItems" :key="item.id" class="cart-item">
            <input type="checkbox" v-model="item.selected" /> <!-- 添加复选框 -->
            <div class="cart-item-image">
              <img :src="formatPath(item.purl)" alt="商品图片" />
            </div>
            <div class="cart-item-info">
              <p class="cart-item-name">{{ item.pname }}</p>
              <p class="cart-item-price">价格: ¥{{ item.ppri }}</p>
              <div class="quantity-control">
                <button @click="decreaseCartItemQuantity(item)">-</button>
                <input type="number" v-model.number="item.quantity" min="1" class="quantity-input" />
                <button @click="increaseCartItemQuantity(item)">+</button>
              </div>
            </div>
          </div>
          <div class="cart-footer">
            <button class="checkout-button" @click="checkout">结算</button>
          </div>
        </div>
      </el-dialog>

      <!-- 地址管理对话框 -->
      <el-dialog v-model="showAddressDialog" title="地址管理" :width="600">
        <div class="address-list">
          <div
            v-for="address in addresses"
            :key="address.aid"
            class="address-item"
            @click="selectAddress(address)"
            :class="{ 'selected-address': selectedAddress && selectedAddress.aid === address.aid }"
          >
            <p class="address">{{ address.addressName }}</p>
            <div class="address-info">
              <span>{{ address.uname }}</span>
              <span>{{ address.uphone }}</span>
              <span>{{ address.umail }}</span>
              <button @click.stop="editAddress(address)" class="edit-button">编辑</button>
            </div>
          </div>
        </div>
        <div class="button-group">
          <button @click="showAddAddressDialog = true" class="add-address-button">+</button>
          <button @click="deleteSelectedAddress" class="delete-button">-</button>
        </div>
        <div class="button-group" v-if="isCheckoutProcess">
          <button @click="confirmPurchaseWithAddress" class="confirm-button">确认购买</button>
        </div>
      </el-dialog>

      <!-- 新增地址对话框 -->
      <el-dialog v-model="showAddAddressDialog" title="新增地址" :width="400">
        <div class="add-address-form">
          <input v-model="newAddress.uname" placeholder="请输入姓名" />
          <input v-model="newAddress.uphone" placeholder="请输入电话" />
          <input v-model="newAddress.addressName" placeholder="请输入地址" />
          <input v-model="newAddress.umail" placeholder="请输入邮箱" />
          <button @click="addAddress" class="confirm-button">确认</button>
        </div>
      </el-dialog>

      <!-- 编辑地址对话框 -->
      <el-dialog v-model="showEditAddressDialog" title="编辑地址" :width="400">
        <div class="edit-address-form">
          <input v-model="editAddressData.uname" placeholder="请输入姓名" />
          <input v-model="editAddressData.uphone" placeholder="请输入电话" />
          <input v-model="editAddressData.addressName" placeholder="请输入地址" />
          <input v-model="editAddressData.umail" placeholder="请输入邮箱" />
          <button @click="updateAddress" class="confirm-button">确认</button>
        </div>
      </el-dialog>

      <!-- 订单管理对话框 -->
      <el-dialog v-model="showOrderDialog" title="订单管理" :width="1200">
        <div class="order-content">
          <div v-for="order in orders" :key="order.oid" class="order-item" @dblclick="fetchOrderDetails(order.oid)">
            <div class="order-item-row">
              <div class="order-item-block">
                <div class="order-item-label">订单创建时间:</div>
                <div class="order-item-value">{{ formatOrderTime(order.otime) }}</div>
              </div>
              <div class="order-item-block">
                <div class="order-item-label">订单状态:</div>
                <div class="order-item-value">{{ formatOrderState(order.ostate) }}</div>
              </div>
              <div class="order-item-block">
                <div class="order-item-label">商品种数:</div>
                <div class="order-item-value">{{ order.pcount }}</div>
              </div>
              <div class="order-item-block">
                <div class="order-item-label">姓名:</div>
                <div class="order-item-value">{{ order.address.split(' ')[0] }}</div>
              </div>
              <div class="order-item-block">
                <div class="order-item-label">邮箱:</div>
                <div class="order-item-value">{{ order.address.split(' ')[1] }}</div>
              </div>
              <div class="order-item-block">
                <div class="order-item-label">地址:</div>
                <div class="order-item-value">{{ order.address.split(' ')[2] }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-dialog>

      <!-- 订单详情对话框 -->
      <el-dialog v-model="showOrderDetailsDialog" title="订单详情" :width="600">
        <div class="order-details-content">
          <div v-for="detail in orderDetails" :key="detail.odid" class="order-detail-item">
            <img :src="formatPath(detail.product.purl)" alt="商品图片" class="order-detail-image" />
            <p class="order-detail-name">{{ detail.product.pname }}</p>
            <p class="order-detail-quantity">数量: {{ detail.odPCount }}</p>
            <p class="order-detail-money">金额: ¥{{ detail.odPMoney }}</p>
          </div>
          <!-- 显示总金额 -->
          <div class="order-total">
            <p class="order-total-label">总金额：</p>
            <p class="order-total-amount">¥{{ calculateTotalAmount() }}</p>
          </div>
        </div>
        <!-- 立即付款和删除订单按钮 -->
        <div class="order-details-footer">
          <button class="pay-now-button" @click="showPaymentDialog = true">立即付款</button>
          <button class="delete-order-button" @click="deleteOrder">删除订单</button>
        </div>
      </el-dialog>

      <!-- 支付图片对话框 -->
      <el-dialog v-model="showPaymentDialog" title="支付" :width="400">
        <img src="/wechatma/pay.jpg" alt="支付图片" class="payment-image" />
        <div class="payment-footer">
          <button class="confirm-payment-button" @click="confirmPayment">立即购买</button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';

export default {
  name: 'CustomerPage',
  setup() {
    const isCheckoutProcess = ref(false);
    const products = ref([]);
    const showDetails = ref(false);
    const selectedProduct = ref({});
    const categories = ref(['全部', '家居日用', '穿搭服饰', '米面粮油', '生鲜水果', '科技数码', '运动户外']);
    const searchQuery = ref('');
    const deliveryAddress = ref('');
    const purchaseQuantity = ref(1);
    const showPurchaseForm = ref(false);
    const cartCount = ref(0);
    const showCart = ref(false);
    const cartItems = ref([]);
    const showMyOptions = ref(false);
    const showAddressDialog = ref(false);
    const showOrderDialog = ref(false);
    const addresses = ref([]);
    const orders = ref([]);
    const newAddress = ref({
      uname: '',
      uphone: '',
      addressName: '',
      umail: '',
    });
    const showAddAddressDialog = ref(false);
    const showEditAddressDialog = ref(false);
    const editAddressData = ref({
      aid: '',
      uname: '',
      uphone: '',
      addressName: '',
      umail: '',
    });
    const selectedAddress = ref(null);
    const showOrderDetailsDialog = ref(false);
    const orderDetails = ref([]);
    const showPaymentDialog = ref(false);
    const showFeedbackDialog = ref(false);
    const feedbackForm = ref({
      srcemail: '',
      content: '',
    });
    const showServiceDialog = ref(false);

    const fetchProducts = async (category = '全部') => {
      try {
        const url = category === '全部' ? '/eShop/product/alllist' : `/eShop/product/typelist?pType=${category}`;
        const response = await axios.get(url);
        if (response.data.code === 200) {
          products.value = response.data.data;
        } else {
          console.error('Failed to fetch products:', response.data.msg);
        }
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    const fetchProductsByCategory = (category) => {
      fetchProducts(category);
    };

    const searchProduct = async () => {
      try {
        const response = await axios.get(`/eShop/product/getproduct`, {
          params: { pName: searchQuery.value },
        });
        if (response.data.code === 200) {
          products.value = response.data.data;
        } else {
          console.error('Failed to fetch products:', response.data.msg);
        }
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    const showProductDetails = async (product) => {
      selectedProduct.value = product;
      showDetails.value = true;
      showPurchaseForm.value = false;

      // 获取用户ID
      const uid = sessionStorage.getItem('uid');
      if (!uid) {
        console.error('User ID not found in localStorage');
        return;
      }

      // 准备请求数据
      const requestData = {
        uid0: uid,
        pid: product.pid,
        seTime: Date.now(), // 当前时间戳
      };

      try {
        // 发送POST请求
        const response = await axios.post('/eShop/shopexp/addshopexp', requestData);
        if (response.data.code === 200) {
          console.log('商品浏览记录已添加');
        } else {
          console.error('Failed to add shop experience:', response.data.msg);
        }
      } catch (error) {
        console.error('Error adding shop experience:', error);
      }
    };

    const formatPath = (path) => path.replace(/\\/g, '/');

    const decreaseQuantity = () => {
      if (purchaseQuantity.value > 1) {
        purchaseQuantity.value--;
      }
    };

    const increaseQuantity = () => {
      purchaseQuantity.value++;
    };

    const addToCart = async () => {
      const uid = sessionStorage.getItem('uid');
      if (!uid) {
        console.error('User ID not found in localStorage');
        return;
      }

      const cartData = {
        uid0: uid,
        pid: selectedProduct.value.pid,
        p_id_count: purchaseQuantity.value,
      };

      try {
        const response = await axios.post('/eShop/shopcar/addshopcar', cartData);
        if (response.data.code === 200) {
          alert('商品已加入购物车！');
          fetchCartCount();
        } else {
          alert('加入购物车失败，请重试。');
        }
      } catch (error) {
        console.error('Error adding to cart:', error);
        alert('加入购物车失败，请重试。');
      }
    };

    const confirmPurchaseWithAddress = async () => {
      if (!selectedAddress.value) {
        alert('请选择一个地址');
        return;
      }

      const uid = sessionStorage.getItem('uid');
      if (!uid) {
        console.error('User ID not found in localStorage');
        return;
      }

      // 计算选中的商品种数
      const selectedItems = cartItems.value.filter(item => item.selected);
      const selectedProductCount = selectedItems.length;

      const orderData = {
        uid0: uid,
        ostate: '0',
        otime: Date.now(),
        pcount: selectedProductCount.toString(), // 使用选中的商品种数
        address: `${selectedAddress.value.uname} ${selectedAddress.value.umail} ${selectedAddress.value.addressName}`,
      };

      try {
        const orderResponse = await axios.post('/eShop/orders/addorders', orderData);
        const oid = orderResponse.data.data;

        if (oid) {
          const orderDetailData = selectedItems.map(item => ({
            uid0: uid,
            pid: item.pid,
            p_id_count: item.quantity,
          }));

          const orderDetailResponse = await axios.post('/eShop/shopcar/ordershopcar', orderDetailData, {
            params: { oid: oid }
          });
          if (orderDetailResponse.data.code === 200) {
            alert('订单提交成功！');
            showAddressDialog.value = false;
            fetchCartItems();
            fetchCartCount();
          } else {
            alert('订单提交失败，请重试。');
          }
        } else {
          alert('订单提交失败，请重试。');
        }
      } catch (error) {
        console.error('Error processing purchase:', error);
        alert('订单提交失败，请重试。');
      }
    };

    const fetchCartCount = async () => {
      const uid = sessionStorage.getItem('uid');
      if (!uid) {
        console.error('User ID not found in localStorage');
        return;
      }

      try {
        const response = await axios.get('/eShop/shopcar/getusershopcarcount', {
          params: { uid: uid },
        });
        if (response.data.code === 200) {
          cartCount.value = response.data.data;
        } else {
          console.error('Failed to fetch cart count:', response.data.msg);
        }
      } catch (error) {
        console.error('Error fetching cart count:', error);
      }
    };

    const fetchCartItems = async () => {
      const uid = sessionStorage.getItem('uid');
      if (!uid) {
        console.error('User ID not found in localStorage');
        return;
      }

      try {
        const response = await axios.get('/eShop/shopcar/getusershopcar', {
          params: { uid: uid },
        });
        if (response.data.code === 200) {
          const items = response.data.data;

          for (const item of items) {
            try {
              const countResponse = await axios.get('/eShop/shopcar/getproductcount', {
                params: { uid0: uid, pId: item.pid },
              });

              if (countResponse.data.code === 200) {
                item.quantity = countResponse.data.data;
              } else {
                console.error('Failed to fetch product count:', countResponse.data.msg);
                item.quantity = 1;
              }
            } catch (error) {
              console.error('Error fetching product count:', error);
              item.quantity = 1;
            }
            item.selected = false;
          }

          cartItems.value = items;
        } else {
          console.error('Failed to fetch cart items:', response.data.msg);
        }
      } catch (error) {
        console.error('Error fetching cart items:', error);
      }
    };

    const fetchOrders = async () => {
      const uid = sessionStorage.getItem('uid');
      if (!uid) {
        console.error('User ID not found in localStorage');
        return;
      }

      try {
        const response = await axios.get('/eShop/orders/getuserorders', {
          params: { uid: uid },
        });
        if (response.data.code === 200) {
          orders.value = response.data.data;
        } else {
          console.error('Failed to fetch orders:', response.data.msg);
        }
      } catch (error) {
        console.error('Error fetching orders:', error);
      }
    };

    const fetchOrderDetails = async (oid) => {
      try {
        const response = await axios.get('/eShop/orders/getordersdetail', {
          params: { oid: oid },
        });
        if (response.data.code === 200) {
          const details = response.data.data;

          for (const detail of details) {
            const productResponse = await axios.get('/eShop/product/getproductbyid', {
              params: { pid: detail.pid },
            });
            if (productResponse.data.code === 200) {
              detail.product = productResponse.data.data;
            } else {
              console.error('Failed to fetch product:', productResponse.data.msg);
            }
          }

          orderDetails.value = details;
          showOrderDetailsDialog.value = true;
        } else {
          console.error('Failed to fetch order details:', response.data.msg);
        }
      } catch (error) {
        console.error('Error fetching order details:', error);
      }
    };

    const calculateTotalAmount = () => {
      return orderDetails.value.reduce((total, detail) => {
        const amount = parseFloat(detail.odPMoney);
        return total + (isNaN(amount) ? 0 : amount);
      }, 0);
    };

    const confirmPayment = async () => {
      const oid = orderDetails.value[0]?.oid;
      if (!oid) {
        alert('无法获取订单ID');
        return;
      }
      const uid = sessionStorage.getItem('uid');
  if (!uid) {
    console.error('User ID not found in sessionStorage');
    return;
  }
      try {
        const response = await axios.post('/eShop/orders/updateorders', {
          oid: oid,
          ostate: 1,
        },{
      params: { uid: uid } // 在这里添加 uid 作为请求参数
    });
        if (response.data.code === 200) {
          alert('订单已付款！');
          showPaymentDialog.value = false;
          showOrderDetailsDialog.value = false;
          fetchOrders();
        } else {
          alert('付款失败，请重试。');
        }
      } catch (error) {
        console.error('Error updating order:', error);
        alert('付款失败，请重试。');
      }
    };

    const deleteOrder = async () => {
      const oid = orderDetails.value[0]?.oid;
      if (!oid) {
        alert('无法获取订单ID');
        return;
      }

      try {
        const response = await axios.post('/eShop/orders/deleteorders', {
          oid: oid
        });
        if (response.data.code === 200) {
          alert('订单删除成功！');
          showOrderDetailsDialog.value = false;
          fetchOrders();
        } else {
          alert('订单删除失败，请重试。');
        }
      } catch (error) {
        console.error('Error deleting order:', error);
        alert('订单删除失败，请重试。');
      }
    };

    const deleteSelectedAddress = async () => {
      if (!selectedAddress.value) {
        alert('请选择要删除的地址');
        return;
      }

      try {
        const response = await axios.post('/eShop/address/deleteuseraddress', {
          aid: selectedAddress.value.aid
        });
        if (response.data.code === 200) {
          alert('地址删除成功！');
          fetchAddresses();
          selectedAddress.value = null;
        } else {
          alert('地址删除失败，请重试。');
        }
      } catch (error) {
        console.error('Error deleting address:', error);
        alert('地址删除失败，请重试。');
      }
    };

    const formatOrderTime = (timestamp) => {
      const numericTimestamp = Number(timestamp);
      if (typeof numericTimestamp !== 'number') {
        console.error('Invalid timestamp:', numericTimestamp);
        return 'Invalid date';
      }
      const date = new Date(numericTimestamp);
      const hours = date.getHours();
      const minutes = date.getMinutes();
      const seconds = date.getSeconds();
      return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日` +
        `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    };

    const formatOrderState = (state) => {
      switch (state) {
        case 0:
          return '未付款';
        case 1:
          return '已付款';
        case 2:
          return '已到货';
        default:
          return '未知状态';
      }
    };

    const decreaseCartItemQuantity = (item) => {
      if (item.quantity > 1) {
        item.quantity--;
      }
    };

    const increaseCartItemQuantity = (item) => {
      item.quantity++;
    };

    const showCartDialog = () => {
      fetchCartItems();
      showCart.value = true;
    };

    const scrollToTop = () => {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const checkout = () => {
      const selectedItems = cartItems.value.filter(item => item.selected);

      if (selectedItems.length === 0) {
        alert('请选择要结算的商品');
        return;
      }

      openAddressDialogForCheckout();
    };

    const fetchAddresses = async () => {
      const uid = sessionStorage.getItem('uid');
      if (!uid) {
        console.error('User ID not found in localStorage');
        return;
      }

      try {
        const response = await axios.get('/eShop/address/getuseraddress', {
          params: { uid: uid },
        });
        if (response.data.code === 200) {
          addresses.value = response.data.data;
        } else {
          console.error('Failed to fetch addresses:', response.data.msg);
        }
      } catch (error) {
        console.error('Error fetching addresses:', error);
      }
    };

    const addAddress = async () => {
      const uid = sessionStorage.getItem('uid');
      if (!uid) {
        console.error('User ID not found in localStorage');
        return;
      }

      try {
        const response = await axios.post('/eShop/address/adduseraddress', {
          ...newAddress.value,
          uid: uid,
        });
        if (response.data.code === 200) {
          alert('地址添加成功！');
          fetchAddresses();
          newAddress.value = { uname: '', uphone: '', addressName: '', umail: '' };
          showAddAddressDialog.value = false;
        } else {
          alert('地址添加失败，请重试。');
        }
      } catch (error) {
        console.error('Error adding address:', error);
        alert('地址添加失败，请重试。');
      }
    };

    const editAddress = (address) => {
      editAddressData.value = { ...address };
      showEditAddressDialog.value = true;
    };

    const updateAddress = async () => {
      const uid = sessionStorage.getItem('uid');
      if (!uid) {
        console.error('User ID not found in localStorage');
        return;
      }

      try {
        const response = await axios.post('/eShop/address/updateuseraddress', {
          ...editAddressData.value,
          uid: uid,
        });
        if (response.data.code === 200) {
          alert('地址更新成功！');
          fetchAddresses();
          showEditAddressDialog.value = false;
        } else {
          alert('地址更新失败，请重试。');
        }
      } catch (error) {
        console.error('Error updating address:', error);
        alert('地址更新失败，请重试。');
      }
    };

    const deleteAddress = async (aid) => {
      try {
        const response = await axios.delete(`/eShop/address/delete/${aid}`);
        if (response.data.code === 200) {
          alert('地址删除成功！');
          fetchAddresses();
        } else {
          alert('地址删除失败，请重试。');
        }
      } catch (error) {
        console.error('Error deleting address:', error);
        alert('地址删除失败，请重试。');
      }
    };

    const selectAddress = (address) => {
      selectedAddress.value = address;
    };

    const toggleMyOptions = () => {
      showMyOptions.value = !showMyOptions.value;
    };

    const openAddressDialogForCheckout = () => {
      isCheckoutProcess.value = true;
      showAddressDialog.value = true;
    };

    const openAddressDialogForManagement = () => {
      isCheckoutProcess.value = false;
      showAddressDialog.value = true;
      showMyOptions.value = false;
    };

    const submitFeedback = async () => {
      try {
        const feedbackData = {
          srcemail: feedbackForm.value.srcemail,
          content: feedbackForm.value.content,
          timestamp: Date.now(),
        };

        const response = await axios.post('/eShop/user/feedback', feedbackData);

        const { code, msg } = response.data;

        if (code === 200 && msg === '成功') {
          ElMessage.success('反馈提交成功');
          showFeedbackDialog.value = false;
        } else {
          ElMessage.error(msg || '反馈提交失败');
        }
      } catch (error) {
        ElMessage.error('请求失败，请稍后重试');
      }
    };

    const showService = () => {
      showServiceDialog.value = true;
    };

    onMounted(() => {
      const jwt = sessionStorage.getItem('jwt');
      if (jwt) {
        axios.defaults.headers.common['Authorization'] = `${jwt}`;
      }
      fetchProducts();
      fetchCartCount();
      fetchAddresses();
    });

    return {
      products,
      showDetails,
      selectedProduct,
      categories,
      searchQuery,
      deliveryAddress,
      purchaseQuantity,
      showPurchaseForm,
      cartCount,
      showCart,
      cartItems,
      showMyOptions,
      showAddressDialog,
      showOrderDialog,
      addresses,
      orders,
      newAddress,
      showAddAddressDialog,
      showEditAddressDialog,
      editAddressData,
      selectedAddress,
      showOrderDetailsDialog,
      orderDetails,
      showPaymentDialog,
      showFeedbackDialog,
      feedbackForm,
      showServiceDialog,
      fetchProductsByCategory,
      searchProduct,
      showProductDetails,
      formatPath,
      decreaseQuantity,
      increaseQuantity,
      addToCart,
      confirmPurchaseWithAddress,
      scrollToTop,
      isCheckoutProcess,
      showCartDialog,
      decreaseCartItemQuantity,
      increaseCartItemQuantity,
      checkout,
      fetchAddresses,
      addAddress,
      editAddress,
      updateAddress,
      deleteAddress,
      selectAddress,
      toggleMyOptions,
      openAddressDialogForCheckout,
      openAddressDialogForManagement,
      fetchOrders,
      fetchOrderDetails,
      calculateTotalAmount,
      confirmPayment,
      deleteOrder,
      deleteSelectedAddress,
      formatOrderTime,
      formatOrderState,
      submitFeedback,
      showService,
    };
  },
};
</script>




<style scoped>
.service-image {
  width: 100%;
  height: auto;
  max-width: 350px; /* 限制图片的最大宽度 */
  margin: 0 auto; /* 居中对齐 */
  display: block; /* 将图片作为块级元素 */
  border-radius: 8px; /* 圆角效果 */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

.el-dialog {
  max-width: 400px;
  margin: 0 auto;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-input {
  width: 100%;
}

.el-button {
  display: block;
  margin: 0 auto;
}

.payment-image {
  width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
  border-radius: 8px;
}

.payment-footer {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.confirm-payment-button {
  background-color: #4CAF50; /* 绿色背景 */
  color: white; /* 白色文字 */
  padding: 10px 20px; /* 内边距 */
  border: none; /* 无边框 */
  border-radius: 5px; /* 圆角 */
  cursor: pointer; /* 鼠标指针 */
  font-size: 16px; /* 字体大小 */
  transition: background-color 0.3s; /* 背景色过渡效果 */
}

.confirm-payment-button:hover {
  background-color: #45a049; /* 悬停时的背景色 */
}

.order-details-footer {
  display: flex; /* 使用 flexbox 布局 */
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  margin-top: 20px; /* 顶部外边距 */
  padding-bottom: 20px; /* 底部内边距 */
}

.delete-order-button {
  background-color: #ff4d4f;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.delete-order-button:hover {
  background-color: #d9363e;
}

.pay-now-button {
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-right: 10px; /* 右侧外边距 */
}

.pay-now-button:hover {
  background-color: #2980b9;
}

.order-details-content {
  display: flex;
  flex-direction: column;
  gap: 10px; /* 每个订单项之间的间距 */
}

.order-detail-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.order-detail-image {
  width: 80px; /* 缩略图宽度 */
  height: 80px; /* 缩略图高度 */
  object-fit: cover; /* 保持图片比例 */
  margin-right: 15px; /* 图片与文本之间的间距 */
}

.order-detail-name {
  flex: 2; /* 占据较多空间 */
  font-size: 16px;
  font-weight: bold;
}

.order-detail-quantity,
.order-detail-money {
  flex: 1; /* 占据较少空间 */
  font-size: 14px;
  text-align: center; /* 居中对齐 */
}
.order-content {
  max-width: 100%; /* 允许对话框占据更大的宽度 */
  padding: 10px;
}

.order-item {
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 15px;
  margin-bottom: 10px;
  background-color: #f9f9f9;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.order-item-row {
  display: flex;
  justify-content: space-between;
  align-items: center; /* 垂直居中对齐 */
  margin-bottom: 5px;
}

.order-item-block {
  flex: 1;
  text-align: center;
  min-width: 150px; /* 增加最小宽度 */
}

.order-item-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.order-item-value {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}




.order-total {
  display: flex;
  justify-content: flex-end; /* 将总金额对齐到右侧 */
  align-items: center;
  padding: 10px;
  border-top: 1px solid #ddd; /* 添加顶部边框 */
  margin-top: 10px; /* 与上方内容的间距 */
}

.order-total-label {
  font-size: 16px;
  font-weight: bold;
  margin-right: 10px; /* 标签与金额之间的间距 */
}

.order-total-amount {
  font-size: 16px;
  font-weight: bold;
  color: #e74c3c; /* 红色字体 */
}




.selected-address {
  background-color: #e0f7fa; /* 淡蓝色背景 */
  border: 2px solid #06899a; /* 选中时的边框色 */
}

.app-container {
  position: relative;
  min-height: 100vh;
  background: linear-gradient(to bottom, #b3e5fc, #f8bbd0);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 20px;
}

.sidebar {
  position: fixed;
  top: 50%;
  right: 0;
  transform: translateY(-50%);
  background-color: white;
  border-radius: 10px 0 0 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  z-index: 5000;
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 380px;
  width: 60px;
}

.sidebar-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  width: 100%;
  cursor: pointer;
  transition: background-color 0.3s;
  position: relative;
  padding-top: 8px;
}

.sidebar-item p {
  margin-top: 2px;
}

.sidebar-item:hover {
  background-color: #f0f0f0;
}

.icon {
  width: 24px;
  height: 24px;
  margin-bottom: 2px;
}

.badge {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: red;
  color: white;
  border-radius: 50%;
  padding: 2px 6px;
  font-size: 12px;
}

.floating-options {
  position: absolute;
  top: 50%;
  left: -70px;
  transform: translateY(-50%);
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  display: flex;
  flex-direction: column;
  padding: 10px;
}

.floating-options button {
  margin-bottom: 5px;
  padding: 5px 10px;
  background-color: #e0f7fa;
  color: #007bff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.floating-options button:hover {
  background-color: #b3e5fc;
}

.sticky-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  width: 100%;
  background: linear-gradient(to bottom, #f8bbd0, #b3e5fc);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-top: -20px;
  padding: 5px 0;
}

.header {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 5px;
}

.logo {
  width: 60px;
  height: auto;
  margin-right: 20px;
}

.search-bar {
  display: flex;
  align-items: center;
}

.search-input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  width: 600px;
  margin-right: 10px;
}

.search-button {
  padding: 8px 16px;
  background-color: #e0f7fa;
  color: #007bff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-button:hover {
  background-color: #b3e5fc;
}

.header-divider {
  width: 100%;
  height: 1px;
  background-color: #e0e0e0;
  margin: 5px 0;
}

.category-bar {
  display: flex;
  justify-content: center;
  padding: 5px;
}

.category-item {
  display: flex;
  align-items: center;
}

.category-item button {
  padding: 5px 10px;
  border: none;
  background-color: transparent;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 16px;
  font-family: 'Arial', sans-serif;
  color: #007bff;
}

.category-item button:hover {
  background-color: rgba(179, 229, 252, 0.5);
  color: #0056b3;
}

.divider {
  width: 1px;
  height: 20px;
  background-color: #e0e0e0;
  margin: 0 10px;
}

.product-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 90%;
  margin: 20px auto;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  width: 100%;
  padding: 20px;
  box-sizing: border-box;
}

.product-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s;
  background-color: white;
}

.product-card:hover {
  transform: scale(1.05);
}

.product-image-wrapper {
  width: 100%;
  height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.product-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.product-info {
  padding: 10px;
  text-align: left;
}

.product-divider {
  margin: 10px 0;
  border: none;
  border-top: 1px solid #e0e0e0;
}

.product-name {
  font-size: 16px;
  margin: 0;
}

.product-price {
  color: #ff5722;
  font-size: 25px;
  margin: 5px 0 0;
}
.product-price::before {
  content: '¥';
  font-size: 16px;
  vertical-align: middle;
}
.product-image-large {
  width: 100%;
  height: 300px;
  object-fit: contain;
  margin-bottom: 20px;
}

.el-dialog__body {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  font-size: 18px;
}
.el-dialog__body p {
  font-size: 16px;
  margin: 5px 0;
}

.quantity-control {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 10px;
}

.quantity-control button {
  padding: 5px 10px;
  border: 1px solid #ccc;
  background-color: #f0f0f0;
  cursor: pointer;
  border-radius: 5px;
  margin: 0 5px;
}

.quantity-input {
  width: 250px;
  padding: 8px;
  margin: 0 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
  text-align: center;
}

.button-group {
  display: flex;
  justify-content: center;
  width: 80%;
  margin: 20px auto;
  gap: 50px;
}

.add-to-cart-button,
.buy-now-button {
  flex: 1;
  padding: 15px 25px;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-to-cart-button {
  background-color: #2196f3;
}

.add-to-cart-button:hover {
  background-color: #1976d2;
}

.buy-now-button {
  background-color: #ff9800;
}

.buy-now-button:hover {
  background-color: #fb8c00;
}

.purchase-form {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.address-input {
  width: 80%;
  padding: 8px;
  margin-top: 5px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.confirm-button {
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.confirm-button:hover {
  background-color: #45a049;
}

.cart-content {
  max-height: 400px;
  overflow-y: auto;
}

.cart-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.cart-item-image {
  flex: 0 0 80px;
  height: 80px;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cart-item-image img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.cart-item-info {
  flex: 1;
  padding-left: 10px;
}

.cart-item-name {
  font-size: 16px;
  font-weight: bold;
  margin: 0 0 5px;
}

.cart-item-price {
  color: #ff5722;
  margin: 0 0 10px;
}

.quantity-control {
  display: flex;
  align-items: center;
}

.quantity-control button {
  padding: 5px 10px;
  border: 1px solid #ccc;
  background-color: #f0f0f0;
  cursor: pointer;
  border-radius: 5px;
  margin: 0 5px;
}

.quantity-input {
  width: 50px;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
  text-align: center;
}

.cart-footer {
  display: flex;
  justify-content: flex-end;
  padding: 10px;
  border-top: 1px solid #e0e0e0;
}

.checkout-button {
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.checkout-button:hover {
  background-color: #45a049;
}

.el-dialog {
  width: 600px !important;
  max-width: 90vw;
}

.my-options {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.address-list {
  max-height: 400px;
  overflow-y: auto;
}

.address-item {
  background-color: #ffffff; /* White background for each address item */
  border-radius: 5px;
  padding: 15px;
  margin-bottom: 10px;
  transition: background-color 0.3s;
}

.address-item:hover {
  background-color: #f0f0f0;
}

.address {
  font-weight: bold;
  font-size: 18px;
  color: #333;
  margin-bottom: 5px;
}

.address-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  color: #555;
}

.edit-button {
  padding: 5px 10px;
  background-color: #e0f7fa;
  color: #007bff;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.edit-button:hover {
  background-color: #b3e5fc;
}

.add-address {
  margin-top: 20px;
}

.add-address input {
  display: block;
  margin-bottom: 10px;
  padding: 5px;
  width: 100%;
  box-sizing: border-box;
}

/* 新增的编辑地址对话框样式 */
.edit-address-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.edit-address-form input {
  width: 80%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
}

.edit-address-form .confirm-button {
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.edit-address-form .confirm-button:hover {
  background-color: #45a049;
}

/* 新增地址对话框样式 */
.add-address-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.add-address-form input {
  width: 80%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
}

.add-address-form .confirm-button {
  padding: 10px 20px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-address-form .confirm-button:hover {
  background-color: #45a049;
}

/* 新增地址按钮样式 */
/* 新增地址按钮样式 */
.add-address-button {
  padding: 10px 20px;
  background-color: #e0f7fa;
  color: #007bff;
  border: none;
  border-radius: 9px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 18px; /* 放大按钮内文字和图标 */
  margin-right: 10px; /* 右侧外边距 */
}

.add-address-button:hover {
  background-color: #b3e5fc;
}

/* 删除地址按钮样式 */
.delete-button {
  padding: 10px 20px;
  background-color: #ff4d4f;
  color: white;
  border: none;
  border-radius: 9px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 18px; /* 放大按钮内文字和图标 */
}

.delete-button:hover {
  background-color: #d9363e;
}

/* 按钮组样式 */
.button-group {
  display: flex;
  justify-content: center;
  width: 100%;
  margin-top: 20px;
  gap: 10px; /* 按钮之间的间距 */
}

</style>

