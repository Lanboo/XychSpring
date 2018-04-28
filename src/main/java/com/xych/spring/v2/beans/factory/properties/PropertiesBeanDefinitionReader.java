package com.xych.spring.v2.beans.factory.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.xych.spring.v2.beans.factory.support.AbstractBeanDefinitionReader;
import com.xych.spring.v2.beans.factory.support.BeanDefinitionRegistry;
import com.xych.spring.v2.context.annotation.ClassPathBeanDefinitionScanner;
import com.xych.spring.v2.util.Assert;

/**
 * 这个类在Spring对应是XmlBeanDefinitionReader
 * @Description
 * @author 晓月残魂
 * @CreateDate 2018年4月28日下午6:03:28
 */
public class PropertiesBeanDefinitionReader extends AbstractBeanDefinitionReader
{
    public static final String COMPONENT_SCAN_BASE_PACKAGE = "component-scan-base-package";

    public PropertiesBeanDefinitionReader(BeanDefinitionRegistry registry)
    {
        super(registry);
    }

    /**
     * 在Spring的XmlBeanDefinitionReader.loadBeanDefinitions(Resource)中开始处理文件了，读取字节流，解析dom节点
     */
    @Override
    public int loadBeanDefinitions(String location)
    {
        InputStream inputStream = null;
        try
        {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:", ""));
            Assert.notNull(inputStream, location + " not find");
            return doLoadBeanDefinitions(inputStream);
        }
        finally
        {
            if(inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch(IOException e)
                {
                    throw new RuntimeException("InputStream close error", e);
                }
            }
        }
    }

    private int doLoadBeanDefinitions(InputStream inputStream)
    {
        try
        {
            Properties contextConfig = new Properties();
            contextConfig.load(inputStream);
            String basePackage = contextConfig.getProperty(COMPONENT_SCAN_BASE_PACKAGE);
            Assert.hasTrimLength(basePackage, "basePackage must not empty");
            /**
             * Spring中，此步骤：将字节流读取成Doc文档，调用XmlBeanDefinitionReader.registerBeanDefinitions处理doc
             * 
             * 这里，应为是properties文件直接配置了basePackage，故此处直接扫描该路劲下的类。
             */
            return registerBeanDefinitions(basePackage);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Properties load error", e);
        }
    }

    /**
     * Spring中，此步骤：将字节流读取成Doc文档，调用XmlBeanDefinitionReader.registerBeanDefinitions处理doc
     * 
     * 这里，应为是properties文件直接配置了basePackage，故此处直接扫描该路劲下的类。
     * 
     */
    private int registerBeanDefinitions(String basePackage)
    {
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        return scanner.scan(basePackage);
    }
}
