package com.appjam.team16;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class HomeActivity extends SherlockActivity implements
		OnSeekBarChangeListener,
		android.widget.RadioGroup.OnCheckedChangeListener, View.OnClickListener {

	private static final int SEEKBAR_MAX = 84;
	private SeekBar seekBar;
	private RadioGroup questionType;
	private LinearLayout yesNoLayout;
	private Button yesButton;
	private Button noButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setMax(SEEKBAR_MAX);
		yesNoLayout = (LinearLayout) findViewById(R.id.yesNoLayout);
		yesButton = (Button) findViewById(R.id.yesButton);
		noButton = (Button) findViewById(R.id.noButton);

		yesButton.setOnClickListener(this);
		noButton.setOnClickListener(this);

		View customNav = LayoutInflater.from(this).inflate(
				R.layout.actionbar_custom_view, null);
		questionType = (RadioGroup) customNav.findViewById(R.id.radio_nav);
		questionType.setOnCheckedChangeListener(this);

		RadioButton scaleOption = (RadioButton) customNav
				.findViewById(R.id.slider_radio);
		scaleOption.setChecked(true);

		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setCustomView(customNav);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
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

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		Log.d("com.team16.appjam", "Check changed");
		if (arg1 == R.id.yes_no_radio) {
			seekBar.setVisibility(View.GONE);
			yesNoLayout.setVisibility(View.VISIBLE);
		} else {
			seekBar.setVisibility(View.VISIBLE);
			yesNoLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
	}

}
