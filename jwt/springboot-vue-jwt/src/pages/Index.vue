<template>
  <div>
    <img src="../assets/logo.png" alt="logo" width="100px" height="100px">

    <h1>JWT Demo</h1>

    <div class="al-flex-justify-space-around">
      <div class="al-p-20px al-box-container al-box-pretty" style="width: 500px; height: 300px">
        <p style="word-break: break-all; padding: 0 10px; height: 250px" >{{result === null ? "无结果" : result}}</p>

        <el-button @click="getNetworkData">发起网络请求（需认证）</el-button>
      </div>

      <div class="al-p-20px al-box-container al-box-pretty" style="width: 500px; height: 300px">
        <h3>登录</h3>
        <div class="al-flex-container al-flex-direction-col">
          <div class="al-flex-container al-flex-container-center-v">
            <div style="width: 60px">帐号：</div>
            <el-input v-model="formData.username"/>
          </div>
          <div style="height: 20px"></div>
          <div class="al-flex-container al-flex-container-center-v">
            <div style="width: 60px">密码：</div>
            <el-input v-model="formData.password"/>
          </div>
        </div>
        <el-button @click="login" type="primary">登录</el-button>
      </div>
    </div>

  </div>
</template>

<script>
  import {request} from "@/util/network/request";
  import qs from "qs";

  export default {
    name: "Index",
    //组件
    components: {},
    //属性
    props: {},

    //数据
    data() {
      return {
        result: null,
        formData: {
          username: "",
          password: ""
        }
      }
    },

    //挂载完成时
    mounted() {
      //this.getNetworkData();
      localStorage.setItem("token", "")
    },

    //方法
    methods: {
      getNetworkData() {
        request({
          url: "http://localhost:8080/user/get/all",
          data: qs.stringify(this.data),
          headers:{
            "Authorization": localStorage.getItem("token")
          }
        }).then(res => {
          console.log(res);
          this.result = res.data;
        }).catch(err => {
          console.log(err)
        })
      },

      login(){
        let data = {
          username: this.formData.username,
          password: this.formData.password,
        }
        console.log(data);

        request({
          url: "http://localhost:8080/user/login",
          method: 'post',
          data: qs.stringify(data)
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
