package com.xych.spring.v2.beans.factory.config;

public interface BeanDefinition
{
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void setBeanClassName(String beanClassName);

    String getBeanClassName();

    void setScope(String scope);

    String getScope();

    boolean isSingleton();

    boolean isPrototype();

    void setLazyInit(boolean lazyInit);

    boolean isLazyInit();

}
