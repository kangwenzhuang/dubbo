package com.kang.consumer;

import com.kang.dubbo.server.proxy.RpcClientProxy;
import com.kang.service.api.MemberService;

public class Consumer {
    public static void main(String[] args) {
        MemberService memberService = new RpcClientProxy().create(MemberService.class);
//        System.out.println("jhdodjoi");
//        String user = memberService.getUser(10L);
//
////        String user = "";
//        System.out.println("result:" + user);
        String he = memberService.test(12L);
        System.out.println("he:" + he);
    }
}
