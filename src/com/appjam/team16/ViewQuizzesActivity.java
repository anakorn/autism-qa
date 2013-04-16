package com.appjam.team16;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.fragments.QuizDetailFragment.QuizCreatedListener;
import com.appjam.team16.fragments.QuizListFragment;
import com.appjam.team16.fragments.QuizListFragment.OnQuizSelectedListener;

public class ViewQuizzesActivity extends SherlockFragmentActivity implements
		OnQuizSelectedListener, QuizCreatedListener {

	public QuizListFragment quizList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_quizzes);

		// we're in single pane view
		if (findViewById(R.id.questionFragmentContainer) != null) {
			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create an instance of ExampleFragment
			QuizListFragment listFragment = new QuizListFragment();
			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			listFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction()
					.add(R.id.questionFragmentContainer, listFragment).commit();
		} else {
			quizList = (QuizListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.quizListFragment);
		}
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
			break;
		case R.id.view_quizzes_item:
			intent = new Intent(this, ViewQuizzesActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;

		}
		return false;
	}

	@Override
	public void onQuizCreated() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onQuizSelected(long id) {
		// TODO Auto-generated method stub

	}

}
