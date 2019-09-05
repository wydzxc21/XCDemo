package com.xc.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author ZhangXuanChen
 * @date 2015-9-17
 * @package com.xc.framework.utils
 * @description 实体类工具类
 */
public class XCBeanUtil {
	/**
	 * 获取bean成员变量名集合
	 * 
	 * @param classBean
	 *            实体类
	 * @return 变量名集合
	 */
	public static List<String> getFieldNameList(Class<?> objectClass) {
		List<String> mList = null;
		if (objectClass != null) {
			try {
				Field[] fields = objectClass.getDeclaredFields();
				if (fields != null && fields.length > 0) {
					mList = new ArrayList<String>();
					for (int i = 0; i < fields.length; i++) {
						Field field = fields[i];
						if (field != null) {
							String filter = "serialVersionUID";// 序列化变量
							String change = "$";// studio 2.0以上反射多出参数:$change
							String name = field.getName() != null ? field.getName() : "";
							if (!name.equals(filter) && !name.contains(change)) {
								mList.add(name);
							}
						}
					}
				}
			} catch (Exception e) {
			}
		}
		return mList;
	}

	/**
	 * 获取get方法
	 * 
	 * @param objectClass
	 *            实体类
	 * @param fieldName
	 *            变量名(驼峰)
	 * @return get方法对象
	 */
	public static Method getGetMethod(Class<?> objectClass, String fieldName) {
		try {
			Field field = objectClass.getDeclaredField(fieldName);
			String newFieldName = "get" + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH) + fieldName.substring(1);
			if (fieldName.length() >= 2) {
				if (Character.isUpperCase(fieldName.charAt(1))) {
					newFieldName = "get" + fieldName;
				}
			}
			Method method = objectClass.getMethod(newFieldName, new Class[] {});
			method.setAccessible(true);
			return method;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取set方法
	 * 
	 * @param objectClass
	 *            实体类
	 * @param fieldName
	 *            变量名(驼峰)
	 * @return set方法对象
	 */
	public static Method getSetMethod(Class<?> objectClass, String fieldName) {
		try {
			Field field = objectClass.getDeclaredField(fieldName);
			String newFieldName = "set" + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH) + fieldName.substring(1);
			if (fieldName.length() >= 2) {
				if (Character.isUpperCase(fieldName.charAt(1))) {
					newFieldName = "set" + fieldName;
				}
			}
			Method method = objectClass.getMethod(newFieldName, new Class[] { field.getType() });
			method.setAccessible(true);
			return method;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 调用对应成员变量的get方法
	 * 
	 * @param classObject
	 *            实体类对象
	 * @param fieldName
	 *            变量名(驼峰)
	 * @return 是否成功
	 */
	public static Object invokeGetMethod(Object classObject, String fieldName) {
		if (classObject != null && !XCStringUtil.isEmpty(fieldName)) {
			try {
				Method methods = getGetMethod(classObject.getClass(), fieldName);
				return methods.invoke(classObject);
			} catch (Exception e) {

			}
		}
		return null;
	}

	/**
	 * 调用对应成员变量的set方法
	 * 
	 * @param classObject
	 *            实体类对象
	 * @param fieldName
	 *            变量名(驼峰)
	 * @param setValue
	 *            设置值
	 * @return 是否成功
	 */
	public static boolean invokeSetMethod(Object classObject, String fieldName, Object setValue) {
		if (classObject != null && !XCStringUtil.isEmpty(fieldName) && setValue != null) {
			try {
				Method method = getSetMethod(classObject.getClass(), fieldName);
				method.invoke(classObject, setValue);
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
