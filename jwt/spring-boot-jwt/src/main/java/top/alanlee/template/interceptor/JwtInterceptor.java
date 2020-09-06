package top.alanlee.template.interceptor;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.alanlee.template.config.JwtConfigProperties;
import top.alanlee.template.util.JWTUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtConfigProperties jwtConfigProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 这里是个坑，因为带请求带headers时，ajax会发送两次请求，
        // 第一次会发送OPTIONS请求，第二次才会发生get/post请求，所以要放行OPTIONS请求
        // 如果是OPTIONS请求，让其响应一个 200状态码，说明可以正常访问
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            // 放行OPTIONS请求
            return true;
        }

        // JwtConfigProperties通过@Autowired的值为null，所以需要通过以下方法获取bean
        if(jwtConfigProperties == null){
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            jwtConfigProperties = (JwtConfigProperties) factory.getBean("jwtConfigProperties");
        }
        boolean verify = false;
        String errMsg = "";
        // 获取header中的token
        String token = request.getHeader("Authorization");
        try {
            verify = JWTUtil.verify(token, jwtConfigProperties);
        } catch (Exception e) {
            errMsg = e.getMessage();
            e.printStackTrace();
        }
        // 验证token，如果验证失败就重定向到未登录页面
        if (!verify){
            errMsg = URLEncoder.encode(errMsg, "UTF-8");
            //这里是个坑，在重定向这里需要设置跨域，不然vue前端会报跨域问题
            response.setHeader("Access-Control-Allow-Origin", "*");
            // 重定向到未登录提示页面
            response.sendRedirect("/user/unlogin?msg=" + errMsg);
            return false;
        }
        return true;
    }
}