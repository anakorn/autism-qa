package com.appjam.team16.db;

public class QuestionTable {
	
	public static final String TABLE_NAME 				= "Question";
	
	private static final String COLUMN_ID 				= "_id";
	private static final String COLUMN_TITLE 			= "title";
	private static final String COLUMN_ANSWER_TYPE 		= "answer_type";
	private static final String COLUMN_QUESTION_TEXT	= "question_text";
	private static final String COLUMN_QUESTION_AUDIBLE	= "question_audible";
	private static final String COLUMN_CREATE_TIMESTAMP	= "create_timestamp";
	private static final String COLUMN_MODIFY_TIMESTAMP	= "modify_timestamp";
	
	// TODO: Need column types + constraints
	public static final String TABLE_CREATE_STATEMENT =
		"CREATE TABLE " + TABLE_NAME + "(" + 
		COLUMN_ID 				+ " INTEGER AUTOINCREMENT NOT NULL" +
		COLUMN_TITLE 			+ " TEXT NOT NULL" +
		COLUMN_ANSWER_TYPE 		+ " INTEGER NOT NULL" +
		COLUMN_QUESTION_TEXT 	+ " TEXT" +
		COLUMN_QUESTION_AUDIBLE + " TEXT" +
		COLUMN_CREATE_TIMESTAMP	+ " TEXT NOT NULL" +
		COLUMN_MODIFY_TIMESTAMP	+ " TEXT NOT NULL" +
		" PRIMARY KEY (" + COLUMN_ID + ")"
		");";
}
