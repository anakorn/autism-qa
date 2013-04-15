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
		COLUMN_ID 				+ " " +
		COLUMN_TITLE 			+ " " +
		COLUMN_ANSWER_TYPE 		+ " " +
		COLUMN_QUESTION_TEXT 	+ " " +
		COLUMN_QUESTION_AUDIBLE + " " +
		COLUMN_CREATE_TIMESTAMP	+ " " +
		COLUMN_MODIFY_TIMESTAMP	+ " " +
		");";
}
