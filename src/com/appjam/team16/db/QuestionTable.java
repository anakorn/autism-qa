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
		COLUMN_ID 				+ "" +
		COLUMN_TITLE 			+ "" +
		COLUMN_ANSWER_TYPE 		+ "" +
		COLUMN_QUESTION_TEXT 	+ "" +
		COLUMN_QUESTION_AUDIBLE + "" +
		COLUMN_CREATE_TIMESTAMP	+ "" +
		COLUMN_MODIFY_TIMESTAMP	+ "" +
		");";
}
