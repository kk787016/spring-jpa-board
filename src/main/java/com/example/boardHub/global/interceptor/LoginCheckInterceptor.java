//package com.example.boardHub.global.interceptor;
//
//import com.example.boardHub.global.context.UserContext;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//@Slf4j
//public class LoginCheckInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        String userId = UserContext.getUserId(); // ThreadLocal에서 꺼냄
//        log.info("userId = " + userId);
//        if (userId == null) {
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("text/plain;charset=UTF-8");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("로그인이 필요합니다.");
//            return false;
//        }
//        return true;
//    }
//}
