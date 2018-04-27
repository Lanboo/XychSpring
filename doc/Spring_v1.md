[toc]
# XychSpring V1
> 模拟Spring IOC容器的初始化。<br>
> 只单单的模拟Spring的4个过程<br>
> 1. 定位<br>
> 2. 加载<br>
> 3. 注册<br>
> 4. 依赖注入<br>


## 1、Coding
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
}
```
### 1.1、定位
``` java
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
                throw new RuntimeException("关闭文件流失败", e);
            }
        }
    }
}
```

### 1.2、载入、加载
``` java
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
```

### 1.3、注册
``` java
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
```

### 1.4、依赖注入
``` java
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
```

## 2、Demo

![](https://github.com/Lanboo/resource/blob/master/images/XychSpring/XychSpring_v1_Test.png?raw=true)

![](https://github.com/Lanboo/resource/blob/master/images/XychSpring/XychSpring_v1_Test_ClassDiagram.png?raw=true)

这个类图描述：一个学生有一个老师、一本书、一个玻璃杯或者塑料杯。

``` properties
# applicationContext.properties
component-scan-base-package=com.xych.spring.v1.bean
```

``` java
@Component
public class Student
{
    @Autowired
    private Book book;

    @Autowired
    private Teacher teacher;

    @Autowired
    @Qualifier("glassCap")
    private Cap cap;
    
    // getter、setter
}
```

``` java
public class XychSpring_V1_Test
{
    public static void main(String[] args)
    {
        String location = "classpath:v1/applicationContext.properties";
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory(location);
        Student student = (Student) beanFactory.getBean("student");
        
        System.out.println(student);
        System.out.println(student.getTeacher());
        System.out.println(student.getCap());
    }
}
/* 运行结果
com.xych.spring.v1.bean.Student@3339ad8e
com.xych.spring.v1.bean.Teacher@3a4afd8d
com.xych.spring.v1.bean.GlassCap@6d1e7682
*/
```