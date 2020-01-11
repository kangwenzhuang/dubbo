package com.kang.dubbo.server.proxy;

import com.kang.dubbo.server.discover.ServiceDiscover;
import com.kang.dubbo.server.discover.ServiceDiscoverImpl;
import com.kang.dubbo.server.handler.DubboClientHandler;
import com.kang.dubbo.server.loadBalance.LoadBalance;
import com.kang.dubbo.server.loadBalance.impl.LoadBalancing;
import com.kang.dubbo.server.marshalling.MarshallingCodeCFactory;
import com.kang.dubbo.server.req.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.List;

public class RpcClientProxy {
    private ServiceDiscover serviceDiscover;

    public RpcClientProxy() {
        serviceDiscover = new ServiceDiscoverImpl();
    }

    public <T> T create(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 从zk上获取注册地址
                String serviceName = interfaceClass.getName();
                List<String> discover = serviceDiscover.getDiscover(serviceName);
                // 使用默认负载均衡器
                LoadBalance<String> loadBalancing = new LoadBalancing();
                // mayikt://192.168.212.1:8080 获取ip和端口号
                String selectAddres = URLDecoder.decode(loadBalancing.select(discover));
                System.out.println("负载均衡：" + selectAddres);
                String[] split = selectAddres.split(":");
                String host = split[1].replace("//", "");
                String port = split[2].replace("/", "");
                // 建立Netty连接 发送数据
                RpcRequest rpcRequest = new RpcRequest(serviceName, method.getName(), method.getParameterTypes(), args);
//                return sendMsg(host, Integer.parseInt(port), rpcRequest);

                DubboClientHandler dubboClientHandler = new DubboClientHandler();
                //创建nioEventLoopGroup
                NioEventLoopGroup group = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(group).channel(NioSocketChannel.class)
                        .remoteAddress(new InetSocketAddress(host, Integer.parseInt(port)))
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                                ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                                ch.pipeline().addLast(dubboClientHandler);
                            }
                        });
                try {
                    // 发起同步连接
                    ChannelFuture sync = bootstrap.connect().sync();
                    sync.channel().writeAndFlush(rpcRequest);
                    sync.channel().closeFuture().sync();
                } catch (Exception e) {

                } finally {
                    group.shutdownGracefully();
                }
                return dubboClientHandler.getResponse();

            }
        });
    }


}

