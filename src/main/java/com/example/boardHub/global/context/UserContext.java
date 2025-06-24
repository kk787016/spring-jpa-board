//package com.example.boardHub.global.context;
//
//import com.example.boardHub.user.model.User;
//
//public class UserContext {
//    private static ThreadLocal<String> userThreadLocal = new ThreadLocal<>();
//
//    public static void setUserId(String userId) {
//        userThreadLocal.set(userId);
//    }
//
//    public static String getUserId() {
//        return userThreadLocal.get();
//    }
//    public static void clear(){
//        userThreadLocal.remove();
//    }
//}
