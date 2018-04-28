package com.xych.spring.v2.beans.factory.config;

public class BeanDefinitionHolder
{
    private final BeanDefinition beanDefinition;
    private final String beanName;

    public BeanDefinitionHolder(String beanName, BeanDefinition beanDefinition)
    {
        super();
        this.beanName = beanName;
        this.beanDefinition = beanDefinition;
    }

    public BeanDefinition getBeanDefinition()
    {
        return beanDefinition;
    }

    public String getBeanName()
    {
        return beanName;
    }
}
