import Vue from 'vue'
import App from './App.vue'
import router from "@/util/router";
import store from "@/util/store";
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css"
import VueRouter from "vue-router";
import qs from "querystring";

Vue.config.productionTip = false;

Vue.use(ElementUI);
Vue.use(VueRouter);

Vue.prototype.qsParam = function (param) {
  return qs.stringify(param);
}

new Vue({
  router: router,
  store: store,
  render: h => h(App),
}).$mount('#app')
