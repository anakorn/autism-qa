package com.appjam.team16.db;

public class AnswerTable {
	
	public static final String TABLE_NAME 				= "Answer";
	
	private static final String COLUMN_ID 				= "_id";
	private static final String COLUMN_QUESTION_ID		= "question_id";
	private static final String COLUMN_CREATE_TIMESTAMP	= "create_timestamp";
	private static final String COLUMN_RESPONSE_TIME	= "response_time";
	private static final String COLUMN_ANSWER_VALUE		= "answer_value";
	
	// TODO: Need column types + constraints
	public static final String TABLE_CREATE_STATEMENT =
		"CREATE TABLE " + TABLE_NAME + "(" + 
		COLUMN_ID 				+ "" +
		COLUMN_QUESTION_ID 		+ "" +
		COLUMN_CREATE_TIMESTAMP + "" +
		COLUMN_RESPONSE_TIME 	+ "" +
		COLUMN_ANSWER_VALUE 	+ "" +
		");";
}
