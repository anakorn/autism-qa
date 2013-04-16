package com.appjam.team16;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CreateQuizActivity extends Activity {

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
}
