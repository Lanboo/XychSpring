package com.xych.spring.v2.util;

public abstract class StringUtils
{
    public static boolean isEmpty(Object str)
    {
        return (str == null || "".equals(str));
    }

    public static boolean isEmpty(String str)
    {
        return isEmpty(str);
    }

    public static boolean isTrimEmpty(String str)
    {
        return (str == null || "".equals(str.trim()));
    }

    /**
     * 首字母小写
     */
    public static String lowerFirstCase(String str)
    {
        if(str != null && str.length() > 0)
        {
            char c = str.charAt(0);
            if(c > 'A' && c < 'Z')
            {
                c += 32;
                return c + str.substring(1);
            }
        }
        return str;
    }
}
