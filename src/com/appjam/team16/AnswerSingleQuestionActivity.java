package com.appjam.team16;

import java.io.IOException;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.db.QuestionTable;

public class AnswerSingleQuestionActivity extends SherlockFragmentActivity
		implements LoaderCallbacks<Cursor> {

	public static final String QUESTION_ID = "qid";

	private long questionId;
	private View buttonContainer;
	private Button audioButton;
	private Button yesButton;
	private Button noButton;
	private SeekBar slider;
	private TextView tv;
	private String filePath;
	private MediaPlayer play;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		play = new MediaPlayer();
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.answer_single_question_layout);
		if (getIntent() != null && getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(QUESTION_ID)) {
			questionId = getIntent().getExtras().getLong(QUESTION_ID);
			loadQuestion(questionId);
		}
		buttonContainer = findViewById(R.id.buttonContainer);
		yesButton = (Button) findViewById(R.id.yesButton);
		noButton = (Button) findViewById(R.id.noButton);
		slider = (SeekBar) findViewById(R.id.seekBar1);
		tv = (TextView) findViewById(R.id.questionDisplay);
		audioButton = (Button) findViewById(R.id.audioButton);
		audioButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				playAudio();
			}
		});
	}

	private void loadQuestion(long questionId) {
		getSupportLoaderManager().initLoader((int) questionId, null, this);
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
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Uri uri = ContentUris.withAppendedId(
				Team16ContentProvider.QUESTION_URI, arg0);
		String[] projection = { QuestionTable.COLUMN_TITLE,
				QuestionTable.COLUMN_QUESTION_TEXT,
				QuestionTable.COLUMN_QUESTION_AUDIBLE,
				QuestionTable.COLUMN_ANSWER_TYPE };
		return new CursorLoader(this, uri, projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		if (arg1.moveToFirst()) {
			String title = arg1.getString(arg1
					.getColumnIndexOrThrow(QuestionTable.COLUMN_TITLE));
			String question = arg1.getString(arg1
					.getColumnIndexOrThrow(QuestionTable.COLUMN_QUESTION_TEXT));
			int answerType = arg1.getInt(arg1
					.getColumnIndexOrThrow(QuestionTable.COLUMN_ANSWER_TYPE));
			String filePath = arg1
					.getString(arg1
							.getColumnIndexOrThrow(QuestionTable.COLUMN_QUESTION_AUDIBLE));
			if (filePath != null && !filePath.equals("")) {
				audioButton.setVisibility(View.VISIBLE);
				this.filePath = filePath;
			} else {
				audioButton.setVisibility(View.GONE);
			}
			setTitle(title);
			tv.setText(question);
			if (answerType == 0) {
				slider.setVisibility(View.VISIBLE);
			} else {
				buttonContainer.setVisibility(View.VISIBLE);
			}

		}
	}

	private void playAudio() {
		releaseMediaPlayer();
		play = new MediaPlayer();
		try {
			play.setDataSource(filePath);
			play.prepare();
			play.start();
		} catch (IOException e) {

		}
	}

	private void releaseMediaPlayer() {
		if (play != null) {
			try {
				play.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
