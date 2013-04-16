package com.appjam.team16.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;

import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.R.string;
import com.appjam.team16.db.QuestionTable;

public class SelectQuestionsDialogFragment extends DialogFragment implements
		OnClickListener, LoaderCallbacks<Cursor> {

	public interface QuestionsSelectedListener {
		public void questionsSelected(long[] ids);
	}

	private SimpleCursorAdapter adapter;
	private QuestionsSelectedListener callback;

	private static String ADDED_KEYS = "added";
	private static final String FRAGMENT_TITLE = "Choose Questions";

	public static SelectQuestionsDialogFragment newInstance(
			long[] alreadyAddedQuestionIds) {
		SelectQuestionsDialogFragment dialog = new SelectQuestionsDialogFragment();
		Bundle args = new Bundle();
		args.putLongArray(ADDED_KEYS, alreadyAddedQuestionIds);
		dialog.setArguments(args);
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		String[] from = new String[] { QuestionTable.COLUMN_TITLE };
		int[] to = new int[] { android.R.id.text1 };
		adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, null, from, to);
		getLoaderManager().initLoader(0, null, this);
		return new AlertDialog.Builder(getActivity()).setCancelable(true)
				.setTitle(FRAGMENT_TITLE)
				.setPositiveButton(R.string.done, this)
				.setNegativeButton(R.string.cancel, this)
				.setAdapter(adapter, this).create();
	}
	
	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		try {
			callback = (QuestionsSelectedListener) a;
		} catch (ClassCastException e) {
			throw new ClassCastException(a.toString()
					+ " must implement QuestionSelectedListener");
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = { QuestionTable.COLUMN_TITLE,
				QuestionTable.COLUMN_ID };
		return new CursorLoader(getActivity(),
				Team16ContentProvider.QUESTION_URI, projection, null, null,
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	}

}
