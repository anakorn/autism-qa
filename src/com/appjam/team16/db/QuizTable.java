package com.appjam.team16.db;

public class QuizTable {
	
	public static final String TABLE_NAME 				= "Quiz";
	
	public static final String COLUMN_ID 				= "_id";
	public static final String COLUMN_TITLE 			= "title";
	public static final String COLUMN_DESCRIPTION 		= "description";
	public static final String COLUMN_CREATE_TIMESTAMP	= "create_timestamp";
	public static final String COLUMN_MODIFY_TIMESTAMP	= "modify_timestamp";

	public static final String TABLE_CREATE_STATEMENT =
		"CREATE TABLE " + TABLE_NAME + "(" + 
		COLUMN_ID 				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		COLUMN_TITLE 			+ " TEXT, " +
		COLUMN_DESCRIPTION 		+ " TEXT, " +
		COLUMN_CREATE_TIMESTAMP	+ " TEXT NOT NULL, " +
		COLUMN_MODIFY_TIMESTAMP	+ " TEXT NOT NULL" +
		");";
}
