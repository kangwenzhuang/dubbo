package com.kang.consumer;

import com.kang.dubbo.server.proxy.RpcClientProxy;
import com.kang.service.api.MemberService;

public class Consumer {
    public static void main(String[] args) {
        MemberService memberService=new RpcClientProxy().create(MemberService.class);
        MemberService memberService=new RpcClientProxy().create(MemberService.class);
        String user=memberService.getUser(10L);
        System.out.println("result:"+user);
    }
}
