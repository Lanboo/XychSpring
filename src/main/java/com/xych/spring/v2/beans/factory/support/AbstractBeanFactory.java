package com.xych.spring.v2.beans.factory.support;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.xych.spring.v1.beans.BeanFactory;
import com.xych.spring.v2.beans.factory.config.BeanDefinition;

public abstract class AbstractBeanFactory implements BeanFactory
{
    /**
     * 父容器
     */
    private BeanFactory parentBeanFactory;
    /**
     * 存放单例bean的实例对象
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    /**
     * 某个Bean所依赖的Bean
     */
    private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);
    /**
     * 某个Bean被哪些Bean所依赖
     */
    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);

    @Override
    public Object getBean(String beanName)
    {
        return doGetBean(beanName);
    }

    protected Object doGetBean(String beanName)
    {
        Object bean;
        Object singletonObject = getSingleton(beanName);
        // 1、从单例缓存中获取
        if(singletonObject != null)
        {
            bean = singletonObject;
        }
        else
        {
            //2、从父容器中获取
            BeanFactory parentBeanFactory = getParentBeanFactory();
            if(parentBeanFactory != null && !containsBeanDefinition(beanName))
            {// 如果存在父级容器，且当前容器中不存在指定的Bean
                return parentBeanFactory.getBean(beanName);
            }
            BeanDefinition bd = getBeanDefinition(beanName);//在子类中，如果不存在就会抛出异常
            //3、首先创建该Bean所依赖的Bean
            //3.1、这里有个问题，小心循环依赖的问题
            String[] dependsOn = bd.getDependsOn();//获取该Beand所依赖的Bean的名称，
            if(dependsOn != null && dependsOn.length > 0)
            {
                // 首先创建
                for(String dep : dependsOn)
                {
                    //判断是否循环依赖
                    if(isDependent(beanName, dep))
                    {
                        throw new RuntimeException("Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                    }
                    //为当前beanName和dep维护dependentBeanMap和dependenciesForBeanMap
                    registerDependentBean(dep, beanName);
                    getBean(dep);
                }
            }
            bean = null;
        }
        return bean;
    }

    public void registerDependentBean(String dep, String beanName)
    {
    }

    protected boolean isDependent(String beanName, String dep)
    {
        return false;
    }

    /**
     * 
     * @param beanName
     * @return
     * @author 晓月残魂
     * @date 2018年5月6日下午7:09:33
     */
    public Object getSingleton(String beanName)
    {
        return this.singletonObjects.get(beanName);
    }

    /**
     * 获取指定的BeanDefinition
     */
    public abstract BeanDefinition getBeanDefinition(String beanName);

    /**
     * 判断IOC容器中是否存在指定的Bean
     */
    protected abstract boolean containsBeanDefinition(String beanName);

    public BeanFactory getParentBeanFactory()
    {
        return parentBeanFactory;
    }

    public void setParentBeanFactory(BeanFactory parentBeanFactory)
    {
        this.parentBeanFactory = parentBeanFactory;
    }
}
