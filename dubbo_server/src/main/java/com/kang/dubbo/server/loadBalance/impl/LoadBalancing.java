package com.kang.dubbo.server.loadBalance.impl;

import com.kang.dubbo.server.loadBalance.LoadBalance;

import java.util.List;

public class LoadBalancing implements LoadBalance<String> {
    private int index = 0;

    @Override
    public synchronized String select(List<String> repos) {
        int size = repos.size();
        if (index >= size) {
            index = 0;
        }
        String value = repos.get(index++);
        return value;
    }
}
