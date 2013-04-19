package com.appjam.team16;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import com.appjam.team16.R;

public class AnswerQuestionActivity extends SherlockActivity implements OnSeekBarChangeListener {

	private static final int SEEKBAR_MAX = 84;

	private SeekBar seekBar;
	private Button noButton = null;
	private Button yesButton = null;
	private Button backButton = null;
	private Button nextButton = null;
	private Button audioButton = null;
	
	private Answers answer = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_question);

		noButton = (Button) findViewById(R.id.noButton);
		yesButton = (Button) findViewById(R.id.yesButton);
		backButton = (Button) findViewById(R.id.backButton);
		nextButton = (Button) findViewById(R.id.nextButton);
		audioButton = (Button) findViewById(R.id.audioButton);
		SeekBar slider = (SeekBar) findViewById(R.id.seekBar1);
		
		answer = new Answers();

		seekBar = (SeekBar) findViewById (R.id.seekBar1);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setMax(SEEKBAR_MAX);
		
		answer.setStartTime(System.currentTimeMillis());
		
		
		noButton.setOnClickListener(
				new OnClickListener() 
				{
					public void onClick(View v) 
					{
						answer.isYesNo(false);
					}
				});
		
		yesButton.setOnClickListener(
				new OnClickListener() 
				{
					public void onClick(View v) 
					{
						answer.isYesNo(true);
					}
				});
		
		nextButton.setOnClickListener(
				new OnClickListener() 
				{
					public void onClick(View v) 
					{
						answer.setEndTime(System.currentTimeMillis());
					}
				});	
		
		backButton.setOnClickListener(
				new OnClickListener() 
				{
					public void onClick(View v) 
					{
						answer.setEndTime(System.currentTimeMillis());
					}
				});	
	}
	
	protected void onPause() {
		answer.setPauseStart(System.currentTimeMillis());
	}
	
	protected void onResume() {
		answer.setPauseEnd(System.currentTimeMillis());
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
		
		answer.setSeekBarValue(seekBar.getProgress());
	}
}
