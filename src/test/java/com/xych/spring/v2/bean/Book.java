package com.xych.spring.v2.bean;

import com.xych.spring.v2.annotation.Component;

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
