package com.xych.spring.v2.context.annotation;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.xych.spring.v2.annotation.Component;
import com.xych.spring.v2.annotation.Lazy;
import com.xych.spring.v2.annotation.Scope;
import com.xych.spring.v2.beans.factory.config.BeanDefinition;
import com.xych.spring.v2.beans.factory.config.BeanDefinitionHolder;
import com.xych.spring.v2.beans.factory.support.BeanDefinitionRegistry;
import com.xych.spring.v2.beans.factory.support.GenericBeanDefinition;
import com.xych.spring.v2.util.StringUtils;

/**
 * 用于扫描指定路径下的Bean
 */
public class ClassPathBeanDefinitionScanner
{
    private final BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry)
    {
        this.registry = registry;
    }

    public int scan(String... basePackages)
    {
        int beanCountAtScanStart = this.registry.getBeanDefinitionCount();
        doScan(basePackages);
        return (this.registry.getBeanDefinitionCount() - beanCountAtScanStart);
    }

    /**
     * 开始扫描
     * 此过程不在仿照Spring
     */
    private void doScan(String[] basePackages)
    {
        for(String basePackage : basePackages)
        {
            Set<String> classNames = new HashSet<>();
            //获取类名，用于获取BeanDefinition
            doScan(basePackage, classNames);
            doLoad(classNames);
        }
    }

    /**
     * 利用反射机制，获取字节码，创建BeanDifinition
     */
    private void doLoad(Set<String> classNames)
    {
        if(classNames.isEmpty())
            return;
        String errClassName = null;
        try
        {
            for(String className : classNames)
            {
                errClassName = className;
                Class<?> clazz = Class.forName(className);
                BeanDefinitionHolder holder = doLoadHolder(clazz);
                if(holder != null)
                {
                    this.registry.registerBeanDefinition(holder.getBeanName(), holder.getBeanDefinition());
                }
            }
        }
        catch(ClassNotFoundException e)
        {
            throw new RuntimeException(errClassName + " 实例化出错");
        }
    }

    /**
     * 获取BeanDefinition
     */
    private BeanDefinitionHolder doLoadHolder(Class<?> clazz)
    {
        if(!clazz.isAnnotationPresent(Component.class))
        {
            return null;
        }
        Component component = clazz.getAnnotation(Component.class);
        String beanName = component.value();
        if("".equals(beanName.trim()))
        {
            //这里将类名首字母小写后的字符串作为key
            beanName = StringUtils.lowerFirstCase(clazz.getSimpleName());
        }
        BeanDefinition beanDefinition = doLoadBeanDifinition(clazz);
        return new BeanDefinitionHolder(beanName, beanDefinition);
    }

    private BeanDefinition doLoadBeanDifinition(Class<?> clazz)
    {
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(clazz.getName());
        if(clazz.isAnnotationPresent(Scope.class))
        {
            Scope scope = clazz.getAnnotation(Scope.class);
            String scopeStr = scope.value();
            if(!StringUtils.isTrimEmpty(scopeStr))
            {
                beanDefinition.setScope(scopeStr);
            }
        }
        if(clazz.isAnnotationPresent(Lazy.class))
        {
            Lazy lazy = clazz.getAnnotation(Lazy.class);
            beanDefinition.setLazyInit(lazy.value());
        }
        return beanDefinition;
    }

    /**
     * 递归调用，获取类名，用于获取BeanDefinition
     */
    private void doScan(String packageName, Set<String> classNames)
    {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File packageDir = new File(url.getFile());
        for(File file : packageDir.listFiles())
        {
            if(file.isDirectory())
            {
                doScan(packageName + "." + file.getName(), classNames);
            }
            else if(file.getName().endsWith(".class"))
            {
                String className = packageName + "." + file.getName().replace(".class", "");
                if(!classNames.contains(className))
                {
                    classNames.add(className);
                }
            }
        }
    }
}
