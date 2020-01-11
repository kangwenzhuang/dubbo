package com.kang.dubbo.server.register;

import org.I0Itec.zkclient.ZkClient;

import java.net.URLEncoder;

public class ServiceRegisterationImpl implements ServiceRegisteration {

    private final String zkServer = "127.0.0.1";
    private final int connectionTimeout = 5000;
    private ZkClient zkClient;

    private String rootNamePath = "/kangkang_rpc";

    public ServiceRegisterationImpl() {
        zkClient = new ZkClient(zkServer, connectionTimeout);
    }

    @Override
    public void register(String serviceName, String serviceAddress) {
        if (!zkClient.exists(rootNamePath)) {
            zkClient.createPersistent(rootNamePath);
        }
        String serviceNodePath = rootNamePath + "/" + serviceName;
        if (!zkClient.exists(serviceNodePath)) {
            zkClient.createPersistent(serviceNodePath);
        }
        String providerNodePath = serviceNodePath + "/" + "providers";
        if (!zkClient.exists(providerNodePath)) {
            zkClient.createPersistent(providerNodePath);
        }
        String serviceAddressNodePath = providerNodePath + "/" + URLEncoder.encode(serviceAddress);
        if (!zkClient.exists(serviceAddressNodePath)) {
            zkClient.delete(serviceAddressNodePath);
        }
        zkClient.createPersistent(serviceAddressNodePath);
    }

    public static void main(String[] args) {
        ServiceRegisterationImpl s = new ServiceRegisterationImpl();
        String rpc = "kangkang://127.0.0.1:8080/com.kang.dubbo.getUser";
        s.register("com.kang.UserService", rpc);
    }
}
