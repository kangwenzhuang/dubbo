package com.kang.dubbo.server.loadBalance.impl;

import com.kang.dubbo.server.loadBalance.LoadBalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance  implements LoadBalance<String> {
    @Override
    public String select(List<String> repos) {
        String value = repos.get(new Random().nextInt(repos.size()));
        return value;
    }

}
