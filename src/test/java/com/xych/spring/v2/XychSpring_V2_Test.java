package com.xych.spring.v2;

import com.xych.spring.v2.context.DefaultApplicationContext;

public class XychSpring_V2_Test
{
    public static void main(String[] args)
    {
        String location = "classpath:v2/applicationContext.properties";
        DefaultApplicationContext applicationContext = new DefaultApplicationContext(location);
        System.out.println(applicationContext);
        //        Student student = (Student) beanFactory.getBean("student");
        //        
        //        System.out.println(student);
        //        System.out.println(student.getTeacher());
        //        System.out.println(student.getCap());
    }
}
