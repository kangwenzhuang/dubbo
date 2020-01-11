package com.kang.dubbo.server.req;

import java.io.Serializable;

public class RpcRequest implements Serializable {
    private static final long SerialVersionUID = 1L;
    /**
     * 类的className
     */
    private String className;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数类型
     */
    Class<?> parameterTypes[];
    /**
     * 参数value
     */
    Object paramsValue[];

    public RpcRequest(String className, String methodName, Class<?>[] parameterTypes, Object[] paramsValue) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.paramsValue = paramsValue;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getParamsValue() {
        return paramsValue;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public void setParamsValue(Object[] paramsValue) {
        this.paramsValue = paramsValue;
    }

}