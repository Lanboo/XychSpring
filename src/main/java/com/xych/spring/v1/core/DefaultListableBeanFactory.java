package com.xych.spring.v1.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.xych.spring.v1.annotation.Autowired;
import com.xych.spring.v1.annotation.Component;
import com.xych.spring.v1.annotation.Qualifier;
import com.xych.spring.v1.beans.BeanFactory;

public class DefaultListableBeanFactory implements BeanFactory
{
    /**
     * <pre>
     * 获取配置文件属性，Spring中是xml，这里用properties文件
     * 只配置component-scan-base-package属性，充当<context:component-scan base-package="" />
     * </pre>
     */
    private Properties contextConfig = new Properties();
    /**
     * 记录component-scan-base-package路径下类的类名，用于反射
     */
    private List<String> classNames = new ArrayList<String>();
    /**
     * 用来存放Bean。
     * 注意：在Spring中，这里不是存放实例，而是存放的BeanDefinition
     */
    private final Map<String, Object> benMap = new ConcurrentHashMap<String, Object>();

    public DefaultListableBeanFactory(String location)
    {
        super();
        init(location);
    }

    private void init(String location)
    {
        //定位
        doLoadConfig(location);
        //载入、加载
        doScanner(contextConfig.getProperty("component-scan-base-package"));
        //注册
        doRegistry();
        //依赖注入
        //在Spring中是通过调用getBean方法才出发依赖注入的
        doAutowired();
    }

    /**
     * 依赖注入
     * 在Spring中是通过调用getBean方法才出发依赖注入的
     */
    private void doAutowired()
    {
        if(this.benMap.isEmpty())
            return;
        try
        {
            for(Map.Entry<String, Object> entry : this.benMap.entrySet())
            {
                Object bean = entry.getValue();
                Field[] fields = bean.getClass().getDeclaredFields();
                for(Field field : fields)
                {
                    if(!field.isAnnotationPresent(Autowired.class))
                    {
                        continue;
                    }
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    boolean required = autowired.required();
                    String beanName = "";
                    if(field.isAnnotationPresent(Qualifier.class))
                    {
                        Qualifier qualifier = field.getAnnotation(Qualifier.class);
                        beanName = qualifier.value();
                    }
                    if("".equals(beanName.trim()))
                    {
                        beanName = this.lowerFirstCase(field.getType().getSimpleName());
                    }
                    Object objValue = this.benMap.get(beanName);
                    if(objValue == null)
                    {
                        if(required)
                        {
                            throw new RuntimeException("no instance:" + bean.getClass().getName() + "#" + field.getName() + " Autowired is required.");
                        }
                        else
                        {
                            continue;
                        }
                    }
                    field.setAccessible(true);
                    field.set(bean, objValue);
                }
            }
        }
        catch(IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 在上一个阶段中，获取了扫描路径下的所有类的名字
     * 这一阶段，加载这些类，并判断是否是一个Bean
     */
    private void doRegistry()
    {
        if(this.classNames.isEmpty())
            return;
        try
        {
            for(String className : this.classNames)
            {
                Class<?> clazz = Class.forName(className);
                // 在Spring中用的多个子方法来处理的
                // 这里简化了。注解只有一个 com.xych.spring.v1.annotation.Component
                if(clazz.isAnnotationPresent(Component.class))
                {
                    Component component = clazz.getAnnotation(Component.class);
                    String beanName = component.value();
                    if("".equals(beanName.trim()))
                    {
                        //这里将类名首字母小写后的字符串作为key
                        beanName = this.lowerFirstCase(clazz.getSimpleName());
                    }
                    //这里，在Spring中，这里不是存放实例，而是存放的BeanDefinition
                    //Bean的实例化是在依赖注入中做的（getBean）
                    this.benMap.put(beanName, clazz.newInstance());
                }
                else
                {
                    continue;
                }
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException("实例化出错");
        }
    }

    /**
     * 载入阶段
     * 这里获取配置文件中扫描路径下的类的类名
     */
    private void doScanner(String packageName)
    {
        //获取basePackage的真实路径
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File packageDir = new File(url.getFile());
        for(File file : packageDir.listFiles())
        {
            if(file.isDirectory())
            {
                doScanner(packageName + "." + file.getName());
            }
            else if(file.getName().endsWith(".class"))
            {
                this.classNames.add(packageName + "." + file.getName().replace(".class", ""));
            }
        }
    }

    /**
     * 定位资源
     * 在Spring中，此阶段是通过Reader去查找和定位[配置文件]的
     * 
     * 在v1版本中，这里只是读取一个简单的properties文件
     * 并且只配置component-scan-base-package属性，充当<context:component-scan base-package="" />
     */
    private void doLoadConfig(String location)
    {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:", ""));
        try
        {
            if(is == null)
            {
                throw new RuntimeException("配置文件不存在");
            }
            contextConfig.load(is);
        }
        catch(IOException e)
        {
            throw new RuntimeException("加载配置文件出错", e);
        }
        finally
        {
            if(is != null)
            {
                try
                {
                    is.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 首字母小写
     * @param str
     * @return
     * @author 晓月残魂
     * @date 2018年4月27日下午4:44:35
     */
    private String lowerFirstCase(String str)
    {
        if(str != null && str.length() > 0)
        {
            char c = str.charAt(0);
            if(c > 'A' && c < 'Z')
            {
                c += 32;
                return c + str.substring(1);
            }
        }
        return str;
    }

    @Override
    public Object getBean(String beanName)
    {
        return this.benMap.get(beanName);
    }
}
