
package com.shiningstone.accounter.db;

import java.util.ArrayList;

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
	private MyDbInfo mDbInfo = null;

	private MyDbHelper(Context ctx) {
		this.mCtx = ctx;
		this.mDbInfo = MyDbInfo.getInstance();
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
				user interface
	****************************************************************/
	public double GetSum(String table, String field, String startDate, String endDate) {
		String timespan = SqlTimeSpan(startDate,endDate);
		String[] param = SqlTimeParam(startDate,endDate);
		
		String sql = "select sum(" + field + ") from " + table + timespan;
		
		Cursor cursor = mDb.rawQuery( sql, param );
		if( cursor.moveToNext() ) {
			return cursor.getDouble(0);
		} else {
			return 0;
		}
	}
	
	public void LoadIdName( ArrayList<Integer> ids, ArrayList<String> names, int tblIdx ) {
		ids.clear();
		names.clear();
		
		Cursor cursor = mDb.query( mDbInfo.TableName(tblIdx), mDbInfo.FieldNames(tblIdx), null, null, null, null, null);
		while( cursor.moveToNext() ) {
			ids.add( cursor.getInt(0) );
			names.add( cursor.getString(1) );
		}
		cursor.close();
	}

	public void LoadIdBudget( ArrayList<Integer> ids, ArrayList<String> names, ArrayList<Double> budgets, int tblIdx ) {
		ids.clear();
		names.clear();
		budgets.clear();
		
		Cursor cursor = mDb.query( mDbInfo.TableName(tblIdx), mDbInfo.FieldNames(tblIdx), null, null, null, null, null);
		while( cursor.moveToNext() ) {
			ids.add( cursor.getInt(0) );
			names.add( cursor.getString(1) );
			budgets.add( cursor.getDouble(2) );
		}
		cursor.close();
	}

	public void LoadIdNameNum( ArrayList<Integer> ids, ArrayList<String> names, ArrayList<Integer> num, int tblIdx ) {
		ids.clear();
		names.clear();
		num.clear();
		
		Cursor cursor = mDb.query( mDbInfo.TableName(tblIdx), mDbInfo.FieldNames(tblIdx), null, null, null, null, null);
		while( cursor.moveToNext() ) {
			ids.add( cursor.getInt(0) );
			names.add( cursor.getString(1) );
			num.add( cursor.getInt(2) );
		}
		cursor.close();
	}
	
	public void LoadAccountData( ArrayList<Integer> ids, ArrayList<String> names, ArrayList<Integer> type,
								ArrayList<Integer> cate, ArrayList<Double> amount, int tblIdx) {
		ids.clear();
		names.clear();
		type.clear();
		cate.clear();
		amount.clear(); 

		Cursor cursor = mDb.query( mDbInfo.TableName(tblIdx), mDbInfo.FieldNames(tblIdx), null, null, null, null, null);
		while( cursor.moveToNext() ) {
			ids.add( cursor.getInt(0) );
			names.add( cursor.getString(1) );
			type.add( cursor.getInt(2) );
			cate.add( cursor.getInt(3) );
			amount.add( cursor.getDouble(4) ); 
		}
		cursor.close();
	}
	
	private String SqlTimeSpan( String start, String end ) {
		if ( start==null && end==null ) {
			return "";
		} else if ( end==null ) {
			return " where strftime('%Y-%m-%d',DATE)=?";
		} else {
			return " where strftime('%Y-%m-%d',DATE)>=? and strftime('%Y-%m-%d',DATE)<=?";
		}
	}
	
	private String[] SqlTimeParam( String start, String end ) {
		String[] param = null;
		
		if ( start==null && end==null ) {
			param = null;
		} else if ( end==null ) {
			param = new String[1];
			param[0] = new String(start);
		} else {
			param = new String[2];
			param[0] = new String(start);
			param[1] = new String(end);
		}
		
		return param;
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

	public long insert(String table, String[] fields, String[] values ) {
		ContentValues cv = new ContentValues();
		for (int i = 0; i < fields.length; i++) {
			cv.put(fields[i], values[i]);
		}
		return mDb.insert(table, null, cv);
	}
	
	public long insert(int tblId, String fields[][])
	{
		return insert( mDbInfo.TableName(tblId), fields );
	}

	public int delete(String table, String where, String[] whereValue)
	{
		return mDb.delete(table, where, whereValue);
	}

	public int delete(int tblId, String where, String[] whereValue)
	{
		return delete( mDbInfo.TableName(tblId), where, whereValue);
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

	public int update(int tblId, String fields[],
			String values[], String where, String[] whereValue)
	{
		return update( mDbInfo.TableName(tblId), fields, values, where, whereValue );
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