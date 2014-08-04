
package com.shiningstone.accounter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper
{
	/**********************************************************
				Singleton
	**********************************************************/
	private static MyDbHelper openHelper = null;
	private final Context mCtx;

	private MyDbHelper(Context ctx) {
		this.mCtx = ctx;
	}

	public static MyDbHelper getInstance(Context context){
		if(openHelper == null){
			openHelper = new MyDbHelper(context);
		}
		return openHelper;
	}

	/**********************************************************
				
	**********************************************************/
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static int version = 1;
	private static String myDBName = "mydb";

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, myDBName, null, version);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			CreateTables(db);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			DeleteTables(db);
			CreateTables(db);
		}
	}
	
	/****************************************************************
				database operations
	****************************************************************/
	public MyDbHelper open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		mDbHelper.close();
	}

	public void execSQL(String sql) throws java.sql.SQLException
	{
		mDb.execSQL(sql);
	}
	
	public Cursor rawQuery(String sql,String[] selectionArgs)
	{
		return mDb.rawQuery(sql, selectionArgs);
	}

	public Cursor select(String table, String[] columns, 
			String selection, String[] selectionArgs, String groupBy, 
			String having, String orderBy)
	{
		return mDb.query
		(
			table, columns, selection, selectionArgs, 
			groupBy, having, orderBy
		);
	}

	public long insert(String table, String fields[][])
	{
		final int KEY = 0;
		final int VALUE = 1;
		ContentValues cv = new ContentValues();
		for (int i = 0; i < fields.length; i++) {
			cv.put(fields[i][KEY], fields[i][VALUE]);
		}
		return mDb.insert(table, null, cv);
	}

	public int delete(String table, String where, String[] whereValue)
	{
		return mDb.delete(table, where, whereValue);
	}

	public int update(String table, String updateFields[],
			String updateValues[], String where, String[] whereValue)
	{
		ContentValues cv = new ContentValues();
		for (int i = 0; i < updateFields.length; i++)
		{
			cv.put(updateFields[i], updateValues[i]);
		}
		return mDb.update(table, cv, where, whereValue);
	}

	/****************************************************************
				private methods
	****************************************************************/
	private static void CreateTables(SQLiteDatabase db) {
		for (int i = 0; i < MyDbInfo.Tables.length; i++) {
			String sql = "CREATE TABLE " + MyDbInfo.Tables[i].Name + " (";
			for (int j = 0; j < MyDbInfo.Tables[i].Fields.size(); j++){
				sql += MyDbInfo.Tables[i].Fields.get(j).Name + " " 
						+ MyDbInfo.Tables[i].Fields.get(j).Type + ",";
			}
			sql = sql.substring(0, sql.length() - 1);
			sql += ")";
			db.execSQL(sql);
		} 
	}
	
	private static void DeleteTables(SQLiteDatabase db) {
		for (int i = 0; i < MyDbInfo.Tables.length; i++){
			String sql = "DROP TABLE IF EXISTS " + MyDbInfo.Tables[i].Name;
			db.execSQL(sql);
		}
	}
}