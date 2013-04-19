package com.appjam.team16.fragments;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.ActionListCursorAdapter;
import com.appjam.team16.ActionListCursorAdapter.CursorAdapterListener;
import com.appjam.team16.AnswerQuestionActivity;
import com.appjam.team16.AnswerSingleQuestionActivity;
import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.db.QuizTable;

public class QuizListFragment extends SherlockListFragment implements
		LoaderCallbacks<Cursor>, CursorAdapterListener {
	public interface OnQuizSelectedListener {
		public void onQuizSelected(long id);
	}

	private OnQuizSelectedListener mCallback;
	private ActionMode mMode;
	private Set<Long> selectedIds;
	private ActionListCursorAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// We need to use a different list item layout for devices older than
		// Honeycomb
		int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
				: android.R.layout.simple_list_item_1;

		mAdapter = new ActionListCursorAdapter(getActivity(), layout, null,
				new String[] { QuizTable.COLUMN_TITLE },
				new int[] { R.id.action_list_text }, this,
				QuizTable.COLUMN_TITLE);
		setListAdapter(mAdapter);
	}

	public void refreshQuestions() {
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("com.team16.appjam", "QuizListFragment onStart()");
		// When in two-pane layout, set the listview to highlight the selected
		// list item
		// (We do this during onStart because at the point the listview is
		// available.)
//		if (getFragmentManager().findFragmentById(R.id.questionDetailFragment) != null) {
//			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//		}
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (OnQuizSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnQuizSelectedListener");
		}
		selectedIds = new HashSet<Long>();
	}

	private final class EditQuizActionMode implements ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Used to put dark icons on light action bar

			menu.add("Delete Selected Quizzes")
					.setIcon(android.R.drawable.ic_menu_delete)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			if (item.getTitle().equals("Delete Selected Quizzes")) {
				deleteQuizzes();
				return true;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			Log.d("com.team16.appjam", "Delete");
			int childCount = getListView().getChildCount();
			for (int i = 0; i < childCount; i++) {
				View v = getListView().getChildAt(i);
				CheckBox box = (CheckBox) v
						.findViewById(R.id.action_list_checkbox);
				box.setChecked(false);
			}
			mMode = null;
			selectedIds.clear();
		}
	}

	private void deleteQuizzes() {
		Uri uri = Team16ContentProvider.QUIZZES_URI;
		ContentResolver cr = getActivity().getContentResolver();
		String selection = QuizTable.COLUMN_ID + " in (";
		String[] selectionArgs = new String[selectedIds.size()];
		int counter = 0;
		for (Long l : selectedIds) {
			selection += "?, ";
			selectionArgs[counter++] = "" + l;
		}
		selection = selection.substring(0, selection.length() - 2);
		selection += ")";
		Log.d("com.team16.appjam",
				"" + cr.delete(uri, selection, selectionArgs));
		getLoaderManager().restartLoader(0, null, this);
		if (mMode != null) {
			mMode.finish();
			mMode = null;
		}
		Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = new String[] { QuizTable.COLUMN_ID,
				QuizTable.COLUMN_TITLE };
		CursorLoader loader = new CursorLoader(getActivity(),
				Team16ContentProvider.QUIZZES_URI, projection, null, null, null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		Log.d("com.team16.appjam", "Loaded quiz cursor with " + arg1.getCount());
		mAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}

	@Override
	public void editButtonClicked(long id) {
		// Notify the parent activity of selected item
		mCallback.onQuizSelected(id);
	}

	@Override
	public void checkboxChecked(long id) {
		Log.d("com.team16.appjam", "checked - " + id);
		selectedIds.add(id);
		if (mMode == null) {
			SherlockFragmentActivity activity = (SherlockFragmentActivity) getSherlockActivity();
			mMode = activity.startActionMode(new EditQuizActionMode());
		}
	}

	@Override
	public void checkboxUnchecked(long id) {
		selectedIds.remove(id);
		if (selectedIds.isEmpty() && mMode != null) {
			mMode.finish();
			mMode = null;
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(getActivity(), AnswerQuestionActivity.class);
		Bundle extras = new Bundle();
		extras.putLong(AnswerQuestionActivity.DISPLAY_QUIZ_ID, id);
		i.putExtras(extras);
		getActivity().startActivity(i);
	}
}
