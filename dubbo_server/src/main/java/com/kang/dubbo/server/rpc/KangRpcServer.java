package com.kang.dubbo.server.rpc;

import com.kang.dubbo.RpcAnnotation;
import com.kang.dubbo.server.handler.DubboServerHandler;
import com.kang.dubbo.server.marshalling.MarshallingCodeCFactory;
import com.kang.dubbo.server.register.ServiceRegisteration;
import com.kang.dubbo.server.register.ServiceRegisterationImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;

public class KangRpcServer {

    private ServiceRegisteration serviceRegisteration;
    private String host;
    private int port;
    private HashMap<String, Object> serviceBean = new HashMap<>();

    public KangRpcServer(String host, int port) {
        this.host = host;
        this.port = port;
        serviceRegisteration = new ServiceRegisterationImpl();
    }

    public void start(Object object) {
        bind(object);
        nettyStart();
    }

    public void bind(Object object) {
        RpcAnnotation rpcAnnotation = object.getClass().getDeclaredAnnotation(RpcAnnotation.class);
        if (rpcAnnotation == null) {
            return;
        }
        String serviceName = rpcAnnotation.value().getName();
        String serviceAddress = "kangkang://" + host + ":" + port;
        serviceRegisteration.register(serviceName, serviceAddress);
        serviceBean.put(serviceName, object);
    }

    private void nettyStart() {
        // 用于接受客户端连接的请求 （并没有处理请求）
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 用于处理客户端连接的读写操作
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        // 用于创建我们的ServerBootstrap
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                        socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        socketChannel.pipeline().addLast(new DubboServerHandler(serviceBean));
                    }
                });
        //  绑定我们的端口号码
        try {
            // 绑定端口号，同步等待成功
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("会员服务启动成功:" + port);
            // 等待服务器监听端口
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // 优雅的关闭连接
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
