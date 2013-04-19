package com.appjam.team16;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.fragments.QuestionDetailFragment;
import com.appjam.team16.fragments.QuestionDetailFragment.QuestionCreatedListener;
import com.appjam.team16.fragments.SelectQuizzesDialogFragment.QuizzesSelectedListener;

public class CreateQuestionActivity extends SherlockFragmentActivity implements
		QuestionCreatedListener, QuizzesSelectedListener {

	QuestionDetailFragment questionFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_create_question);
		questionFragment = new QuestionDetailFragment();
		FragmentTransaction transcation = getSupportFragmentManager()
				.beginTransaction();
		transcation.add(R.id.createQuestionFragmentContainer, questionFragment);
		transcation.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.create_question_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.home:
			intent = new Intent(this, HomeActivity.class);
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
	public void questionCreated() {
		Intent viewQuestions = new Intent(this, ViewQuestionsActivity.class);
		startActivity(viewQuestions);
		finish();

	}

	@Override
	public void quizzesSelected(long[] ids) {
		questionFragment.addQuizzes(ids);
	}
}
