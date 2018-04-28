package com.xych.spring.v2.context;

import com.xych.spring.v2.beans.factory.properties.PropertiesBeanDefinitionReader;
import com.xych.spring.v2.beans.factory.support.DefaultListableBeanFactory;

public class DefaultApplicationContext extends AbstractApplicationContext
{
    public DefaultApplicationContext(String configLocation)
    {
        this(new String[] { configLocation }, true);
    }

    public DefaultApplicationContext(String[] configLocations, boolean refresh)
    {
        super();
        setConfigLocations(configLocations);
        if(refresh)
        {
            refresh();
        }
    }

    /**
     * <pre>
     * 模板方法，由子类去实现。
     * 此过程，在Spring中通常会调用AbstractXmlApplicationContext.loadBeanDefinitions，
     * 1、然后在xml配置文件中，如果配置了context:component-scan标签，
     * 2、那么在解析的时候，即org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader.parseBeanDefinitions方法中，走第二个分值
     * 3、又因为是context标签，故在DefaultNamespaceHandlerResolver.handlerMappings找到org.springframework.context.config.ContextNamespaceHandler
     *      (handlerMappings加载的是Spring.jar/META-INF/spring.handlers中的配置，其配置的是xml命名空间所对应的解析类)
     * 4、接着org.springframework.context.annotation.ClassPathBeanDefinitionScanner.doScan方法，去扫描配置basePackage下的符合条件Bean
     * </pre>
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
    {
        //在String中是XmlBeanDefinitionReader
        PropertiesBeanDefinitionReader beanDefinitionReader = new PropertiesBeanDefinitionReader(beanFactory);
        loadBeanDefinitions(beanDefinitionReader);
    }

    protected void loadBeanDefinitions(PropertiesBeanDefinitionReader reader)
    {
        String[] configLocations = getConfigLocations();
        if(configLocations != null)
        {
            reader.loadBeanDefinitions(configLocations);
        }
    }
}
