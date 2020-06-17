import Vue from 'vue'
import VueRouter from 'vue-router'
//导入页面
import Index from "@/pages/Index";

//1.使用插件
Vue.use(VueRouter);

//2.创建VueRouter对象
const routes = [
  {
    path: '/',
    redirect: '/index'
  },
  {
    path: '/index',
    component: Index,
    meta: {
      title: "首页"
    }
  }
];
const router = new VueRouter({
  routes,
  mode: 'hash'
});

const VueRouterPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(to) {
  return VueRouterPush.call(this, to).catch(err => err)
};


router.beforeEach((to, from, next) => {
  // console.log("=============== beforeEach ==============");
  /* 路由发生变化修改页面title */
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next();

  if (to.matched.some(record => {
    return record.meta.requireAuth
  })) {
    // console.log("========== 判断前：登录状态" + store.state.storeIsLogin);
    if ("no" === localStorage.getItem("isLogin")) {
      next({
        path: '/login',
        query: {
          redirect: to.fullPath
        }
      })
    } else {
      next();
    }

  } else {
    next();
  }
});

//3.将路由对象传入到Vue实例
export default router

