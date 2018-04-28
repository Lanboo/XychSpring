package com.xych.spring.v2.context;

import com.xych.spring.v2.beans.factory.BeanFactory;
import com.xych.spring.v2.beans.factory.support.DefaultListableBeanFactory;

public abstract class AbstractApplicationContext implements ApplicationContext
{
    private String[] configLocations;
    private BeanFactory beanFactory = null;

    @Override
    public Object getBean(String beanName)
    {
        return beanFactory.getBean(beanName);
    }

    public void refresh()
    {
        obtainFreshBeanFactory();
    }

    private void obtainFreshBeanFactory()
    {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        /**
         * 模板方法，由子类去实现。
         * 此过程，在Spring中通常会调用AbstractXmlApplicationContext.loadBeanDefinitions，
         * 1、然后在xml配置文件中，如果配置了context:component-scan标签，
         * 2、那么在解析的时候，即org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.parseBeanDefinitions方法中，走第二个分值
         * 3、又因为是context标签，故在DefaultNamespaceHandlerResolver.handlerMappings找到org.springframework.context.config.ContextNamespaceHandler
         *      (handlerMappings加载的是Spring.jar/META-INF/spring.handlers中的配置，其配置的是xml命名空间所对应的解析类)
         * 4、接着org.springframework.context.annotation.ClassPathBeanDefinitionScanner.doScan方法，去扫描配置basePackage下的符合条件Bean
         * 
         * 在这个项目中，不使用xml配置，故直接扫描相当于org.springframework.web.context.support.AnnotationConfigWebApplicationContext.loadBeanDefinitions
         */
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }

    /**
     * 子类去实现怎么加载Bean
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory);

    public String[] getConfigLocations()
    {
        return configLocations;
    }

    public void setConfigLocations(String[] configLocations)
    {
        this.configLocations = configLocations;
    }

    public BeanFactory getBeanFactory()
    {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory)
    {
        this.beanFactory = beanFactory;
    }
}
