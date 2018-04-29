[toc]
# XychSpring

<b>目的：为了更好的理解Spring，更好的看懂Spring源码</b><br>

<b>大体上分为三个模块：ICO容器的初始化、Spring MVC、Spring AOP</b><br>
<b>每个模块分为两个版本，v1只是单单有个主体流程，v2明确出各个主要的类的功能</b>
## 1、Spring IOC
### 1.1、v1版本
> 模拟Spring IOC容器的初始化。<br>
> 只单单的模拟Spring的4个过程<br>
> 1. 定位<br>
> 2. 加载<br>
> 3. 注册<br>
> 4. 依赖注入<br>

[Spring IOC V1](doc/Spring_IOC_V1.md)

> 1. 实现了`BeanFactory.getBean(String beanName)`
> 2. 实现了注解`@Component`、`@Autowired`、`@Qualifier`

未完善部分：

> 1. 实现过程过于简单，不符合单一职责
> 2. `@Autowired`、`@Qualifier`只在`ElementType.FIELD`级别上有效

### 1.2、v2版本
[Spring IOC V2](doc/Spring_IOC_V2.md)
> - 1、IOC初始化<br>
>   - 1.1、定位<br>
>   - 1.2、加载<br>
>   - 1.3、注册<br>
> - 2、依赖注入<br>