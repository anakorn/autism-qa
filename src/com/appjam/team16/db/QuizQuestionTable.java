package com.appjam.team16.db;

public class QuizQuestionTable {
	
	public static final String TABLE_NAME 				= "QuizQuestion";
	
	// TODO: Learn2Make quiz_id + question_id the composite primary key
	public static final String COLUMN_QUIZ_ID 			= "quiz_id";
	public static final String COLUMN_QUESTION_ID		= "question_id";
	public static final String COLUMN_QUIZ_POSITION	= "quiz_position";
	
	// TODO: Need column types + constraints
	public static final String TABLE_CREATE_STATEMENT =
		"CREATE TABLE " + TABLE_NAME + "(" + 
		COLUMN_QUIZ_ID 			+ " " +
		COLUMN_QUESTION_ID 		+ " " +
		COLUMN_QUIZ_POSITION + "" +
		");";
}
