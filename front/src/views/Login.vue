<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="login-title">登录</h2>
      <el-form :model="loginForm" @submit.prevent="handleLogin">
        <el-form-item>
          <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="options">
        <el-button class="option-button" type="text" @click="showRegister = true">注册</el-button>
        <el-button class="option-button" type="text" @click="showForgotPassword = true">忘记密码</el-button>
      </div>
    </el-card>

    <!-- 注册对话框 -->
    <el-dialog v-model="showRegister" title="注册">
      <el-form :model="registerForm">
        <el-form-item>
          <el-input v-model="registerForm.u_name" placeholder="用户名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="密码"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input v-model="registerForm.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="registerForm.utype" placeholder="选择用户类型">
            <el-option label="商家" :value="1"></el-option>
            <el-option label="顾客" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister">提交注册</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 忘记密码对话框 -->
    <el-dialog v-model="showForgotPassword" title="忘记密码">
      <el-form v-if="!isCodeVerified" :model="forgotPasswordForm">
        <el-form-item>
          <el-input v-model="forgotPasswordForm.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="sendVerificationCode">获取验证码</el-button>
        </el-form-item>
        <el-form-item>
          <el-input v-model="forgotPasswordForm.code" placeholder="验证码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="verifyCode">验证验证码</el-button>
        </el-form-item>
      </el-form>
      <el-form v-else :model="resetForm">
        <el-form-item>
          <el-input v-model="resetForm.newUsername" placeholder="新用户名"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="resetForm.newPassword"
            type="password"
            placeholder="新密码"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="resetPassword">重置密码</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  name: 'LoginPage',
  setup() {
    const router = useRouter();
    const loginForm = ref({
      username: '',
      password: '',
    });

    const registerForm = ref({
      u_name: '',
      password: '',
      email: '',
      utype: null,
    });

    const forgotPasswordForm = ref({
      email: '',
      code: '',
    });

    const resetForm = ref({
      newUsername: '',
      newPassword: '',
    });

    const showRegister = ref(false);
    const showForgotPassword = ref(false);
    const isCodeVerified = ref(false);

    const handleLogin = async () => {
      try {
      console.log('true')
        const response = await axios.post('/eShop/user/login', {
          uname: loginForm.value.username,
          password: loginForm.value.password,
        });

        const { code, msg, data } = response.data;

        if (code === 200 && msg === '成功') {
          const match = data.match(/^(\d)(\d*)(.*)$/);

          
            const utype = match[1]; // 第一个数字
            const uid = match[2];   // 后续的数字序列
            const jwt = match[3];   // 剩余的字符串
            
          // 将 JWT 和用户名存储到 localStorage 中
          sessionStorage.setItem('jwt', jwt);
          sessionStorage.setItem('uid', uid);
          sessionStorage.setItem('username', loginForm.value.username);

          // 设置 Axios 默认请求头
          axios.defaults.headers.common['Authorization'] = `Bearer ${jwt}`;

          if (utype === '0') {
            ElMessage.success('登录成功，欢迎来到ESHOP，祝您购物愉快');
            router.push('/eShopF/customer-dashboard');
          } else if (utype === '1') {
            ElMessage.success('登录成功，欢迎来到ESHOP，祝您生意红火');
            router.push('/eShopF/merchant-dashboard');
          }
        } else {
          ElMessage.error(data || '登录失败');
        }
      } catch (error) {
        ElMessage.error('请求失败，请稍后重试');
      }
    };

    const handleRegister = async () => {
      try {
        const response = await axios.post('/eShop/user/register', {
          uname: registerForm.value.u_name,
          password: registerForm.value.password,
          email: registerForm.value.email,
          utype: registerForm.value.utype,
        });

        const { code, msg } = response.data;

        if (code === 200 && msg === '成功') {
          ElMessage.success('注册成功');
          showRegister.value = false;
        } else {
          ElMessage.error(msg || '注册失败');
        }
      } catch (error) {
        ElMessage.error('请求失败，请稍后重试');
      }
    };

    const sendVerificationCode = async () => {
      try {
        await axios.get(`/eShop/user/sendcode/${forgotPasswordForm.value.email}`);
        ElMessage.success('验证码已发送，请检查您的邮箱');
      } catch (error) {
        ElMessage.error('发送验证码失败，请稍后重试');
      }
    };

    const verifyCode = async () => {
      try {
        const response = await axios.post('/eShop/user/checkcode', {
          email: forgotPasswordForm.value.email,
          code: forgotPasswordForm.value.code,
        });

        const { data } = response.data;

        if (data == true) {
          ElMessage.success('验证码验证成功');
          isCodeVerified.value = true;
        } else {
          ElMessage.error('验证码错误，请重新获取');
        }
      } catch (error) {
        ElMessage.error('验证失败，请稍后重试');
      }
    };

    const resetPassword = async () => {
      try {
        const response = await axios.post('/eShop/user/reset', {
          email: forgotPasswordForm.value.email,
          uname: resetForm.value.newUsername,
          password: resetForm.value.newPassword,
        });

        const { code, msg } = response.data;

        if (code === 200 && msg === '成功') {
          ElMessage.success('密码重置成功');
          showForgotPassword.value = false;
          isCodeVerified.value = false;
        } else {
          ElMessage.error(msg || '重置失败');
        }
      } catch (error) {
        ElMessage.error('请求失败，请稍后重试');
      }
    };

    const handleLogout = () => {
      // 清除 localStorage 中的 JWT 和用户名
      sessionStorage.removeItem('jwt');
      sessionStorage.removeItem('username');
      sessionStorage.removeItem('uid');

      // 清除 axios 默认请求头
      delete axios.defaults.headers.common['Authorization'];

      // 跳转回登录界面
      router.push('/login');
    };

    onMounted(() => {
      // 在组件挂载时设置请求头
      const jwt = sessionStorage.getItem('jwt');
      if (jwt) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${jwt}`;
      }
    });

    return {
      loginForm,
      registerForm,
      forgotPasswordForm,
      resetForm,
      showRegister,
      showForgotPassword,
      isCodeVerified,
      handleLogin,
      handleRegister,
      sendVerificationCode,
      verifyCode,
      resetPassword,
      handleLogout,
    };
  },
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  margin: 0;
  background-image: url('@/assets/在线购物平台登录界面设计 (4).png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.login-card {
  width: 350px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.9);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  position: relative;
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.login-title {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
  font-weight: bold;
}

.el-input {
  border-radius: 5px;
}

.el-button {
  width: 100%;
  border-radius: 5px;
  background-color: #66a6ff;
  color: #fff;
  font-weight: bold;
}

.el-button:hover {
  background-color: #5591e6;
}

.options {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.option-button {
  width: auto;
  text-align: center;
  color: #333;
  background-color: transparent;
  padding: 0;
}
</style>
