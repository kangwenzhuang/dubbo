package com.kang.service.impl;

import com.kang.dubbo.server.rpc.KangRpcServer;

public class MemberProducer {
    public static void main(String[] args) {
        new KangRpcServer("127.0.0.1",8080).start(new MemberServiceImpl());
    }
}
