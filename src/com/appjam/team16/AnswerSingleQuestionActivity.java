package com.appjam.team16;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.appjam.team16.db.QuestionTable;

public class AnswerSingleQuestionActivity extends SherlockFragmentActivity
		implements LoaderCallbacks<Cursor> {

	public static final String QUESTION_ID = "qid";

	private long questionId;
	private View buttonContainer;
	private Button yesButton;
	private Button noButton;
	private SeekBar slider;
	private TextView tv;
	private Button audioButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

	}

	private void loadQuestion(long questionId) {
		getSupportLoaderManager().initLoader((int) questionId, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.answer_single_question, menu);
		return true;
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
			setTitle(title);
			tv.setText(question);
			if (answerType == 0) {
				slider.setVisibility(View.VISIBLE);
			} else {
				buttonContainer.setVisibility(View.VISIBLE);
			}

		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
