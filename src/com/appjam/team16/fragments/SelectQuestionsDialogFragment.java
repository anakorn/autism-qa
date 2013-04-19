package com.appjam.team16.fragments;

import java.util.List;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.db.QuestionTable;

public class SelectQuestionsDialogFragment extends SherlockDialogFragment
		implements LoaderCallbacks<Cursor>, OnItemClickListener {

	public interface QuestionsSelectedListener {
		public void questionsSelected(long[] ids);
	}

	private SimpleCursorAdapter adapter;
	private QuestionsSelectedListener callback;
	private List<Long> ids;

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

		ids = new LinkedList<Long>();
		String[] from = new String[] { QuestionTable.COLUMN_TITLE };
		int[] to = new int[] { R.id.selectable_text };
		adapter = new CustomCursorAdapter(getActivity(),
				R.layout.selectable_list_item, null, from, to);
		getLoaderManager().initLoader(0, null, this);

		View myView = (View) getActivity().getLayoutInflater().inflate(
				R.layout.simple_listview, null);
		
		ListView lv = (ListView) myView.findViewById(R.id.simple_list);
		lv.setOnItemClickListener(this);
		lv.setAdapter(adapter);
		
		//Add the ids we've already selected
		if (getArguments() != null) 
			for (long l : getArguments().getLongArray(ADDED_KEYS)) 
				ids.add(l);

		return new AlertDialog.Builder(getActivity()).setCancelable(true)
				.setTitle(FRAGMENT_TITLE)
				.setPositiveButton(R.string.done, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						positiveButtonClicked();
					}
				}).setNegativeButton(R.string.cancel, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						negativeButtonClicked();
					}
				}).setView(myView).create();
	}

	private void positiveButtonClicked() {
		int counter = 0;
		long[] ids = new long[this.ids.size()];
		for (Long l : this.ids)
			ids[counter++] = l;
		Log.d("com.team16.appjam", this.ids.toString());
		callback.questionsSelected(ids);
	}

	private void negativeButtonClicked() {
		Log.d("com.team16.appjam", "Negative clicked");
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.d("com.team16.appjam", "Got itemClickEvent");
		CheckBox box = (CheckBox) arg1.findViewById(R.id.selectable_checkbox);
		boolean alreadyChecked = box.isChecked();
		if (alreadyChecked)
			ids.remove(arg3);
		else
			ids.add(arg3);
		box.setChecked(!alreadyChecked);
	}

	private class CustomCursorAdapter extends SimpleCursorAdapter {

		public CustomCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
		}

		class ViewHolder {
			CheckBox box;
			TextView text;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			long t_id = -1;
			String t_title = "";
			mCursor.moveToFirst();
			if (mCursor.move(position)) {
				t_id = mCursor.getLong(mCursor
						.getColumnIndexOrThrow(QuestionTable.COLUMN_ID));
				t_title = mCursor.getString(mCursor
						.getColumnIndex(QuestionTable.COLUMN_TITLE));
			}
			final String title = t_title;

			ViewHolder holder = null;
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.selectable_list_item, null);

				holder = new ViewHolder();

				final CheckBox box = (CheckBox) convertView
						.findViewById(R.id.selectable_checkbox);

				holder.box = box;
				holder.text = (TextView) convertView
						.findViewById(R.id.selectable_text);
				if (ids.contains(t_id)) {
					holder.box.setChecked(true);	
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text.setText(title);
			return convertView;
		}

	}

}
