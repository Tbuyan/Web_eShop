<template>
  <div class="merchant-dashboard">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="200px">
        <el-menu default-active="1" class="el-menu-vertical-demo">
          <el-menu-item index="1" @click="showOrders">
            <template v-slot:title>
              <i class="el-icon-s-order"></i>
              <span>订单管理</span>
            </template>
          </el-menu-item>
          <el-menu-item index="2" @click="showProducts">
            <template v-slot:title>
              <i class="el-icon-goods"></i>
              <span>我的商品</span>
            </template>
          </el-menu-item>
          <el-menu-item index="3" @click="showVisitorRecords">
            <template v-slot:title>
              <i class="el-icon-user"></i>
              <span>访客记录</span>
            </template>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-main>
        <!-- 订单展示 -->
        <div v-if="activeView === 'orders'">
          <h2>订单管理</h2>
          <el-table :data="orderDetails" style="width: 100%">
            <el-table-column prop="oid" label="订单号" width="180"></el-table-column>
            <el-table-column prop="productName" label="商品名称" width="180"></el-table-column>
            <el-table-column prop="productQuantity" label="商品数量" width="180"></el-table-column>
            <el-table-column prop="customerName" label="客户姓名" width="180"></el-table-column>
            <el-table-column prop="customerEmail" label="客户邮箱" width="180"></el-table-column>
            <el-table-column prop="customerAddress" label="客户地址" width="180"></el-table-column>
            <el-table-column prop="odPMoney" label="订单金额" width="180"></el-table-column>
            <el-table-column prop="orderTime" label="订单时间" width="180"></el-table-column>
          </el-table>

          <!-- 折线图 -->
          <div ref="chartRef" style="width: 100%; height: 400px; margin-top: 20px;"></div>
        </div>

        <!-- 商品管理 -->
        <div v-if="activeView === 'products'">
          <h2>商品管理
            <el-button type="primary" icon="el-icon-plus" @click="openAddProductDialog" style="float: right;">
              新增商品
            </el-button>
          </h2>
          <!-- 商品管理内容 -->
          <el-row :gutter="20">
            <el-col :span="8" v-for="product in products" :key="product.productId">
              <el-card
                class="product-card"
                :body-style="{ padding: '20px' }"
                @dblclick="editProduct(product)"
                @click="selectProduct(product)"
                :class="{ selected: selectedProduct && selectedProduct.productId === product.productId }"
              >
                <img :src="formatPath(product.purl)" alt="商品图片" class="product-image">
                <div class="product-info">
                  <h3>{{ product.pname }}</h3>
                  <p>{{ product.pdes }}</p>
                  <p>价格: ¥{{ product.ppri }}</p>
                  <p>库存: {{ product.pcount }}</p>
                  <p>种类: {{ product.ptype }}</p>
                </div>
                <el-button
                  v-if="selectedProduct && selectedProduct.productId === product.productId"
                  type="danger"
                  @click.stop="deleteProduct(product)"
                  class="delete-button"
                >
                  删除
                </el-button>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 访客记录 -->
        <div v-if="activeView === 'visitorRecords'">
    <h2>访客记录</h2>
    <el-table :data="visitorRecords" style="width: 1200px" 
    :header-cell-style="{'text-align':'center'}"
    :cell-style="{'text-align':'center'}"
    >
      <el-table-column prop="productImage" label="商品图片" width="240" >
        <template v-slot="{ row }">
          <img :src="row.productImage" alt="商品图片" style="width: 100px; height: 100px; object-fit: cover;">
        </template>
      </el-table-column>
      <el-table-column prop="productName" label="商品名称" width="240" ></el-table-column>
      <el-table-column prop="productPrice" label="商品价格" width="240" ></el-table-column>
      <el-table-column prop="visitorId" label="访问用户" width="240" ></el-table-column>
      <el-table-column prop="visitTime" label="访问时间" width="240" ></el-table-column>
    </el-table>
  </div>
      </el-main>
    </el-container>

    <!-- 编辑对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑商品">
      <el-form :model="editProductForm">
        <el-form-item label="名称">
          <el-input v-model="editProductForm.pname"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="editProductForm.pdes"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="editProductForm.ppri" type="number"></el-input>
        </el-form-item>
        <el-form-item label="库存">
          <el-input v-model="editProductForm.pcount" type="number"></el-input>
        </el-form-item>
        <el-form-item label="种类">
          <el-input v-model="editProductForm.ptype"></el-input>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="updateProduct">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 新增商品对话框 -->
    <el-dialog v-model="addDialogVisible" title="新增商品">
      <el-form :model="newProductForm">
        <el-form-item label="图片链接">
          <el-input v-model="newProductForm.purl"></el-input>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="newProductForm.pname"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="newProductForm.pdes"></el-input>
        </el-form-item>
        <el-form-item label="价格">
          <el-input v-model="newProductForm.ppri" type="number"></el-input>
        </el-form-item>
        <el-form-item label="库存">
          <el-input v-model="newProductForm.pcount" type="number"></el-input>
        </el-form-item>
        <el-form-item label="种类">
          <el-select v-model="newProductForm.ptype" placeholder="请选择种类">
            <el-option label="家居日用" value="家居日用"></el-option>
            <el-option label="穿搭服饰" value="穿搭服饰"></el-option>
            <el-option label="米面粮油" value="米面粮油"></el-option>
            <el-option label="生鲜水果" value="生鲜水果"></el-option>
            <el-option label="科技数码" value="科技数码"></el-option>
            <el-option label="运动户外" value="运动户外"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <div class="dialog-footer">
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addProduct">添加</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue';
import axios from 'axios';
import * as echarts from 'echarts';

