package com.yc.blogs.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class JpaUtil {


    /**
     * @param src
     * @param target
     * @name 排除指定字段
     */
    public static void copyNotNullPropertiesExclude(Object src, Object target, String[] Field) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src, Field, true));
    }

    /**
     * @param src
     * @param target
     * @name 允许指定字段
     */
    public static void copyNotNullPropertiesAllow(Object src, Object target, String[] excludeField) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src, excludeField, false));
    }


    /**
     * @param src
     * @param target
     * @name 允许指定字段
     */
    public static void copyPropertiesAllow(Object src, Object target, String[] excludeField) {
        BeanUtils.copyProperties(src, target, getPropertyNames(src, excludeField, false));
    }

    /**
     * @param source
     * @return
     * @name 筛选忽略字段
     */
    public static String[] getNullPropertyNames(Object source, String[] excludeField, boolean tag) {
        String[] result = null;
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (pd.getName().equals("baseData")) {
                continue;
            } else {
                Object srcValue = src.getPropertyValue(pd.getName());
                //pd.getName() , 存在 Allow ,Exclude
                boolean contains = Arrays.asList(excludeField).contains(pd.getName());
                if (srcValue == null || contains == tag) {
                    emptyNames.add(pd.getName());
                }
            }
        }
        result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }



    /**
     * @param source
     * @return
     * @name 筛选忽略字段 ， 不去掉null
     */
    public static String[] getPropertyNames(Object source, String[] excludeField, boolean tag) {
        String[] result = null;
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (pd.getName().equals("baseData")) {
                continue;
            } else {
                Object srcValue = src.getPropertyValue(pd.getName());
                //pd.getName() , 存在 Allow ,Exclude
                boolean contains = Arrays.asList(excludeField).contains(pd.getName());
                if (contains == tag) {
                    emptyNames.add(pd.getName());
                }
            }
        }
        result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}