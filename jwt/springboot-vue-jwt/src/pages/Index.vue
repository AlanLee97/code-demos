<template>
  <div>
    <img src="../assets/logo.png" alt="logo" width="100px" height="100px">

    <h1>JWT Demo</h1>

    <div class="al-flex-justify-space-around">
      <div class="al-box-pretty al-p-20px">
        <p>帐号：
          <el-input v-model="formData.username"/>
        </p>
        <p>密码：
          <el-input v-model="formData.password"/>
        </p>
        <el-button @click="login">登录</el-button>
      </div>

      <div>
        <el-button @click="getNetworkData">发起网络请求（需认证）</el-button>
      </div>
    </div>

    <div class="al-box-pretty">
      {{result}}
    </div>
  </div>
</template>

<script>
  import {request} from "@/util/network/request";
  import {ApiConst} from "@/util/network/api/api-const";

  export default {
    name: "Index",
    //组件
    components: {},
    //属性
    props: {},

    //数据
    data() {
      return {
        result: {},
        formData: {
          username: "",
          password: ""
        }
      }
    },

    //挂载完成时
    mounted() {
      //this.getNetworkData();
    },

    //方法
    methods: {
      getNetworkData() {
        request({
          url: ApiConst.USER_GET_ALL,
          data: this.qsParam(this.data),
          headers:{
            // "Authorization": "Bearer " + localStorage.getItem("token")
          }
        }).then(res => {
          console.log(res);
          this.result = res.data;
        }).catch(err => {
          console.log(err)
        })
      },

      login(){
        request({
          // url: ApiConst.USER_GET_ALL,
          url: "http://localhost:8080/user/login",
          method: 'post',
          data: this.qsParam(this.data)
        }).then(res => {
          console.log(res);
          this.result = res.data;
          localStorage.setItem("token", res.data.data);
        }).catch(err => {
          console.log(err)
        })
      }
    },

    //计算属性
    computed: {},

    //监听
    watch: {}
  }
</script>

<style scoped>

</style>
