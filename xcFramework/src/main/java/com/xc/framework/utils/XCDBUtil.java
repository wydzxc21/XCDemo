package com.xc.framework.utils;

import java.util.List;
import android.content.Context;
import com.xc.framework.db.DBManager;
/**
 * @author ZhangXuanChen
 * @date 2016-11-13
 * @package com.xc.framework.utils
 * @description 数据库操作工具类
 */
public class XCDBUtil {
	/**
	 * 创建数据库表
	 * 
	 * @param context 上下文
	 * @param tableClass
	 *            以实体类名创建表名,成员变量创建字段(只支持String类型变量,相同类名不会重复创建表)
	 * @return 是否成功
	 */
	public static boolean createTable(Context context, Class<?> tableClass) {
		return DBManager.getInstance(context).createTable(tableClass);
	}

	/**
	 * 数据库表是否存在
	 * 
	 * @param context 上下文
	 * @param tableClass
	 *            以实体类名创建的表
	 * @return 是否成功
	 */
	public static boolean isTableExist(Context context, Class<?> tableClass) {
		return DBManager.getInstance(context).isTableExist(tableClass);
	}

	/**
	 * 删除数据库表
	 * 
	 * @param context 上下文
	 * @param tableClass
	 *            以实体类名创建的表
	 * @return 是否成功
	 */
	public static boolean deleteTable(Context context, Class<?> tableClass) {
		return DBManager.getInstance(context).deleteTable(tableClass);
	}

	/**
	 * 删除数据库
	 * 
	 * @param context 上下文
	 * @return 是否成功
	 */
	public static boolean deleteDB(Context context) {
		return DBManager.getInstance(context).deleteDB();
	}

	/**
	 * 插入
	 * 
	 * @param context 上下文
	 * @param classObject
	 *            类对象,操作以该对象类名创建的表,反射get方法获取插入数据,只支持String变量(完全相同的数据不会重复插入)
	 * @return 是否成功
	 */
	public static boolean insert(Context context, Object classObject) {
		return DBManager.getInstance(context).insert(classObject);
	}

	/**
	 * 删除
	 * 
	 * @param context 上下文
	 * @param classObject
	 *            类对象,操作以该对象类名创建的表,反射get方法获取删除条件(条件唯一删除唯一一条数据,条件不唯一删除符合条件的所有数据,
	 *            new空对象删除该表所有数据 )
	 * 
	 * @return 是否成功
	 */
	public static boolean delete(Context context, Object classObject) {
		return DBManager.getInstance(context).delete(classObject);
	}

	/**
	 * 更新
	 * 
	 * @param context 上下文
	 * @param updateObject
	 *            更新数据类对象,反射get方法获取更新数据(要与查询条件类对象为相同类的对象)
	 * 
	 * @param conditionObject
	 *            查询条件类对象,操作以该对象类名创建的表,反射get方法获取查询条件(条件唯一更新唯一一条数据,
	 *            条件不唯一更新符合条件的所有数据, new空对象更新该表所有数据)
	 * 
	 * @return 是否成功
	 */
	public static boolean update(Context context, Object updateObject, Object conditionObject) {
		return DBManager.getInstance(context).update(updateObject, conditionObject);
	}

	/**
	 * 查询
	 * 
	 * @param context 上下文
	 * @param classObject
	 *            类对象,操作以该对象类名创建的表,反射get方法获取查询条件(条件唯一返回唯一一条数据,条件不唯一返回符合条件的所有数据,
	 *            new空对象查询该表所有数据 )
	 * @return 结果集
	 */
	public static <T> List<T> query(Context context, T classObject) {
		return DBManager.getInstance(context).query(classObject);
	}

	/**
	 * 是否存在
	 * 
	 * @param context 上下文
	 * @param classObject
	 *            类对象,操作以该对象类名创建的表,反射get方法获取查询条件(有符合条件的就返回true)
	 * @return 是否存在
	 */
	public static <T> boolean isExist(Context context, T classObject) {
		return DBManager.getInstance(context).isExist(classObject);
	}
}
