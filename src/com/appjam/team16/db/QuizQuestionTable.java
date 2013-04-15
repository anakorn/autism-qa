package com.appjam.team16.db;

public class QuizQuestionTable {
	
	public static final String TABLE_NAME 				= "QuizQuestion";
	
	public static final String COLUMN_ID 			= "_id";
	public static final String COLUMN_QUESTION_ID		= "question_id";
	public static final String COLUMN_QUIZ_POSITION	= "quiz_position";
	
	public static final String TABLE_CREATE_STATEMENT =
		"CREATE TABLE " + TABLE_NAME + "(" + 
		COLUMN_ID 			+ " INTEGER NOT NULL " +
		COLUMN_QUESTION_ID 		+ " INTEGER NOT NULL " +
		COLUMN_QUIZ_POSITION + " INTEGER NOT NULL " +
		"FOREIGN KEY (" + COLUMN_ID + ") REFERENCES " + 
			QuizTable.TABLE_NAME + "(" + QuizTable.COLUMN_ID + ") " +
		"FOREIGN KEY (" + COLUMN_QUESTION_ID + ") REFERENCES " +
			QuestionTable.TABLE_NAME + "(" + QuestionTable.COLUMN_ID + ") " +
		"PRIMARY KEY (" + COLUMN_ID + ", " + COLUMN_QUESTION_ID + ")" +
		");";
}
