package com.xych.spring.v1.bean;

import com.xych.spring.v1.annotation.Component;

@Component
public class Book
{
    private String bookName;

    public String getBookName()
    {
        return bookName;
    }

    public void setBookName(String bookName)
    {
        this.bookName = bookName;
    }
}
