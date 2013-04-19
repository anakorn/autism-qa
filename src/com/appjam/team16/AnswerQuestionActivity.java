package com.appjam.team16;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.db.QuestionTable;
import com.appjam.team16.db.QuizQuestionTable;
import com.appjam.team16.db.QuizTable;
import com.appjam.team16.fragments.DisplayQuestionFragment;
import com.appjam.team16.fragments.DisplayQuestionFragment.QuestionButtonListener;

public class AnswerQuestionActivity extends SherlockFragmentActivity implements
		QuestionButtonListener, LoaderCallbacks<Cursor> {

	public static final String DISPLAY_QUIZ_ID = "dqd";
	public static final String DISPLAY_QUESTION_PREV = "dqp";
	public static final String DISPLAY_QUESTION_NEXT = "dqn";

	private long quiz_id;
	private List<Question> questions;
	private DisplayQuestionFragment displayQuestionFragment;

	private static class Question {
		public int answerValue;
		public int timeOn;
		public int questionId;

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_question_layout);
		
		questions = new ArrayList<AnswerQuestionActivity.Question>();
		if (getIntent() != null && getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(DISPLAY_QUIZ_ID)) {
			quiz_id = getIntent().getExtras().getLong(DISPLAY_QUIZ_ID);
			displayQuiz(quiz_id);
		}
		displayQuestionFragment = new DisplayQuestionFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.displayQuestionHolder, displayQuestionFragment);
		transaction.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.answer_question_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			break;
		case R.id.create_question_item:
			intent = new Intent(this, CreateQuestionActivity.class);
			startActivity(intent);
			break;
		case R.id.view_questions_item:
			intent = new Intent(this, ViewQuestionsActivity.class);
			startActivity(intent);
			break;
		case R.id.create_quiz_item:
			intent = new Intent(this, CreateQuizActivity.class);
			startActivity(intent);
			break;
		case R.id.view_quizzes_item:
			intent = new Intent(this, ViewQuizzesActivity.class);
			startActivity(intent);
			break;
		default:
			break;

		}
		return false;
	}

	public void displayQuiz(long quizId) {
		String[] projection = new String[] { QuizTable.COLUMN_TITLE,
				QuizQuestionTable.COLUMN_QUESTION_ID + " as _id",
				QuestionTable.COLUMN_TITLE,
				QuizQuestionTable.COLUMN_QUIZ_POSITION };
		Uri uri = ContentUris.withAppendedId(
				Team16ContentProvider.QUESTION_QUIZZES_URI, quizId);
		getSupportLoaderManager().initLoader((int) quizId, null, this);
	}

	@Override
	public void forwardButtonPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void backButtonPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = new String[] { QuizTable.COLUMN_TITLE,
				QuizQuestionTable.COLUMN_QUESTION_ID + " as _id",
				QuestionTable.COLUMN_TITLE,
				QuizQuestionTable.COLUMN_QUIZ_POSITION };
		Uri uri = ContentUris.withAppendedId(
				Team16ContentProvider.QUESTION_QUIZZES_URI, arg0);
		return new CursorLoader(this, uri, projection, null, null,
				QuizQuestionTable.COLUMN_QUIZ_POSITION);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		if (arg1.moveToFirst()) {
			for (int i = 0; i < arg1.getCount(); i++) {
				Question question = new Question();
				question.questionId = arg1.getInt(arg1
						.getColumnIndexOrThrow(QuestionTable.COLUMN_ID));
				questions.add(question);
			}
		}
		displayQuestion(0);
	}

	private void displayQuestion(int question)
	{
		int questionId = questions.get(question).questionId;
		displayQuestionFragment.displayQuestion(questionId);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

}
