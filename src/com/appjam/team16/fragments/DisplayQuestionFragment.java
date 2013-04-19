package com.appjam.team16.fragments;

import java.io.IOException;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.appjam.team16.Answers;
import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.db.QuestionTable;

public class DisplayQuestionFragment extends SherlockFragment implements
		LoaderCallbacks<Cursor> {

	public static final String QUESTION_ID_KEY = "qik";

	public interface QuestionButtonListener {
		public void forwardButtonPressed(Answers answer);

		public void backButtonPressed(Answers answer);

		public void finishButtonPressed(Answers answer);
	}

	private QuestionButtonListener mCallback;
	private Button audioButton;
	private Button nextButton;
	private Button prevButton;
	private Button yesButton;
	private Button noButton;
	private View buttonContainer;
	private TextView questionTitle;
	private SeekBar seekBar;
	private long questionId;
	private String filePath;
	private Answers answer;
	private MediaPlayer play;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myView = inflater.inflate(R.layout.activity_answer_question,
				container, false);
		play = new MediaPlayer();
		nextButton = (Button) myView.findViewById(R.id.nextButton);
		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Button b = (Button) v;
				if (b.getText().equals("Finish"))
					finishButtonPressed();
				else
					nextButtonPressed();
			}
		});
		prevButton = (Button) myView.findViewById(R.id.backButton);
		prevButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				prevButtonPressed();
			}
		});
		yesButton = (Button) myView.findViewById(R.id.yesButton);
		yesButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				yesButtonPressed();
			}
		});
		noButton = (Button) myView.findViewById(R.id.noButton);
		noButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				noButtonPressed();
			}
		});
		buttonContainer = myView.findViewById(R.id.buttonContainer);
		seekBar = (SeekBar) myView.findViewById(R.id.seekBar1);
		questionTitle = (TextView) myView.findViewById(R.id.questionDisplay);
		audioButton = (Button) myView.findViewById(R.id.audioButton);
		audioButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				playAudio();
			}
		});
		return myView;
	}

	private void yesButtonPressed() {
		Log.d("com.team16.appjam", "YesButton");
		yesButton.setPressed(true);
		noButton.setPressed(false);
	}

	private void noButtonPressed() {
		yesButton.setPressed(false);
		noButton.setPressed(true);
	}

	private void nextButtonPressed() {
		mCallback.forwardButtonPressed(answer);
	}

	private void prevButtonPressed() {
		mCallback.backButtonPressed(answer);
	}

	private void finishButtonPressed() {
		mCallback.finishButtonPressed(answer);
	}

	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		try {
			mCallback = (QuestionButtonListener) a;
		} catch (ClassCastException e) {
			throw new ClassCastException(a.toString()
					+ " must implement QuestionButtonListener!");
		}
	}

	public void displayQuestion(int questionId, boolean hasPrev, boolean hasNext) {
		this.questionId = questionId;
		answer = new Answers(questionId);
		answer.setStartTime(System.currentTimeMillis());
		getLoaderManager().initLoader(questionId, null, this);
		if (!hasPrev)
			prevButton.setEnabled(false);
		else
			prevButton.setEnabled(true);

		if (!hasNext)
			nextButton.setText("Finish");
		else
			nextButton.setText(R.string.next_button_text);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Uri uri = ContentUris.withAppendedId(
				Team16ContentProvider.QUESTION_URI, arg0);
		String[] projection = { QuestionTable.COLUMN_TITLE,
				QuestionTable.COLUMN_QUESTION_TEXT,
				QuestionTable.COLUMN_QUESTION_AUDIBLE,
				QuestionTable.COLUMN_ANSWER_TYPE };
		return new CursorLoader(getActivity(), uri, projection, null, null,
				null);
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
			String audioFilepath = arg1
					.getString(arg1
							.getColumnIndexOrThrow(QuestionTable.COLUMN_QUESTION_AUDIBLE));
			getActivity().setTitle(title);
			questionTitle.setText(question);
			if (answerType == 0) {
				answer.setIsSliderQuestion(true);
				seekBar.setVisibility(View.VISIBLE);
				buttonContainer.setVisibility(View.GONE);
			} else {
				answer.setIsSliderQuestion(false);
				buttonContainer.setVisibility(View.VISIBLE);
				seekBar.setVisibility(View.GONE);
			}
			if (audioFilepath != null && !audioFilepath.equals("")) {
				audioButton.setVisibility(View.VISIBLE);
				filePath = audioFilepath;
			} else {
				audioButton.setVisibility(View.GONE);
			}

		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

	private void playAudio() {
		Toast.makeText(getActivity(), "Got into play audio", Toast.LENGTH_SHORT);
		releaseMediaPlayer();
		play = new MediaPlayer();
		try {
			Log.d("com.team16.appjam", filePath);
			Toast.makeText(getActivity(), filePath, Toast.LENGTH_SHORT).show();
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

}
