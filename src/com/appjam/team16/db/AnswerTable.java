package com.appjam.team16.db;

public class AnswerTable {

	public static final String TABLE_NAME = "Answer";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_QUESTION_ID = "question_id";
	public static final String COLUMN_CREATE_TIMESTAMP = "create_timestamp";
	public static final String COLUMN_RESPONSE_TIME = "response_time";
	public static final String COLUMN_ANSWER_VALUE = "answer_value";

	public static final String TABLE_CREATE_STATEMENT = "CREATE TABLE "
			+ TABLE_NAME + "(" + COLUMN_ID + " INTEGER NOT NULL, "
			+ COLUMN_QUESTION_ID + " TEXT NOT NULL, " + COLUMN_CREATE_TIMESTAMP
			+ " TEXT NOT NULL, " + COLUMN_RESPONSE_TIME + " REAL NOT NULL, "
			+ COLUMN_ANSWER_VALUE + " INT NOT NULL, " + "FOREIGN KEY ("
			+ COLUMN_QUESTION_ID + ") REFERENCES " + QuestionTable.TABLE_NAME
			+ "(" + QuestionTable.COLUMN_ID + "), " + "PRIMARY KEY ("
			+ COLUMN_ID + ", " + COLUMN_QUESTION_ID + ")" + ");";
}
