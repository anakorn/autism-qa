package com.appjam.team16;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.appjam.team16.db.QuestionTable;
import com.appjam.team16.fragments.QuestionDetailFragment;
import com.appjam.team16.fragments.QuestionListFragment;
import com.appjam.team16.fragments.QuestionDetailFragment.QuestionCreatedListener;
import com.appjam.team16.fragments.QuestionListFragment.OnQuestionSelectedListener;

public class ViewQuestionsActivity extends FragmentActivity implements
		OnQuestionSelectedListener, QuestionCreatedListener {

	public QuestionListFragment questionList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_questions);

		// we're in single pane view
		if (findViewById(R.id.questionFragmentContainer) != null) {
			// However, if we're being restored from a previous state,
			// then we don't need to do anything and should return or else
			// we could end up with overlapping fragments.
			if (savedInstanceState != null) {
				return;
			}

			// Create an instance of ExampleFragment
			QuestionListFragment listFragment = new QuestionListFragment();
			// In case this activity was started with special instructions from
			// an Intent,
			// pass the Intent's extras to the fragment as arguments
			listFragment.setArguments(getIntent().getExtras());

			// Add the fragment to the 'fragment_container' FrameLayout
			getSupportFragmentManager().beginTransaction()
					.add(R.id.questionFragmentContainer, listFragment).commit();
		} else {
			questionList = (QuestionListFragment) getSupportFragmentManager()
					.findFragmentById(R.id.questionListFragment);
		}
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

	public void onQuestionSelected(long id) {

		// Capture the article fragment from the activity layout
		QuestionDetailFragment detailFrag = (QuestionDetailFragment) getSupportFragmentManager()
				.findFragmentById(R.id.questionDetailFragment);

		if (detailFrag != null) {
			// If article frag is available, we're in two-pane layout...

			// Call a method in the ArticleFragment to update its content
			detailFrag.displayQuestion(id);

		} else {
			// If the frag is not available, we're in the one-pane layout and
			// must swap frags...
			QuestionDetailFragment detailFragment = new QuestionDetailFragment();
			Bundle extras = new Bundle();
			extras.putLong(QuestionTable.COLUMN_ID, id);
			detailFragment.setArguments(extras);

			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.questionFragmentContainer, detailFragment);
			transaction.addToBackStack(null);

			transaction.commit();
		}
	}

	@Override
	public void questionCreated() {
		questionList.refreshQuestions();
	}

}
