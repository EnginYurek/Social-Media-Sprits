package com.example.uiexercisesplash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Post {

	//tablodaki kolonlarýn  isimleri
	public static final String KEY_ROWID= "_id";
	public static final String KEY_TITLE= "last_title";
	public static final String KEY_DATE= "last_date";
	
	private static final String DATABASE_NAME= "TitleandDatedb"; //table tüm kolon isimlerini içeriyor
	private static final String DATABASE_TABLE= "postTable";
	private static final int DATABASE_VERSION= 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL( "CREATE TABLE " + DATABASE_TABLE + " (" +
			KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			KEY_TITLE + " TEXT NOT NULL, " + 
			KEY_DATE + " TEXT NOT NULL);"			
			);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);	
		}
	}
		
	public Post(Context c){
		ourContext =c;
	}
	public Post open (){
		ourHelper= new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		ourHelper.close();
	}
	public long createEntr(String title, String date) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_TITLE, title);
		cv.put(KEY_DATE, date);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
		
	}
	public String getData(){
		String [] columns = new String[]{KEY_ROWID, KEY_TITLE, KEY_DATE	};
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
		String result = "";
		
		int iRow=c.getColumnIndex(KEY_ROWID);
		int iTitle=c.getColumnIndex(KEY_TITLE);
		int iDate=c.getColumnIndex(KEY_DATE);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result=c.getString(iRow) + " " +c.getString(iTitle) +" "+ c.getString(iDate) +" ";
		}
		
		return result;
	}
	public String getDate(long l) {
		String [] columns = new String[]{KEY_ROWID, KEY_TITLE, KEY_DATE	};

		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID+"="+l,null, null, null, null,null);
		if(c!=null && c.getCount()>0){
			c.moveToFirst();
			String date = c.getString(2); //2 for third column(date column)
			return date;
		}
		return null;
	}
	public String getTitle(long l) {
		String [] columns = new String[]{KEY_ROWID, KEY_TITLE, KEY_DATE	};

		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID+"="+l,null, null, null, null,null);
		if(c!=null&&c.getCount()>0){
			c.moveToFirst();
			String title = c.getString(1); //1 for second column
			return title;
		}
		return null;
	}
	public void updateEntry(long lRow, String editedTitle, String editedDate) {
		ContentValues cvUpdate= new ContentValues();
		cvUpdate.put(KEY_TITLE, editedTitle);
		cvUpdate.put(KEY_DATE, editedDate);
		//cvUpdate.put(KEY_ROWID, 1);
		ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID+ "=" +lRow, null);
		
	}
	public void deleteEntry(long lRow) {
		ourDatabase.delete(DATABASE_TABLE, KEY_ROWID+"="+lRow, null);
		
	}
	
}
