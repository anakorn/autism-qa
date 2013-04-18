package com.appjam.team16.fragments;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.db.QuestionTable;
import com.appjam.team16.db.QuizQuestionTable;
import com.appjam.team16.db.QuizTable;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.SimpleDragSortCursorAdapter;

public class QuizDetailFragment extends SherlockFragment implements
		LoaderCallbacks<Cursor> {

	private static final String IDS_KEY = "ids_key";

	public interface OnQuizCreatedListener {
		public void onQuizCreated();
	}

	private OnQuizCreatedListener callback;
	private DragSortListView questionsList;
	private EditText title;
	private SimpleDragSortCursorAdapter adapter;
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

		questionsList = (DragSortListView) quizView.findViewById(R.id.quizQuestionList);
		title = (EditText) quizView.findViewById(R.id.quizTitle);

		DragSortController dragController = new DragSortController(questionsList);
		dragController.setBackgroundColor(Color.argb(64, 0, 0, 0));
		dragController.setDragInitMode(DragSortController.ON_LONG_PRESS);
		
		String[] from = new String[] { QuestionTable.COLUMN_TITLE };
		int[] to = new int[] { android.R.id.text1 };

		adapter = new SimpleDragSortCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, null, from, to);
		questionsList.setAdapter(adapter);
		questionsList.setFloatViewManager(dragController);
		questionsList.setOnTouchListener(dragController);
		questionsList.setDragEnabled(true);

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
		if (getArguments() != null
				&& getArguments().containsKey(QuizTable.COLUMN_ID))
			displayQuiz(getArguments().getLong(QuizTable.COLUMN_ID));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.create_quiz_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_question_to_quiz:
			addQuestionToQuiz();
			return false;
		case R.id.save_quiz:
			saveQuiz();
			return true;
		default:
			return false;
		}
	}

	private void addQuestionToQuiz() {
		long[] selectedIds = new long[ids.size()];
		int counter = 0;
		for (Long l : ids) {
			selectedIds[counter++] = l;
		}

		SelectQuestionsDialogFragment select = SelectQuestionsDialogFragment
				.newInstance(selectedIds);
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
			int quiz_id = Integer.parseInt(row.getPathSegments().get(1));
			Log.d("com.team16.appjam", "Added quiz with id of " + quiz_id);
			
			uri = Team16ContentProvider.QUESTION_QUIZZES_URI;
			cv = new ContentValues();
			int counter = 0;
			for (long id : ids) {
				counter++;
				cv = new ContentValues();
				cv.put(QuizQuestionTable.COLUMN_ID, quiz_id);
				cv.put(QuizQuestionTable.COLUMN_QUESTION_ID, id);
				cv.put(QuizQuestionTable.COLUMN_QUIZ_POSITION, counter);
				Uri newRow = getActivity().getContentResolver().insert(uri, cv);
				if (newRow == null)
					Log.d("com.team16.appjam", "Dead");
			}
			Log.d("com.team16.appjam", "Added " + counter + " questions to quiz");
			ids.clear();

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
			String[] projection = new String[] { QuizTable.COLUMN_ID, QuizTable.COLUMN_TITLE,
					QuizQuestionTable.COLUMN_QUESTION_ID };
			Uri uri = ContentUris.withAppendedId(
					Team16ContentProvider.QUESTION_QUIZZES_URI, id);
			return new CursorLoader(getActivity(), uri, projection, null, null,
					null);
		}
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		Log.d("com.team16.appjam", "Load finished with data cursor size of " + data.getCount());
		adapter.swapCursor(data);
		if (data.moveToFirst())
			if (data.getColumnIndex(QuizTable.COLUMN_TITLE) != -1) {
				data.moveToFirst();
				title.setText(data.getString(data
						.getColumnIndex(QuizTable.COLUMN_TITLE)));
			}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	public void addQuestions(long[] newIds) {
		if (newIds.length > 0) {
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

	public void displayQuiz(long id) {
		Log.d("com.team16.appjam", "Display quiz of id " + id);
		editingQuiz = true;
		getLoaderManager().initLoader((int) id, null, this);
	}

}
