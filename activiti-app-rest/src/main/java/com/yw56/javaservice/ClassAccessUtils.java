package com.yw56.javaservice;

import java.lang.reflect.Field;

public class ClassAccessUtils {
    /**
     * 获取类的静态属相的访问权
     *
     * @param <T>
     * @param targetClass
     * @param fieldName
     */
    public static <T> T getStaticPropertyAccess(Class targetClass, String fieldName) {
        try {
            Field f = targetClass.getDeclaredField(fieldName);
            f.setAccessible(true);
            T o = (T) f.get(targetClass);
            return o;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
