package com.xych.spring.v2.beans.factory.support;

import com.xych.spring.v2.beans.factory.config.BeanDefinition;

public class GenericBeanDefinition implements BeanDefinition
{
    public static final String SCOPE_DEFAULT = "";
    private volatile Object beanClass;
    private String scope = SCOPE_DEFAULT;
    private boolean lazyInit = false;

    @Override
    public void setBeanClassName(String beanClassName)
    {
        this.beanClass = beanClassName;
    }

    @Override
    public String getBeanClassName()
    {
        Object beanClassObject = this.beanClass;
        if(beanClassObject instanceof Class)
        {
            return ((Class<?>) beanClassObject).getName();
        }
        else
        {
            return (String) beanClassObject;
        }
    }

    @Override
    public String getScope()
    {
        return scope;
    }

    @Override
    public void setScope(String scope)
    {
        this.scope = scope;
    }

    @Override
    public boolean isSingleton()
    {
        return SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
    }

    @Override
    public boolean isPrototype()
    {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public boolean isLazyInit()
    {
        return lazyInit;
    }

    @Override
    public void setLazyInit(boolean lazyInit)
    {
        this.lazyInit = lazyInit;
    }
}
