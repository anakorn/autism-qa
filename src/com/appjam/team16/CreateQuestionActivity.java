package com.appjam.team16;

import com.appjam.team16.fragments.QuestionDetailFragment;
import com.appjam.team16.fragments.QuestionDetailFragment.QuestionCreatedListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CreateQuestionActivity extends FragmentActivity implements
		QuestionCreatedListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_question);
		QuestionDetailFragment questionDetailFragment = new QuestionDetailFragment();
		FragmentTransaction transcation = getSupportFragmentManager()
				.beginTransaction();
		transcation.add(R.id.createQuestionFragmentContainer,
				questionDetailFragment);
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
	public void questionCreated() {
		// TODO Auto-generated method stub

	}
}
