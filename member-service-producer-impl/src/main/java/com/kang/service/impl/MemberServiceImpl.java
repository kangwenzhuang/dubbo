package com.kang.service.impl;

import com.kang.dubbo.RpcAnnotation;
import com.kang.service.api.MemberService;


@RpcAnnotation(MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Override
    public String getUser(Long userId) {
        return "hello kangkang!!!";
    }
}
