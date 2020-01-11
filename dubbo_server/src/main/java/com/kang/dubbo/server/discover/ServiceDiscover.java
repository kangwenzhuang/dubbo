package com.kang.dubbo.server.discover;

import java.util.List;

public interface ServiceDiscover {

    /**
     * 根据服务名称查找对应服务列表
     *
     * @param serviceName
     * @return
     */
    List<String> getDiscover(String serviceName);

}
