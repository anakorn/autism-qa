package com.appjam.team16;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.appjam.team16.fragments.QuizDetailFragment;
import com.appjam.team16.fragments.SelectQuestionsDialogFragment.QuestionsSelectedListener;

public class CreateQuizActivity extends FragmentActivity implements
		QuestionsSelectedListener {

	private QuizDetailFragment quizDetailFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_quiz);
		quizDetailFragment = new QuizDetailFragment();
		FragmentTransaction transcation = getSupportFragmentManager()
				.beginTransaction();
		transcation.add(R.id.createQuizFragmentContainer, quizDetailFragment);
		transcation.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
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
		case R.id.home_item:
			intent = new Intent(this, AnswerQuestionActivity.class);
			startActivity(intent);
			break;
		default:
			break;

		}
		return false;

	}

	@Override
	public void questionsSelected(long[] ids) {
		// TODO Auto-generated method stub

	}
}