export default {
  name: 'MerchantDashboard',
  setup() {
    const activeView = ref('orders');
    const orderDetails = ref([]);
    const products = ref([]);
    const visitorRecords = ref([]);
    const selectedProduct = ref(null);
    const chartRef = ref(null);
    let chartInstance = null;
    const formatPath = (path) => path.replace(/\\/g, '/');

    const editDialogVisible = ref(false);
    const addDialogVisible = ref(false);
    const editProductForm = ref({
      pname: '',
      pdes: '',
      ppri: 0,
      pcount: 0,
      ptype: '',
      productId: null,
    });

    const newProductForm = ref({
      purl: '',
      pname: '',
      pdes: '',
      ppri: 0,
      pcount: 0,
      ptype: '',
    });

    const jwt = sessionStorage.getItem('jwt');
    if (jwt) {
      axios.defaults.headers.common['Authorization'] = `${jwt}`;
    }

    const showOrders = () => {
      activeView.value = 'orders';
      updateChart();
    };

    const showProducts = () => {
      activeView.value = 'products';
      fetchProducts();
    };

    const showVisitorRecords = () => {
      activeView.value = 'visitorRecords';
      fetchVisitorRecords();
    };

    const fetchOrderDetails = async () => {
      try {
        const uid = sessionStorage.getItem('uid');
        if (!uid) {
          console.error('用户ID未找到');
          return;
        }

        const response = await axios.get('/eShop/orders/getordersbysaler', {
          params: { uid },
        });

        if (response.status !== 200) {
          console.error('请求失败，状态码:', response.status);
          return;
        }

        const orderDetailsList = response.data.data;
        console.log('订单详情数据:', orderDetailsList);

        if (!Array.isArray(orderDetailsList)) {
          console.error('订单详情数据不是数组:', orderDetailsList);
          return;
        }

        const ordersPromises = orderDetailsList.map(async (orderDetail) => {
          try {
            const orderResponse = await axios.post('/eShop/orders/getorderbyorderdetail', {
              oid: orderDetail.oid,
            });

            if (orderResponse.status !== 200) {
              console.error('获取订单信息失败，状态码:', orderResponse.status);
              return null;
            }

            const order = orderResponse.data.data;
            const [name, email, address] = order.address.split(' ');
            const orderTime = new Date(parseInt(order.otime)).toISOString().split('T')[0];

            const productResponse = await axios.get('/eShop/product/getproductbyid', {
              params: { pid: orderDetail.pid },
            });

            if (productResponse.status !== 200) {
              console.error('获取商品信息失败，状态码:', productResponse.status);
              return null;
            }

            const productName = productResponse.data.data.pname;

            return {
              ...orderDetail,
              customerName: name,
              customerEmail: email,
              customerAddress: address,
              orderTime,
              productName,
              productQuantity: orderDetail.odPCount,
              odPMoney: parseFloat(orderDetail.odPMoney)
            };
          } catch (error) {
            console.error('获取订单信息时出错:', error);
            return null;
          }
        });

        orderDetails.value = (await Promise.all(ordersPromises)).filter(item => item !== null);
        console.log(orderDetails.value);

        updateChart();
      } catch (error) {
        console.error('获取订单详情失败:', error);
      }
    };

    const fetchProducts = async () => {
      try {
        const uid = sessionStorage.getItem('uid');
        if (!uid) {
          console.error('用户ID未找到');
          return;
        }

        const response = await axios.get('/eShop/product/getproductbysaler', {
          params: { uid },
        });

        if (response.status !== 200) {
          console.error('请求失败，状态码:', response.status);
          return;
        }

        products.value = response.data.data.map(product => ({
          ...product,
        }));
        console.log('商品数据:', products.value);
      } catch (error) {
        console.error('获取商品信息失败:', error);
      }
    };

    const fetchVisitorRecords = async () => {
      try {
        const uid = sessionStorage.getItem('uid');
        if (!uid) {
          console.error('用户ID未找到');
          return;
        }

        const response = await axios.get('/eShop/shopexp/getsalershopexp', {
          params: { uid },
        });

        if (response.status !== 200) {
          console.error('请求失败，状态码:', response.status);
          return;
        }

        const shopExpList = response.data.data;
        console.log('访客记录数据:', shopExpList);

        const recordsPromises = shopExpList.map(async (shopExp) => {
          try {
            const productResponse = await axios.get('/eShop/product/getproductbyid', {
              params: { pid: shopExp.pid },
            });

            if (productResponse.status !== 200) {
              console.error('获取商品信息失败，状态码:', productResponse.status);
              return null;
            }

            const product = productResponse.data.data;
            const visitTime = new Date(parseInt(shopExp.seTime)).toLocaleString();

            return {
              productImage: formatPath(product.purl),
              productName: product.pname,
              productPrice: product.ppri,
              visitorId: shopExp.uid0,
              visitTime,
            };
          } catch (error) {
            console.error('获取商品信息时出错:', error);
            return null;
          }
        });

        visitorRecords.value = (await Promise.all(recordsPromises)).filter(item => item !== null);
        console.log(visitorRecords.value);
      } catch (error) {
        console.error('获取访客记录失败:', error);
      }
    };

    const updateChart = async () => {
      await nextTick();
      if (chartInstance) {
        chartInstance.dispose(); // 销毁现有实例
      }
      chartInstance = echarts.init(chartRef.value); // 重新初始化

      const today = new Date();
      const dates = Array.from({ length: 7 }, (_, i) => {
        const date = new Date(today);
        date.setDate(today.getDate() - i);
        return date.toISOString().split('T')[0];
      }).reverse();

      const orderAmounts = dates.map(date => {
        const dailyOrders = orderDetails.value.filter(order => order.orderTime.startsWith(date));
        console.log(`Date: ${date}, Orders:`, dailyOrders);
        return dailyOrders.reduce((sum, order) => sum + order.odPMoney, 0);
      });

      const option = {
        title: {
          text: '订单金额统计',
        },
        tooltip: {
          trigger: 'axis',
        },
        xAxis: {
          type: 'category',
          data: dates,
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: orderAmounts,
            type: 'line',
          },
        ],
      };

      chartInstance.setOption(option);
    };

    const selectProduct = (product) => {
      selectedProduct.value = product;
    };

    const editProduct = (product) => {
      editProductForm.value = { ...product };
      editDialogVisible.value = true;
    };

    const deleteProduct = async (product) => {
      try {
        const response = await axios.post('/eShop/product/deleteproduct', { pid: product.productId });
        if (response.status === 200) {
          console.log('商品删除成功');
          fetchProducts(); // 更新商品列表
          selectedProduct.value = null; // 清除选中状态
        } else {
          console.error('商品删除失败，状态码:', response.status);
        }
      } catch (error) {
        console.error('删除商品时出错:', error);
      }
    };

    const updateProduct = async () => {
      try {
        const response = await axios.post('/eShop/product/updateproduct', editProductForm.value);
        if (response.status === 200) {
          console.log('商品更新成功');
          editDialogVisible.value = false;
          fetchProducts(); // 更新商品列表
        } else {
          console.error('商品更新失败，状态码:', response.status);
        }
      } catch (error) {
        console.error('更新商品时出错:', error);
      }
    };

    const openAddProductDialog = () => {
      newProductForm.value = {
        purl: '',
        pname: '',
        pdes: '',
        ppri: 0,
        pcount: 0,
        ptype: '',
      };
      addDialogVisible.value = true;
    };

    const addProduct = async () => {
      try {
        const uid = sessionStorage.getItem('uid');
        if (!uid) {
          console.error('用户ID未找到');
          return;
        }

        const response = await axios.post('/eShop/product/addproduct', {
          ...newProductForm.value,
          uid1:uid,
        });

        if (response.status === 200) {
          console.log('商品添加成功');
          addDialogVisible.value = false;
          fetchProducts(); // 更新商品列表
        } else {
          console.error('商品添加失败，状态码:', response.status);
        }
      } catch (error) {
        console.error('添加商品时出错:', error);
      }
    };

    onMounted(() => {
      if (activeView.value === 'orders') {
        fetchOrderDetails();
      }
    });

    return {
      activeView,
      orderDetails,
      products,
      visitorRecords,
      selectedProduct,
      showOrders,
      showProducts,
      showVisitorRecords,
      chartRef,
      chartInstance,
      formatPath,
      editDialogVisible,
      addDialogVisible,
      editProductForm,
      newProductForm,
      selectProduct,
      editProduct,
      deleteProduct,
      updateProduct,
      openAddProductDialog,
      addProduct
    };
  },
};
</script>

<style scoped>
.merchant-dashboard {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.el-container {
  height: 100%;
}

.el-aside {
  background-color: #f5f5f5;
}

.el-main {
  padding: 20px;
}

.product-card {
  transition: transform 0.3s, box-shadow 0.3s;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  height: 400px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  position: relative;
}

.product-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.product-card.selected {
  border: 2px solid #409eff;
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 14px;
  text-align: center;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-info h3 {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-info p {
  font-size: 14px;
  color: #666;
  margin: 5px 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.delete-button {
  position: absolute;
  top: 10px;
  right: 10px;
}

.visitor-records img {
  width: 100px;
  height: 100px;
  object-fit: cover;
}
</style>
