[toc]
# XychSpring IOC V2
> 模拟Spring IOC容器的初始化。<br>
> 引入BeanDefinition，分为两个过程：IOC初始化、依赖注入(DI)<br>
> - 1、IOC初始化<br>
>   - 1.1、定位<br>
>   - 1.2、加载<br>
>   - 1.3、注册<br>
> - 2、依赖注入<br>

<br>

代码见[/main/java/com.xych.spring.v2](../src/main/java/com/xych/spring/v2)<br>
测试代码[/test/java/com.xych.spring.v2](../src/test/java/com/xych/spring/v2)

## 1、IOC初始化
### 1.1、类图
![](https://github.com/Lanboo/resource/blob/master/images/XychSpring/XychSpring_V2_IOC_ClassDiagram.png?raw=true)

### 1.2、时序图
![](https://github.com/Lanboo/resource/blob/master/images/XychSpring/XychSpring_V2_IOC_SequenceDiagram.png?raw=true)

### 1.3、主要流程
- 1、`DefaultApplicationContext`调用父类`AbstractApplicationContext.refresh`方法
- 2、在`obtainFreshBeanFactory`方法中，创建`BeanFactory`的实现类`DefaultListableBeanFactory`
- 3、接着创建`BeanDefinitionReader`的实现类`PropertiesBeanDefinitionReader`（本项目中的配置文件是`properties`，不是xml，故在Spring中，这一步骤通常创建的是XmlBeanDefinitionReader）
- 4、通过`BeanDefinitionReader.loadBeanDefinitions`方法去加载`BeanDefinition`
- 5、最终在子类`PropertiesBeanDefinitionReader.loadBeanDefinitions(String)`方法中实现怎样读取`BeanDefinition`。
    - 本项目中，是在配置文件中配置的`basePackage`，故仿照`ClassPathBeanDefinitionScanner`去读取BeanDefinition，并且简化了读取过程
    - 另外，在Spring，`XmlBeanDefinitionReader`会采用dom方式读取xml配置文件，从而借助`BeanDefinitionDocumentReader`解析xml
        - 5.1、配置文件根节点使用Spring默认的命名空间
            - 5.1.1、Spring中默认XML命名空间中的标签，例如`import`、`alias`、`bean`、`beans`
            - 5.1.2、在jar/META-INF/spring.handlers中配置解析规则类的标签，比如`context:component-scan`
        - 5.2、根节点没有使用Spring默认的命名空间
## 2、依赖注入(DI)
