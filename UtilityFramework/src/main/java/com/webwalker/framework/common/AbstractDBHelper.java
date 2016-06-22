package com.webwalker.framework.common;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class AbstractDBHelper {

	Context context;

	public AbstractDBHelper(Context context) {
		this.context = context;
	}

	// SQLite数据库实例
	protected SQLiteDatabase db = null;

	// 数据库创建帮手
	protected CreateDBHelper helper = null;

	// 获得当前数据库帮手类标识(一般是该类名称)，用于日志等的记录
	protected abstract String getTag();

	// 获得数据库名称
	protected abstract String getDbName();

	/**
	 * 获得数据库版本，值至少为1。 当数据库结构发生改变的时候，请将此值加1，系统会在初始化时自动调用
	 * createDBTables和dropDBTables方法更新数据库结构。
	 */
	protected abstract int getDatabaseVersion();

	public SQLiteDatabase getDatabase() {
		this.init();

		return db;
	}

	// 创建数据库表的SQL语句，一个元素一条语句
	protected abstract String[] createDBTables();

	// 删除数据库表的SQL语句，一个元素一条语句
	protected abstract String[] dropDBTables();

	// 内部数据库创建帮手类
	private class CreateDBHelper extends SQLiteOpenHelper {

		public CreateDBHelper(Context ctx) {
			super(ctx, getDbName(), null, getDatabaseVersion());
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// executeBatch(createDBTables(), db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(getTag(), "Upgrading database '" + getDbName()
					+ "' from version " + oldVersion + " to " + newVersion);
			executeBatch(dropDBTables(), db);
			onCreate(db);
		}

		// 批量执行Sql语句
		private void executeBatch(String[] sqls, SQLiteDatabase db) {
			if (sqls == null) {
				return;
			}

			db.beginTransaction();
			try {
				int len = sqls.length;
				for (int i = 0; i < len; i++) {
					db.execSQL(sqls[i]);
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				Log.e(getTag(), e.getMessage(), e);
			} finally {
				db.endTransaction();
			}
		}
	}

	// 打开或者创建一个指定名称的数据库
	public void open() {
		Log.i(getTag(), "Open database '" + getDbName() + "'");
		try {
			helper = new CreateDBHelper(context);
			if (helper != null) {
				db = helper.getWritableDatabase();
			}
		} catch (SQLException e) {
			Log.e("open", e.getMessage());
		}

	}

	// 关闭数据库
	public void close() {
		try {
			if (helper != null) {
				Log.i(getTag(), "Close database '" + getDbName() + "'");
				helper.close();
			}
		} catch (SQLException e) {
			Log.e("close", e.getMessage());
		}
	}

	// 执行sql
	protected void execSql(String sql) {
		if (sql.equals("")) {
			return;
		}

		try {
			// 如果子类没有实例化，则默认进行实例化
			this.init();

			db.execSQL(sql);
		} catch (SQLException e) {
			Log.i(getTag(), e.getMessage(), e);
		}
	}

	protected boolean execSql(int sqlId, Object[] bindArgs) {
		return execSql(getSql(sqlId), bindArgs);
	}

	protected boolean execSql(String sql, Object[] bindArgs) {
		try {
			this.init();

			db.execSQL(sql, bindArgs);
		} catch (Exception e) {
			Log.e(getTag(), "execSql:" + e);
			return false;
		}
		// finally {
		// db.close(); // 不关闭，子类调用时，可以批量处理后关闭
		// }
		return true;
	}

	protected boolean execAutoClose(int sqlId, Object[] bindArgs) {
		return execAutoClose(getSql(sqlId), bindArgs);
	}

	// 执行并自动关闭
	protected boolean execAutoClose(String sql, Object[] bindArgs) {
		boolean ret = execSql(sql, bindArgs);
		if (ret && db != null) {
			db.close();
		}
		return ret;
	}

	protected Cursor Query(int sqlId, String[] strArgs) {
		return Query(getSql(sqlId), strArgs);
	}

	protected Cursor Query(String sql, String[] strArgs) {
		this.init();

		return db.rawQuery(sql, strArgs);
	}

	private void init() {
		if (db == null)
			this.open();
		else if (!db.isOpen()) {
			db = helper.getWritableDatabase();
		}
	}

	protected String getSql(int sqlId) {
		return context.getString(sqlId);
		// context.getResources().getString(sqlId);
	}
}
