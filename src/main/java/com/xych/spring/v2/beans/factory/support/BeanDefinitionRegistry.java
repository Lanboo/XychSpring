package com.xych.spring.v2.beans.factory.support;

import com.xych.spring.v2.beans.factory.config.BeanDefinition;

/**
 * BeanDefinition相关操作
 */
public interface BeanDefinitionRegistry
{
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    void removeBeanDefinition(String beanName);

    BeanDefinition getBeanDefinition(String beanName);

    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();
}
