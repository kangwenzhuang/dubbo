package com.kang.dubbo.server.loadBalance;

import java.util.List;

public interface LoadBalance<T> {
    /**
     * 实现Dubbo负载均衡器 负载均衡、随机、一致性hash
     *
     * @param repos
     * @return
     */
    String select(List<T> repos);
}
