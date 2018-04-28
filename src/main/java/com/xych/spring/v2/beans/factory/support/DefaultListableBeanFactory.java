package com.xych.spring.v2.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xych.spring.v2.beans.factory.BeanFactory;
import com.xych.spring.v2.beans.factory.config.BeanDefinition;
import com.xych.spring.v2.util.StringUtils;

public class DefaultListableBeanFactory implements BeanFactory, BeanDefinitionRegistry
{
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    @Override
    public Object getBean(String beanName)
    {
        return null;
    }

    /**
     * 在Spring中，此方法会做N多检查，才会放入beanDefinitionMap
     */
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
    {
        if(!StringUtils.isTrimEmpty(beanName) && beanDefinition != null)
        {
            if(beanDefinition instanceof GenericBeanDefinition)
            {
                this.beanDefinitionMap.put(beanName, beanDefinition);
            }
        }
    }

    @Override
    public void removeBeanDefinition(String beanName)
    {
        this.beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName)
    {
        return this.beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName)
    {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public int getBeanDefinitionCount()
    {
        return this.beanDefinitionMap.size();
    }
}
