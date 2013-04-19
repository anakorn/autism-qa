package com.appjam.team16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateFormat;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.db.AnswerTable;
import com.appjam.team16.db.QuestionTable;
import com.appjam.team16.db.QuizQuestionTable;
import com.appjam.team16.db.QuizTable;
import com.appjam.team16.fragments.DisplayQuestionFragment;
import com.appjam.team16.fragments.DisplayQuestionFragment.QuestionButtonListener;

public class AnswerQuestionActivity extends SherlockFragmentActivity implements
		QuestionButtonListener, LoaderCallbacks<Cursor> {

	public static final String DISPLAY_QUIZ_ID = "dqd";

	private long quiz_id;
	private List<Question> questions;
	private DisplayQuestionFragment displayQuestionFragment;
	private int currentPosition;
	private Map<Integer, Integer> answerIds; // map from question id to the uri
												// of
												// the answer if it has already
												// been
												// inserted

	private static class Question {
		public int answerValue;
		public int timeOn;
		public int questionId;

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		answerIds = new HashMap<Integer, Integer>();
		setContentView(R.layout.display_question_layout);
		getSupportActionBar().setHomeButtonEnabled(true);
		questions = new ArrayList<AnswerQuestionActivity.Question>();
		if (getIntent() != null && getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(DISPLAY_QUIZ_ID)) {
			quiz_id = getIntent().getExtras().getLong(DISPLAY_QUIZ_ID);
			displayQuiz(quiz_id);
		}
		displayQuestionFragment = new DisplayQuestionFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction
				.replace(R.id.displayQuestionHolder, displayQuestionFragment);
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
		case R.id.see_answers_item:
			intent = new Intent(this, SeeAnswersActivity.class);
			startActivity(intent);
			break;
		default:
			break;

		}
		return false;
	}

	public void displayQuiz(long quizId) {
		getSupportLoaderManager().initLoader((int) quizId, null, this);
	}

	@Override
	public void forwardButtonPressed(Answers answer) {
		currentPosition++;
		displayQuestion();
		storeAnswer(answer);
	}

	@Override
	public void backButtonPressed(Answers answer) {
		currentPosition--;
		displayQuestion();
		storeAnswer(answer);
	}

	public void finishButtonPressed(Answers answer) {
		storeAnswer(answer);
	}

	private void storeAnswer(Answers answer) {
		if (answerIds.containsKey(answer.getQuestionId())) {
			int answerId = answerIds.get(answer.getQuestionId());
			int questionId = (int) answer.getQuestionId();
			int answerValue = answer.isSliderQuestion() ? answer
					.getSeekBarValue() : answer.getYesNo();
			String formattedResponseTime = answer.getTotalTime();
			ContentValues cv = new ContentValues();
			cv.put(AnswerTable.COLUMN_ANSWER_VALUE, answerValue);
			cv.put(AnswerTable.COLUMN_QUESTION_ID, questionId);
			cv.put(AnswerTable.COLUMN_RESPONSE_TIME, formattedResponseTime);
			DateFormat df = new DateFormat();
			String date = df.format("MM-dd hh:mm", new java.util.Date())
					.toString();
			cv.put(AnswerTable.COLUMN_CREATE_TIMESTAMP, date);
			cv.put(AnswerTable.COLUMN_RESPONSE_TIME, System.currentTimeMillis());
			getContentResolver().update(
					ContentUris.withAppendedId(
							Team16ContentProvider.ANSWERS_URI, answerId), cv,
					null, null);
		} else {
			Log.d("com.team16.appjam", "Is answer null " + (answer == null));
			int questionId = (int) answer.getQuestionId();
			int answerValue = answer.isSliderQuestion() ? answer
					.getSeekBarValue() : answer.getYesNo();
			String formattedResponseTime = answer.getTotalTime();
			ContentValues cv = new ContentValues();
			cv.put(AnswerTable.COLUMN_ANSWER_VALUE, answerValue);
			cv.put(AnswerTable.COLUMN_QUESTION_ID, questionId);
			cv.put(AnswerTable.COLUMN_RESPONSE_TIME, formattedResponseTime);
			DateFormat df = new DateFormat();
			String date = df.format("yyyy-MM-dd hh:mm", new java.util.Date())
					.toString();
			cv.put(AnswerTable.COLUMN_CREATE_TIMESTAMP, date);
			Uri uri = getContentResolver().insert(
					Team16ContentProvider.ANSWERS_URI, cv);

			answerIds.put((int) answer.getQuestionId(),
					Integer.parseInt(uri.getPathSegments().get(1)));
		}
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
				arg1.moveToPosition(i);
				Question question = new Question();
				question.questionId = arg1.getInt(arg1
						.getColumnIndexOrThrow(QuestionTable.COLUMN_ID));
				Log.d("com.team16.appjam", "Adding" + question.questionId);
				questions.add(question);
			}
		}
		currentPosition = 0;
		displayQuestion();
	}

	private void displayQuestion() {
		int questionId = questions.get(currentPosition).questionId;
		Log.d("com.team16.appjam", "d: " + currentPosition);
		boolean hasPrev = currentPosition > 0;
		boolean hasNext = currentPosition + 1 < questions.size();
		displayQuestionFragment.displayQuestion(questionId, hasPrev, hasNext);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

}
