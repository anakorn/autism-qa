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

public class DisplayQuestionFragment extends SherlockFragment implements LoaderCallbacks<Cursor>  {
	
	public static final String QUESTION_ID_KEY = "qik";
	
	public interface QuestionButtonListener {
		public void forwardButtonPressed();
		public void backButtonPressed();
	}
	
	private QuestionButtonListener mCallback;
	private Button nextButton;
	private Button prevButton;
	private TextView questionTitle;
	private SeekBar seekBar;
	private long questionId;
	
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View myView = inflater.inflate(R.layout.activity_answer_question, container, false);
		
		return myView;
	}
	
	@Override
	public void onAttach (Activity a) {
		super.onAttach(a);
		try {
			mCallback = (QuestionButtonListener) a;
		} catch (ClassCastException e) {
			throw new ClassCastException (a.toString() + " must implement QuestionButtonListener!");
		}
		if (getArguments() != null && getArguments().containsKey(QUESTION_ID_KEY))
			displayQuestion(getArguments().getInt(QUESTION_ID_KEY));
	}
	
	public void displayQuestion (int questionId) {
		getLoaderManager().initLoader(questionId, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Uri uri = ContentUris.withAppendedId(Team16ContentProvider.QUESTION_URI, arg0);
		String [] projection = new String [] {
			QuestionTable.COLUMN_TITLE,
			QuestionTable.COLUMN_ANSWER_TYPE,
			QuestionTable.COLUMN_QUESTION_AUDIBLE,
			QuestionTable.COLUMN_QUESTION_TEXT
		};
		return new CursorLoader(getActivity(), uri, projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
