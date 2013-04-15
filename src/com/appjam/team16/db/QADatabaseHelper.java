package com.appjam.team16.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class QADatabaseHelper extends SQLiteOpenHelper {

	private static final String	DB_NAME = "QA.db";
	private static final int	DB_VERSION = 1;
	
	// SQLite statement used to create database, qa.db
	private static final String	DB_CREATE_STATEMENT = 
			QuizTable.TABLE_CREATE_STATEMENT + " " + 
			QuestionTable.TABLE_CREATE_STATEMENT + " " + 
			AnswerTable.TABLE_CREATE_STATEMENT + " " + 
			QuizQuestionTable.TABLE_CREATE_STATEMENT;
	
	public QADatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE_STATEMENT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersionNum, int newVersionNum) {
		Log.w(QADatabaseHelper.class.getName(), "Upgrading database " + DB_NAME +
				" from version " + oldVersionNum + "to version " + newVersionNum +
				". All existing data will be erased.");

		db.execSQL("DROP TABLE IF EXISTS " + QuizTable.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + AnswerTable.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + QuizQuestionTable.TABLE_NAME);
		// TODO: DROP ALL TABLES AND RECREATE TABLES IF POSSIBLE
	}

}
