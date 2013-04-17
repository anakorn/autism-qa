package com.appjam.team16.fragments;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.appjam.team16.ActionListCursorAdapter;
import com.appjam.team16.ActionListCursorAdapter.CursorAdapterListener;
import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.db.QuestionTable;
import com.appjam.team16.db.QuizTable;

public class QuestionListFragment extends ListFragment implements
		LoaderCallbacks<Cursor>, CursorAdapterListener {

	public interface OnQuestionSelectedListener {
		public void onQuestionSelected(long id);
	}

	private OnQuestionSelectedListener mCallback;
	private SimpleCursorAdapter mAdapter;
	private Set<Long> selectedIds;
	private ActionMode mode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selectedIds = new HashSet<Long>();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// We need to use a different list item layout for devices older than
		// Honeycomb
		// int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
		// android.R.layout.simple_list_item_activated_1
		// : android.R.layout.simple_list_item_1;

		int layout = R.layout.action_list_item;
		mAdapter = new ActionListCursorAdapter(getActivity(), layout, null,
				new String[] { QuizTable.COLUMN_TITLE },
				new int[] { R.id.action_list_text }, this);
		setListAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);
	}

	public void refreshQuestions() {
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public void onStart() {
		super.onStart();

		// When in two-pane layout, set the listview to highlight the selected
		// list item
		// (We do this during onStart because at the point the listview is
		// available.)
		if (getFragmentManager().findFragmentById(R.id.questionDetailFragment) != null) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (OnQuestionSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnQuestionSelectedListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Notify the parent activity of selected item
		mCallback.onQuestionSelected(id);

		// Set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = new String[] { QuestionTable.COLUMN_ID,
				QuestionTable.COLUMN_TITLE };
		CursorLoader loader = new CursorLoader(getActivity(),
				Team16ContentProvider.QUESTION_URI, projection, null, null,
				null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}

	private final class EditQuestionActionMode implements ActionMode.Callback {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Used to put dark icons on light action bar

			menu.add("New Quiz").setIcon(android.R.drawable.ic_menu_add)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
			Toast.makeText(getActivity(), "Got click: " + item,
					Toast.LENGTH_SHORT).show();
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
		}
	}

	@Override
	public void editButtonClicked(long id) {
		Log.d("com.team16.appjam", "Edit button clicked");
	}

	@Override
	public void checkboxChecked(long id) {
		selectedIds.add(id);
		if (mode == null)
		{
			SherlockFragmentActivity activity = (SherlockFragmentActivity) getActivity();
			mode = activity.startActionMode(new EditQuestionActionMode());
		}
	}

	@Override
	public void checkboxUnchecked(long id) {
		selectedIds.remove(id);
		if (selectedIds.isEmpty() && mode != null) {
			mode.finish();
			mode = null;
		}
	}

}
