# XychSpring V1
> 模拟Spring IOC容器的初始化。<br>
> 只单单的模拟Spring的4个过程<br>
> 1. 定位<br>
> 2. 加载<br>
> 3. 注册<br>
> 4. 依赖注入<br>

``` java
public class DefaultListableBeanFactory implements BeanFactory
{
    /**
     * <pre>
     * 获取配置文件属性，Spring中是xml，这里用properties文件
     * 只配置component-scan-base-package属性，充当<context:component-scan base-package="" />
     * </pre>
     */
     
    private Properties contextConfig = new Properties();
    private List<String> classNames = new ArrayList<String>();
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
}
```