package com.xych.spring.v1.bean;

import com.xych.spring.v1.annotation.Autowired;
import com.xych.spring.v1.annotation.Component;
import com.xych.spring.v1.annotation.Qualifier;

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

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
    }

    public Teacher getTeacher()
    {
        return teacher;
    }

    public void setTeacher(Teacher teacher)
    {
        this.teacher = teacher;
    }

    public Cap getCap()
    {
        return cap;
    }

    public void setCap(Cap cap)
    {
        this.cap = cap;
    }
}
