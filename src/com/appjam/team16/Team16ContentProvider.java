package com.appjam.team16;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.appjam.team16.db.AnswerTable;
import com.appjam.team16.db.QuestionTable;
import com.appjam.team16.db.QuizQuestionTable;
import com.appjam.team16.db.QuizTable;

public class Team16ContentProvider extends ContentProvider {

	public static final String AUTHORITY = "com.appjam.team16.team16contentprovider";
	public static final Uri QUESTION_URI = Uri.parse("content://" + AUTHORITY
			+ "/questions");
	public static final Uri QUIZZES_URI = Uri.parse("content://" + AUTHORITY
			+ "/quizzes");
	public static final Uri QUESTION_QUIZZES_URI = Uri.parse("content://"
			+ AUTHORITY + "/questionQuiz");
	public static final Uri ANSWERS_URI = Uri.parse("content://" + AUTHORITY
			+ "/answers");

	private static final int QUESTIONS = 1;
	private static final int QUESTION_ID = 2;
	private static final int QUIZZES = 3;
	private static final int QUIZ_ID = 4;
	private static final int QUESTION_QUIZZES = 5;
	private static final int QUESTION_QUIZ_ID = 6;
	private static final int ANSWER_ID = 7;
	private static final int ANSWER = 8;
	
	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "questions", QUESTIONS);
		uriMatcher.addURI(AUTHORITY, "questions/#", QUESTION_ID);
		uriMatcher.addURI(AUTHORITY, "quizzes", QUIZZES);
		uriMatcher.addURI(AUTHORITY, "quizzes/#", QUIZ_ID);
		uriMatcher.addURI(AUTHORITY, "questionQuiz", QUESTION_QUIZZES);
		uriMatcher.addURI(AUTHORITY, "questionQuiz/#", QUESTION_QUIZ_ID);
		uriMatcher.addURI(AUTHORITY, "answers/#", ANSWER_ID);
		uriMatcher.addURI(AUTHORITY, "answers", ANSWER);

	}

	private QADatabaseHelper myOpenHelper;

	@Override
	public boolean onCreate() {
		Context context = getContext();
		myOpenHelper = new QADatabaseHelper(context, QADatabaseHelper.DB_NAME,
				null, QADatabaseHelper.DB_VERSION);
		return true;
	}

	// This method probably won't do shit since we're not exporting this apps
	// data but anyway
	// Fuck it; that line was also over 80 characters
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case QUIZZES:
			return "vnd.android.cursor.dir/vnd.team16.quizzes";
		case QUIZ_ID:
			return "vnd.android.cursor.item/vnd.team16.quizzes";
		case QUESTIONS:
			return "vnd.android.curosr.dir/vnd.team16.questions";
		case QUESTION_ID:
			return "vnd.android.cursor.item/vnd.team16.questions";
		case QUESTION_QUIZ_ID:
			return "vnd.android.cursor.dir/vnd.team16.questions";
		case ANSWER_ID:
			return "vnd.android.cursor.dir/vnd.team16.answer";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();

		String groupBy = null;
		String having = null;

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		switch (uriMatcher.match(uri)) {
		case QUESTION_ID:
			queryBuilder.setTables(QuestionTable.TABLE_NAME);
			String questionId = uri.getPathSegments().get(1);
			queryBuilder
					.appendWhere(QuestionTable.COLUMN_ID + "=" + questionId);
			break;
		case QUESTIONS:
			queryBuilder.setTables(QuestionTable.TABLE_NAME);
			break;
		case QUIZ_ID:
			queryBuilder.setTables(QuizTable.TABLE_NAME);
			String quizId = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(QuizTable.COLUMN_ID + "=" + quizId);
			break;
		case QUIZZES:
			queryBuilder.setTables(QuizTable.TABLE_NAME);
			break;
		case QUESTION_QUIZ_ID:
			Log.d("com.team16.appjam", "In QUESTION_QUIZ_ID");
			queryBuilder
					.setTables("("
							+ QuizQuestionTable.TABLE_NAME
							+ " natural join "
							+ QuizTable.TABLE_NAME
							+ ") JOIN Question ON Question._id=QuizQuestion.question_id");
			String quizForiegnKey = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(QuizQuestionTable.TABLE_NAME + "."
					+ QuizQuestionTable.COLUMN_ID + "=" + quizForiegnKey);
			break;
		case QUESTION_QUIZZES:
			queryBuilder.setTables(QuizQuestionTable.TABLE_NAME
					+ " natural join " + QuizTable.TABLE_NAME);
			break;
		case ANSWER_ID:
			queryBuilder.setTables(AnswerTable.TABLE_NAME);
			String questionForiegnKey = uri.getPathSegments().get(1);
			queryBuilder.appendWhereEscapeString(AnswerTable.COLUMN_QUESTION_ID
					+ "=" + questionForiegnKey);
		default:
			break;
		}

		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, groupBy, having, null);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues contentValues) {
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		String nullColumnHack = null;
		String tableName = "";
		Uri tableUri = null;
		switch (uriMatcher.match(uri)) {
		case QUESTIONS:
			tableName = QuestionTable.TABLE_NAME;
			tableUri = QUESTION_URI;
			break;
		case QUIZZES:
			tableName = QuizTable.TABLE_NAME;
			tableUri = QUIZZES_URI;
			break;
		case QUESTION_QUIZZES:
			Log.d("com.team16.appjam", "Fell into question quizzes");
			tableName = QuizQuestionTable.TABLE_NAME;
			tableUri = QUESTION_QUIZZES_URI;
			break;
		case ANSWER:
			Log.d("com.team16.appjam", "Fell into AnswerTable");
			tableName = AnswerTable.TABLE_NAME;
			tableUri = ANSWERS_URI;
			break;
		}

		Log.d("com.team16.appjam", "Inserting into table " + tableName);
		long id = db.insert(tableName, nullColumnHack, contentValues);
		Log.d("com.appjam.team16", "" + id);
		if (id > -1) {
			Uri insertedId = ContentUris.withAppendedId(tableUri, id);
			getContext().getContentResolver().notifyChange(tableUri, null);
			return insertedId;
		} else {
			return null;
		}
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {

		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		String tableName = "";
		String query = "";

		switch (uriMatcher.match(uri)) {
		case QUESTION_ID:
			tableName = QuestionTable.TABLE_NAME;
			String questionId = uri.getPathSegments().get(1);
			query = QuestionTable.COLUMN_ID + "=" + questionId;
			break;
		case QUESTIONS:
			tableName = QuestionTable.TABLE_NAME;
			query = "1=1";
			break;
		case QUIZ_ID:
			tableName = QuizTable.TABLE_NAME;
			String quizId = uri.getPathSegments().get(1);
			query = QuizTable.COLUMN_ID + "=" + quizId;
			break;
		case QUIZZES:
			tableName = QuizTable.TABLE_NAME;
			query = "1=1";
			break;
		case QUESTION_QUIZ_ID:
			tableName = QuizQuestionTable.TABLE_NAME;
			String questionForiegnKey = uri.getPathSegments().get(1);
			query = QuestionTable.COLUMN_ID + "=" + questionForiegnKey;
			break;
		case QUESTION_QUIZZES:
			tableName = QuizQuestionTable.TABLE_NAME;
			query = "1=1";
			break;
		case ANSWER_ID:
			tableName = AnswerTable.TABLE_NAME;
			String questionForiegnKey2 = uri.getPathSegments().get(1);
			query = AnswerTable.COLUMN_QUESTION_ID + "=" + questionForiegnKey2;
		default:
			break;
		}

		int count = db.delete(tableName, query
				+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
				whereArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String where,
			String[] whereArgs) {
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		String tableName = "";
		where = "";
		String groupBy = null;
		String having = null;

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		switch (uriMatcher.match(uri)) {
		case QUESTION_ID:
			tableName = QuestionTable.TABLE_NAME;
			String questionId = uri.getPathSegments().get(1);
			where = QuestionTable.COLUMN_ID + "=" + questionId;
			break;
		case QUESTIONS:
			tableName = QuestionTable.TABLE_NAME;
			break;
		case QUIZ_ID:
			tableName = QuizTable.TABLE_NAME;
			String quizId = uri.getPathSegments().get(1);
			where = QuizTable.COLUMN_ID + "=" + quizId;
			break;
		case QUIZZES:
			tableName = QuizTable.TABLE_NAME;
			break;
		case QUESTION_QUIZ_ID:
			tableName = QuizQuestionTable.TABLE_NAME;
			String quizForiegnKey = uri.getPathSegments().get(1);
			where = QuizQuestionTable.TABLE_NAME + "."
					+ QuizQuestionTable.COLUMN_ID + "=" + quizForiegnKey;
			break;
		case QUESTION_QUIZZES:
			tableName = QuizQuestionTable.TABLE_NAME;
			break;
		case ANSWER_ID:
			tableName = AnswerTable.TABLE_NAME;
			String answerId = uri.getPathSegments().get(1);
			where = AnswerTable.COLUMN_ID + "=" + answerId;
		default:
			break;
		}
		return db.update(tableName, contentValues, where, whereArgs);
	}

	private static class QADatabaseHelper extends SQLiteOpenHelper {

		private static final String DB_NAME = "QA.db";
		private static final int DB_VERSION = 1;

		public QADatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(QuizTable.TABLE_CREATE_STATEMENT);
			db.execSQL(QuestionTable.TABLE_CREATE_STATEMENT);
			db.execSQL(AnswerTable.TABLE_CREATE_STATEMENT);
			db.execSQL(QuizQuestionTable.TABLE_CREATE_STATEMENT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersionNum,
				int newVersionNum) {
			Log.w(QADatabaseHelper.class.getName(), "Upgrading database "
					+ DB_NAME + " from version " + oldVersionNum
					+ "to version " + newVersionNum
					+ ". All existing data will be erased.");

			db.execSQL("DROP TABLE IF EXISTS " + QuizTable.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + AnswerTable.TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + QuizQuestionTable.TABLE_NAME);
			onCreate(db);
		}

	}
}
