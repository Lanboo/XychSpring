package com.xych.spring.v2.beans.factory.support;

public interface BeanDefinitionReader
{
    BeanDefinitionRegistry getRegistry();
    int loadBeanDefinitions(String location);
    int loadBeanDefinitions(String... locations);
}
