package com.appjam.team16;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.db.QuestionTable;
import com.appjam.team16.db.QuizTable;
import com.appjam.team16.fragments.QuizDetailFragment.OnQuizCreatedListener;
import com.appjam.team16.fragments.QuestionDetailFragment;
import com.appjam.team16.fragments.QuizDetailFragment;
import com.appjam.team16.fragments.QuizListFragment;
import com.appjam.team16.fragments.QuizListFragment.OnQuizSelectedListener;
import com.appjam.team16.fragments.SelectQuestionsDialogFragment.QuestionsSelectedListener;

public class ViewQuizzesActivity extends SherlockFragmentActivity implements
		OnQuizSelectedListener, OnQuizCreatedListener,
		QuestionsSelectedListener {

	public QuizListFragment quizList;
	private QuizDetailFragment detailFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_quizzes);
		Log.d("com.team16.appjam", "onCreate()!");
		// we're in single pane view
		if (findViewById(R.id.quizFragmentContainer) != null) {
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
					.add(R.id.quizFragmentContainer, listFragment).commit();
		} else {
			quizList = (QuizListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.quizListFragment);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.view_quizzes_menu, menu);
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
		case R.id.create_quiz_item:
			intent = new Intent(this, CreateQuizActivity.class);
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
		default:
			break;

		}
		return false;
	}

	@Override
	public void onQuizCreated() {
		Intent intent = new Intent(this, ViewQuizzesActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onQuizSelected(long id) {
		Log.d("com.team16.appjam", "onQuizSelected");
		// Capture the article fragment from the activity layout
		QuizDetailFragment detailFrag = (QuizDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.quizDetailFragment);
		if (detailFrag != null) {
			// If article frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			detailFrag.displayQuiz(id);

		} else {
			// If the frag is not available, we're in the one-pane layout and
			// must swap frags...
			detailFragment = new QuizDetailFragment();
			Bundle extras = new Bundle();
			extras.putLong(QuizTable.COLUMN_ID, id);
			detailFragment.setArguments(extras);

			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.quizFragmentContainer, detailFragment);
			transaction.addToBackStack(null);

			transaction.commit();
		}
	}

	@Override
	public void questionsSelected(long[] ids) {
		detailFragment.addQuestions(ids);
	}

}
