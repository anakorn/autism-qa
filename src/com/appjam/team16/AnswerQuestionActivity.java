package com.appjam.team16;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.fragments.DisplayQuestionFragment;
import com.appjam.team16.fragments.DisplayQuestionFragment.QuestionButtonListener;

public class AnswerQuestionActivity extends SherlockActivity implements
		QuestionButtonListener, LoaderCallbacks<Cursor> {

	public static final String DISPLAY_QUIZ_ID = "dqd";

	private long quiz_id;
	private List<Question> questions;
	private DisplayQuestionFragment displayQuestionFragment;

	private static class Question {
		public int answerValue;
		public int timeOn;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_question_layout);
		questions = new ArrayList<AnswerQuestionActivity.Question>();
		if (getIntent() != null && getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(DISPLAY_QUIZ_ID)) {
			quiz_id = getIntent().getExtras().getLong(DISPLAY_QUIZ_ID);
		}

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
