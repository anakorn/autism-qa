package com.appjam.team16.fragments;

import java.util.HashSet;
import java.util.Set;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.db.QuestionTable;

public class QuizDetailFragment extends Fragment implements
		LoaderCallbacks<Cursor> {

	private static final String IDS_KEY = "ids_key";

	public interface QuizCreatedListener {
		public void onQuizCreated();
	}

	private ListView questionsList;
	private EditText title;
	private SimpleCursorAdapter adapter;
	private Button submitQuizButton;
	private Button addQuestionButton;
	private Set<Long> ids;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ids = new HashSet<Long>();
		View quizView = inflater
				.inflate(R.layout.quiz_detail, container, false);

		questionsList = (ListView) quizView.findViewById(R.id.quizQuestionList);
		title = (EditText) quizView.findViewById(R.id.quizTitle);

		View headerView = inflater.inflate(R.layout.quiz_list_header, null);
		View footerView = inflater.inflate(R.layout.quiz_list_footer, null);

		submitQuizButton = (Button) footerView
				.findViewById(R.id.quizFooterButton);
		addQuestionButton = (Button) headerView
				.findViewById(R.id.quizHeaderButton);

		addQuestionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addQuestion();
			}
		});

		questionsList.addHeaderView(headerView, null, false);
		questionsList.addFooterView(footerView, null, false);

		String[] from = new String[] { QuestionTable.COLUMN_TITLE };
		int[] to = new int[] { android.R.id.text1 };

		adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, null, from, to);
		questionsList.setAdapter(adapter);

		return quizView;
	}

	private void addQuestion() {
		SelectQuestionsDialogFragment select = SelectQuestionsDialogFragment
				.newInstance(new long[] { 1l });
		select.show(getFragmentManager(), "dialog");
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		if (arg1 != null) {
			// we're dealing with postCreate loader call
			Uri uri = Team16ContentProvider.QUESTION_URI;
			String selection = QuestionTable.COLUMN_ID + " in (";
			long[] ids = arg1.getLongArray(IDS_KEY);
			String[] selectionArgs = new String[ids.length];
			for (int i = 0; i < ids.length; i++) {
				selection += "?, ";
				selectionArgs[i] = "" + ids[i];
			}
			selection = selection.substring(0, selection.length() - 2); // trim
																		// last
																		// comma
																		// and
																		// " "
			selection += ")";
			String[] projection = new String[] { QuestionTable.COLUMN_ID,
					QuestionTable.COLUMN_TITLE };
			return new CursorLoader(getActivity(), uri, projection, selection,
					selectionArgs, null);
		} else
			return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	public void addQuestions(long[] newIds) {
		for (long newId : newIds)
			ids.add(newId);
		long[] idsArray = new long[ids.size()];
		int counter = 0;
		for (long id : ids)
			idsArray[counter++] = id;
		Bundle selectIds = new Bundle();
		selectIds.putLongArray(IDS_KEY, idsArray);
		getLoaderManager().restartLoader(0, selectIds, this);
	}

}
