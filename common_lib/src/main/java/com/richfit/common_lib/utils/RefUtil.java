package com.richfit.common_lib.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.data.exception.InstanceException;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * Created by monday on 2017/6/11.
 */

public class RefUtil {

    public static BaseFragment findFragment(FragmentManager fm, String tag, String companyCode, String moduleCode,
                                            String bizType, String refType, int fragmentType, String title, Class clazz) throws Exception{
        BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = newInstance(clazz, companyCode, moduleCode, bizType, refType, fragmentType, title);
        }
        return fragment;
    }

    public static BaseFragment findFragment(FragmentManager fm, String tag, Bundle arguments, String className) throws Exception {
        BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = newInstance(className, arguments);
        }
        return fragment;
    }

    private static void setFieldValue(Class clazz, Object object, String value) {
        try {
            /**
             * getDeclaredMethod*()获取的是类自身声明的所有方法，包含public、protected和private方法。
             * getMethod*()获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。
             */
            Method method = clazz.getMethod("setTabTitle", String.class);
            method.setAccessible(true);
            method.invoke(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成BaseFragment的子类的实例对象
     *
     * @param clazz
     * @param moduleCode:组模块编码
     * @param bizType：子模块编码
     * @param refType：业务类型
     * @param <T>
     * @return
     */
    private static <T extends BaseFragment> T newInstance(final Class<T> clazz, String companyCode, String moduleCode,
                                                          String bizType, String refType, int fragmentType, String title)  throws  Exception  {
        T instance = clazz.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(Global.EXTRA_COMPANY_CODE_KEY, companyCode);
        bundle.putString(Global.EXTRA_MODULE_CODE_KEY, moduleCode);
        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, bizType);
        bundle.putString(Global.EXTRA_REF_TYPE_KEY, refType);
        bundle.putString(Global.EXTRA_TITLE_KEY, title);
        bundle.putInt(Global.EXTRA_FRAGMENT_TYPE_KEY, fragmentType);
        setFieldValue(clazz, instance, title);
        instance.setArguments(bundle);
        return instance;

    }

    public static <T extends BaseFragment> T newInstance(String className, Bundle arguments) throws  Exception {
        final Class clazz = Class.forName(className);
        T instance = (T) clazz.newInstance();
        String tabTitle = arguments.getString(Global.EXTRA_TITLE_KEY);
        setFieldValue(clazz, instance, tabTitle);
        instance.setArguments(arguments);
        return instance;
    }

    public static Class<?> getRawType(Type type) {
        if (type == null) throw new NullPointerException("type == null");

        if (type instanceof Class<?>) {
            // Type is a normal class.
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) throw new IllegalArgumentException();
            return (Class<?>) rawType;
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }

        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
    }

    public static <T> T newInstance(Class<T> clazz) throws InstanceException {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new InstanceException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new InstanceException(e.getMessage());
        }
    }
}
