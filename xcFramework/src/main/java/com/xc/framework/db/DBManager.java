package com.xc.framework.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xc.framework.utils.XCBeanUtil;
import com.xc.framework.utils.XCStringUtil;

/**
 * @author ZhangXuanChen
 * @date 2015-9-22
 * @package com.xc.framework.utils
 * @description 数据库管理类
 */
public class DBManager {
	private Context context;
	public static DBManager mDBManager;

	public DBManager(Context context) {
		this.context = context;
	}

	public static synchronized DBManager getInstance(Context context) {
		if (mDBManager == null) {
			mDBManager = new DBManager(context);
		}
		return mDBManager;
	}

	/**
	 * 创建数据库表
	 * 
	 * @param tableClass
	 *            以实体类名创建表名,成员变量创建字段(只支持String类型变量,相同类名不会重复创建表)
	 * @return
	 */
	public synchronized boolean createTable(Class<?> tableClass) {
		SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
		if (db != null && tableClass != null) {
			try {
				db.execSQL(getCreateTableSql(tableClass));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				if (db != null) {
					db.close();
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 数据库表是否存在
	 * 
	 * @param tableClass
	 *            以实体类名创建的表
	 * @return
	 */
	public synchronized boolean isTableExist(Class<?> tableClass) {
		SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
		if (db != null && tableClass != null) {
			try {
				String sql = "select * from " + tableClass.getSimpleName();
				Cursor rawQuery = db.rawQuery(sql, null);
				if (rawQuery == null) {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				if (db != null) {
					db.close();
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 删除数据库表
	 * 
	 * @param tableClass
	 *            以实体类名创建的表
	 * @return
	 */
	public synchronized boolean deleteTable(Class<?> tableClass) {
		SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
		if (db != null && tableClass != null) {
			try {
				String sql = "drop table " + tableClass.getSimpleName();
				db.execSQL(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				if (db != null) {
					db.close();
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 删除数据库
	 * 
	 * @return
	 */
	public synchronized boolean deleteDB() {
		DBHelper dbHelper = DBHelper.getInstance(context);
		if (dbHelper != null) {
			try {
				return dbHelper.deleteDB(context);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 插入
	 * 
	 * @param classObject
	 *            类对象,操作以该对象类名创建的表,反射get方法获取插入数据,只支持String变量(完全相同的数据不会重复插入)
	 * @return
	 */
	public synchronized boolean insert(Object classObject) {
		if (!isTableExist(classObject.getClass())) {
			createTable(classObject.getClass());
		}
		if (!isExist(classObject)) {
			SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
			if (db != null && classObject != null) {
				try {
					db.execSQL(getInsertSql(classObject));
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} finally {
					if (db != null) {
						db.close();
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 删除
	 * 
	 * @param classObject
	 *            类对象,操作以该对象类名创建的表,反射get方法获取删除条件(条件唯一删除唯一一条数据,条件不唯一删除符合条件的所有数据,
	 *            new空对象删除该表所有数据 )
	 * 
	 * @return
	 */
	public synchronized boolean delete(Object classObject) {
		if (!isTableExist(classObject.getClass())) {
			createTable(classObject.getClass());
		}
		if (isExist(classObject)) {
			SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
			if (db != null && classObject != null) {
				try {
					db.execSQL(getDeleteSql(classObject));
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} finally {
					if (db != null) {
						db.close();
					}
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 更新
	 * 
	 * @param updateObject
	 *            更新数据类对象,反射get方法获取更新数据(要与查询条件类对象为相同类的对象)
	 * 
	 * @param conditionObject
	 *            查询条件类对象,操作以该对象类名创建的表,反射get方法获取查询条件(条件唯一更新唯一一条数据,
	 *            条件不唯一更新符合条件的所有数据, new空对象更新该表所有数据)
	 * 
	 * @return
	 */
	public synchronized boolean update(Object updateObject, Object conditionObject) {
		if (!isTableExist(conditionObject.getClass())) {
			createTable(conditionObject.getClass());
		}
		if (isExist(conditionObject)) {
			SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
			if (db != null && conditionObject != null && updateObject != null) {
				if (conditionObject.getClass().equals(updateObject.getClass())) {// 同一实体类的对象
					try {
						db.execSQL(getUpdateSql(updateObject, conditionObject));
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					} finally {
						if (db != null) {
							db.close();
						}
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 查询
	 * 
	 * @param classObject
	 *            类对象,操作以该对象类名创建的表,反射get方法获取查询条件(条件唯一返回唯一一条数据,条件不唯一返回符合条件的所有数据,
	 *            new空对象查询该表所有数据 )
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> List<T> query(T classObject) {
		if (!isTableExist(classObject.getClass())) {
			createTable(classObject.getClass());
		}
		SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
		List<T> mList = new ArrayList<T>();
		if (db != null && classObject != null) {
			Cursor cursor = null;
			try {
				cursor = db.rawQuery(getQuerySql(classObject), null);
				if (cursor != null) {
					if (cursor.moveToFirst()) {
						do {
							List<String> nameList = XCBeanUtil.getFieldNameList(classObject.getClass());
							T newClassObject = (T) classObject.getClass().newInstance();
							if (nameList != null && !nameList.isEmpty() && newClassObject != null) {
								for (int i = 0; i < nameList.size(); i++) {
									// key
									String key = nameList.get(i);
									// value
									String value = cursor.getString(cursor.getColumnIndex(key));
									XCBeanUtil.invokeSetMethod(newClassObject, key, value);
								}
								mList.add(newClassObject);
							}
						} while (cursor.moveToNext());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (db != null) {
					db.close();
				}
				if (cursor != null) {
					cursor.close();
				}
			}
		}
		return mList;
	}

	/**
	 * 是否存在
	 * 
	 * @param classObject
	 *            类对象,操作以该对象类名创建的表,反射get方法获取查询条件(有符合条件的就返回true)
	 * @return
	 */
	public synchronized <T> boolean isExist(T classObject) {
		List<T> queryList = query(classObject);
		if (queryList != null && !queryList.isEmpty()) {
			if (queryList.size() > 0) {
				return true;
			}
		}
		return false;

	}

	// ------------------------------------------------生成sql语句方法--------------------------------------------------
	/**
	 * 根据实体类生成创建数据库表sql语句
	 * 
	 * @param tableClass
	 * @return
	 */
	private String getCreateTableSql(Class<?> tableClass) {
		List<String> nameList = XCBeanUtil.getFieldNameList(tableClass);
		String sql = "";
		if (nameList != null && !nameList.isEmpty()) {
			for (int i = 0; i < nameList.size(); i++) {
				sql += "," + nameList.get(i) + " text";
			}
		}
		return "create table if not exists " + tableClass.getSimpleName() + "(_id integer not null primary key autoincrement" + sql + ")";
	}

	/**
	 * 根据类对象生成-插入sql语句
	 * 
	 * @param classObject
	 *            类对象
	 * @return
	 */
	private String getInsertSql(Object classObject) {
		String key = "";
		String value = "";
		List<String> nameList = XCBeanUtil.getFieldNameList(classObject.getClass());
		if (nameList != null && !nameList.isEmpty()) {
			for (int i = 0; i < nameList.size(); i++) {
				// key
				key += nameList.get(i) + ",";
				// value
				value += "'" + XCBeanUtil.invokeGetMethod(classObject, nameList.get(i)) + "',";
			}
			key = key.substring(0, key.length() - 1);
			value = value.substring(0, value.length() - 1);
		}
		return "insert into " + classObject.getClass().getSimpleName() + " (" + key + ") values (" + value + " )";
	}

	/**
	 * 根据类对象生成-删除sql语句
	 * 
	 * @return
	 */
	private String getDeleteSql(Object classObject) {
		String condition = getKeyEqualValueSql(classObject, "and");
		if (XCStringUtil.isEmpty(condition)) {
			return "delete from " + classObject.getClass().getSimpleName();
		} else {
			return "delete from " + classObject.getClass().getSimpleName() + " where " + condition;
		}

	}

	/**
	 * 根据类对象生成-更新sql语句
	 * 
	 * @param classObject
	 * @return
	 */
	private String getUpdateSql(Object updateObject, Object conditionObject) {
		String updateSql = getKeyEqualValueSql(updateObject, ",");
		String conditionSql = getKeyEqualValueSql(conditionObject, "and");
		if (XCStringUtil.isEmpty(conditionSql)) {
			return "update " + conditionObject.getClass().getSimpleName() + " set " + updateSql;
		} else {
			return "update " + conditionObject.getClass().getSimpleName() + " set " + updateSql + " where " + conditionSql;
		}
	}

	/**
	 * 根据类对象生成-查询sql语句
	 * 
	 * @param classObject
	 *            类对象
	 * @return
	 */
	private String getQuerySql(Object classObject) {
		String condition = getKeyEqualValueSql(classObject, "and");
		if (XCStringUtil.isEmpty(condition)) {
			return "select * from " + classObject.getClass().getSimpleName();
		} else {
			return "select * from " + classObject.getClass().getSimpleName() + " where " + condition;
		}
	}

	/**
	 * 获取key等于value的sql语句
	 * 
	 * @param classObject
	 *            类对象
	 * @param connectFlag
	 *            ","或"and"
	 * @return key = 'value' connectFlag key = 'value'
	 */
	private static String getKeyEqualValueSql(Object classObject, String connectFlag) {
		String condition = "";
		List<String> nameList = XCBeanUtil.getFieldNameList(classObject.getClass());
		if (nameList != null && !nameList.isEmpty()) {
			for (int i = 0; i < nameList.size(); i++) {
				// key
				String key = nameList.get(i);
				// value
				String value = (String) XCBeanUtil.invokeGetMethod(classObject, nameList.get(i));
				if (!XCStringUtil.isEmpty(value)) {
					condition += key + " = " + "'" + value + "' " + connectFlag + " ";
				}
			}
			if (!XCStringUtil.isEmpty(condition)) {
				if (",".equals(connectFlag)) {
					condition = condition.substring(0, condition.length() - 3);
				} else if ("and".equals(connectFlag)) {
					condition = condition.substring(0, condition.length() - 5);
				}
			}
		}
		return condition;
	}
}
