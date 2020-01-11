package com.kang.dubbo.server.discover;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class ServiceDiscoverImpl implements ServiceDiscover {
    /**
     * zk连接地址
     */
    private final String zkServers = "127.0.0.1";
    /**
     * 会话时间
     */
    private final int connectionTimeout = 5000;
    /***
     * zkClient
     */
    private ZkClient zkClient;


    private String rootNamePath = "/kangkang_rpc";

    public ServiceDiscoverImpl() {
        zkClient = new ZkClient(zkServers, connectionTimeout);
    }

    @Override
    public List<String> getDiscover(String serviceName) {
        String serviceNameNodePath = rootNamePath + "/" + serviceName + "/providers";
        List<String> children = zkClient.getChildren(serviceNameNodePath);
        return children;
    }
}
