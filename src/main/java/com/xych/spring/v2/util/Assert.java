package com.xych.spring.v2.util;

import java.util.Collection;

public abstract class Assert
{
    public static void notNull(Object object, String message)
    {
        if(object == null)
        {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String text, String message)
    {
        if(text == null || text.isEmpty())
        {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasTrimLength(String text, String message)
    {
        if(text == null || text.trim().isEmpty())
        {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message)
    {
        if(collection == null || collection.isEmpty())
        {
            throw new IllegalArgumentException(message);
        }
    }
}
