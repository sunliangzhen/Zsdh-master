package com.shuxiangbaima.task.ui.main.fragment;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2016/11/18.
 */

//动态代理类只能代理接口，代理类都需要实现InvocationHandler类，实现invoke方法。该invoke方法就是调用被代理接口的所有方法时需要调用的，该invoke方法返回的值是被代理接口的一个实现类
public class DynamicProxy implements InvocationHandler {

    private Object object;

    //绑定关系，也就是关联到哪个接口（与具体的实现类绑定）的哪些方法将被调用时，执行invoke方法。
    //Proxy.newProxyInstance的第三个参数是表明这些被拦截的方法执行时需要执行哪个InvocationHandler的invoke方法
    public Object bindRelation(Object object) {
        this.object = object;
        return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), this);
    }

    //拦截关联的这个实现类的方法被调用时将被执行
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Welcome");
        Object result = method.invoke(object, args);
        return result;
    }

}
