package com.xych.spring.v1;

import com.xych.spring.v1.bean.Student;
import com.xych.spring.v1.core.DefaultListableBeanFactory;

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
