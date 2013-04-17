package com.appjam.team16;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.fragments.QuizDetailFragment;
import com.appjam.team16.fragments.QuizDetailFragment.OnQuizCreatedListener;
import com.appjam.team16.fragments.SelectQuestionsDialogFragment.QuestionsSelectedListener;

public class CreateQuizActivity extends SherlockFragmentActivity implements
		QuestionsSelectedListener, OnQuizCreatedListener {

	private QuizDetailFragment quizDetailFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_quiz);
		getSupportActionBar().setHomeButtonEnabled(true);
		quizDetailFragment = new QuizDetailFragment();
		FragmentTransaction transcation = getSupportFragmentManager()
				.beginTransaction();
		transcation.add(R.id.createQuizFragmentContainer, quizDetailFragment);
		transcation.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
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
			finish();
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
	public void questionsSelected(long[] ids) 
	{
		quizDetailFragment.addQuestions(ids);
	}

	@Override
	public void onQuizCreated() {
		Intent intent = new Intent(this, ViewQuizzesActivity.class);
		startActivity(intent);
		finish();
		
	}
}
