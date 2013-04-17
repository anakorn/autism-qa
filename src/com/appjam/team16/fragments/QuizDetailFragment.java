package com.appjam.team16.fragments;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.db.QuestionTable;
import com.appjam.team16.db.QuizTable;
import com.appjam.team16.fragments.QuizListFragment.OnQuizSelectedListener;

public class QuizDetailFragment extends SherlockFragment implements
		LoaderCallbacks<Cursor> {

	private static final String IDS_KEY = "ids_key";

	public interface OnQuizCreatedListener {
		public void onQuizCreated();
	}

	private OnQuizCreatedListener callback;
	private ListView questionsList;
	private EditText title;
	private SimpleCursorAdapter adapter;
	private Button submitQuizButton;
	private Button addQuestionButton;
	private Set<Long> ids;
	private boolean editingQuiz;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

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
		submitQuizButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveQuiz();
			}
		});
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

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			callback = (OnQuizCreatedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnQuizCreatedListener");
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.create_quiz_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_question_to_quiz:
			return true;
		case R.id.save_quiz:
			return true;
		default:
			return false;
		}
	}

	private void addQuestion() {
		SelectQuestionsDialogFragment select = SelectQuestionsDialogFragment
				.newInstance(new long[] { 1l });
		select.show(getFragmentManager(), "dialog");
	}

	private void saveQuiz() {
		if (!editingQuiz) {
			Log.d("com.team16.appjam", "saveQuiz!");
			Uri uri = Team16ContentProvider.QUIZZES_URI;
			ContentValues cv = new ContentValues();
			cv.put(QuizTable.COLUMN_TITLE, title.getText().toString());
			cv.put(QuizTable.COLUMN_DESCRIPTION, title.getText().toString());
			cv.put(QuizTable.COLUMN_CREATE_TIMESTAMP,
					System.currentTimeMillis());
			cv.put(QuizTable.COLUMN_MODIFY_TIMESTAMP,
					System.currentTimeMillis());
			Uri row = getActivity().getContentResolver().insert(uri, cv);
			int id = Integer.parseInt(row.getPathSegments().get(1));
			callback.onQuizCreated();
		} else
			Toast.makeText(getActivity(), "Editing", Toast.LENGTH_SHORT);
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
		} else {
			return null;
		}
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

	public void displayQuiz(long id) {
		editingQuiz = true;
		getLoaderManager().initLoader((int) id, null, this);
	}

}
