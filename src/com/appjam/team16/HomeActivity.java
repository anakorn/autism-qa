package com.appjam.team16;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class HomeActivity extends SherlockActivity implements OnSeekBarChangeListener {

	private static final int SEEKBAR_MAX = 84;
	private SeekBar seekBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_question);
		seekBar = (SeekBar) findViewById (R.id.seekBar1);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setMax(SEEKBAR_MAX);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main_menu, menu);
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
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int progress = seekBar.getProgress();
		if (progress >= 77)
			seekBar.setProgress(84);
		else if (progress >= 63)
			seekBar.setProgress(70);
		else if (progress >= 49)
			seekBar.setProgress(56);
		else if (progress >= 35)
			seekBar.setProgress(42);
		else if (progress >= 21)
			seekBar.setProgress(28);
		else if (progress >= 7)
			seekBar.setProgress(14);
		else
			seekBar.setProgress(0);
	}

}
