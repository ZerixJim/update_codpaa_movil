package com.codpaa.db;



import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;




public class DBAdapter {
	
	private static final String DATABASE_TABLE = "usuarios";

 
	SQLiteDatabase mDb;
	Context mCtx;
	BDopenHelper mDbHelper = null;
 
	public DBAdapter(Context context){
		this.mCtx = context;
	}
 
	public void open(){
		
		mDbHelper = new BDopenHelper(mCtx);
		mDb = mDbHelper.getReadableDatabase();
		
	}
 
	public void close(){
		if (mDbHelper != null)
            mDbHelper.close();
	}
 
	
	public boolean Login(String username, String password) throws SQLException{
		
		mDb = mDbHelper.getReadableDatabase();
		Cursor mCursor = mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE user=? AND pass=?", new String[]{username,password});
		if (mCursor != null) {


			if(mCursor.getCount() > 0){
				
				mCursor.close();
				mDb.close();
				return true;
				
			}
            mCursor.close();
		}

		mDb.close();
		return false;
	}
}