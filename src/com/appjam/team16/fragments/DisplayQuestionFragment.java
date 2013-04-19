package com.appjam.team16.fragments;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.db.QuestionTable;

public class DisplayQuestionFragment extends SherlockFragment implements
		LoaderCallbacks<Cursor> {

	public static final String QUESTION_ID_KEY = "qik";

	public interface QuestionButtonListener {
		public void forwardButtonPressed();

		public void backButtonPressed();

		public void finishButtonPressed();
	}

	private QuestionButtonListener mCallback;
	private Button nextButton;
	private Button prevButton;
	private Button yesButton;
	private Button noButton;
	private View buttonContainer;
	private TextView questionTitle;
	private SeekBar seekBar;
	private long questionId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myView = inflater.inflate(R.layout.activity_answer_question,
				container, false);
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
		noButton = (Button) myView.findViewById(R.id.noButton);
		buttonContainer = myView.findViewById(R.id.buttonContainer);
		seekBar = (SeekBar) myView.findViewById(R.id.seekBar1);
		questionTitle = (TextView) myView.findViewById(R.id.questionDisplay);

		return myView;
	}

	private void nextButtonPressed() {
		mCallback.forwardButtonPressed();
	}

	private void prevButtonPressed() {
		mCallback.backButtonPressed();
	}

	private void finishButtonPressed() {

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
			getActivity().setTitle(title);
			questionTitle.setText(question);
			if (answerType == 0) {
				seekBar.setVisibility(View.VISIBLE);
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
