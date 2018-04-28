package com.xych.spring.v2.beans.factory.support;

import com.xych.spring.v2.util.Assert;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader
{
    private final BeanDefinitionRegistry registry;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry)
    {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
        this.registry = registry;
    }

    @Override
    public final BeanDefinitionRegistry getRegistry()
    {
        return this.registry;
    }

    @Override
    public int loadBeanDefinitions(String... locations)
    {
        Assert.notNull(locations, "Location array must not be null");
        int counter = 0;
        for(String location : locations)
        {
            counter += loadBeanDefinitions(location);
        }
        return counter;
    }
}
