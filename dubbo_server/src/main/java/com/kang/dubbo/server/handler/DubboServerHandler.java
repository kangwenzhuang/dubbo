package com.kang.dubbo.server.handler;

import com.kang.dubbo.server.req.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DubboServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 存放注册的bean对象
     */
    private Map<String, Object> serviceBean = new HashMap<>();

    public DubboServerHandler(Map<String, Object> serviceBean) {
        this.serviceBean = serviceBean;

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest) msg;
        String className = rpcRequest.getClassName();
        Object objectImpl = serviceBean.get(className);
        if (objectImpl == null) {
            return;
        }
        Method method = objectImpl.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        // 使用反射技术执行我们的方法
        Object result = method.invoke(objectImpl, rpcRequest.getParamsValue());
        // 响应给客户端
        ctx.writeAndFlush(result);
    }
}