import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const store = new Vuex.Store({
    state:{
        storeIsLogin:false,
        storeUserInfo: {},
    },
    mutations:{
        setLoginState(state, loginState){
            state.storeIsLogin = loginState;
            console.log("storeIsLogin: " + state.storeIsLogin);
        },

        setStoreUserInfo(state, userInfo){
            state.storeUserInfo = userInfo;
        },
    },
    actions:{

    },
    getters:{

    },
    modules:{

    }
});


export default store
