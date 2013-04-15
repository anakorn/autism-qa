package com.appjam.team16.db;

public class QuestionTable {
	
	public static final String TABLE_NAME 				= "Question";
	
	public static final String COLUMN_ID 				= "_id";
	public static final String COLUMN_TITLE 			= "title";
	public static final String COLUMN_ANSWER_TYPE 		= "answer_type";
	public static final String COLUMN_QUESTION_TEXT	= "question_text";
	public static final String COLUMN_QUESTION_AUDIBLE	= "question_audible";
	public static final String COLUMN_CREATE_TIMESTAMP	= "create_timestamp";
	public static final String COLUMN_MODIFY_TIMESTAMP	= "modify_timestamp";
	
	// TODO: Need column types + constraints
	public static final String TABLE_CREATE_STATEMENT =
		"CREATE TABLE " + TABLE_NAME + "(" + 
		COLUMN_ID 				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		COLUMN_TITLE 			+ " TEXT NOT NULL, " +
		COLUMN_ANSWER_TYPE 		+ " INTEGER NOT NULL, " +
		COLUMN_QUESTION_TEXT 	+ " TEXT, " +
		COLUMN_QUESTION_AUDIBLE + " TEXT, " +
		COLUMN_CREATE_TIMESTAMP	+ " TEXT NOT NULL, " +
		COLUMN_MODIFY_TIMESTAMP	+ " TEXT NOT NULL" +
		");";
}
