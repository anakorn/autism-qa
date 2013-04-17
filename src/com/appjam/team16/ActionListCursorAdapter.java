package com.appjam.team16;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.appjam.team16.db.QuestionTable;

public class ActionListCursorAdapter extends SimpleCursorAdapter {

	private CursorAdapterListener listener;

	public ActionListCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, CursorAdapterListener listener) {
		super(context, layout, c, from, to);
		this.listener = listener;
	}

	public interface CursorAdapterListener {
		void editButtonClicked(long id);

		void checkboxChecked(long id);

		void checkboxUnchecked(long id);
	}

	static class ViewHolder {
		CheckBox box;
		Button editButton;
		TextView text;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		long t_id = -1;
		String t_title = "";
		if (mCursor.move(position)) {
			t_id = mCursor.getLong(mCursor
					.getColumnIndexOrThrow(QuestionTable.COLUMN_ID));
			t_title = mCursor.getString(mCursor
					.getColumnIndex(QuestionTable.COLUMN_TITLE));
		}
		final long id = t_id;
		final String title = t_title;

		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.action_list_item, null);

			holder = new ViewHolder();

			final CheckBox box = (CheckBox) convertView
					.findViewById(R.id.action_list_checkbox);
			final Button editButton = (Button) convertView
					.findViewById(R.id.action_list_button);

			holder.box = box;
			holder.editButton = editButton;
			holder.text = (TextView) convertView
					.findViewById(R.id.action_list_text);
			convertView.setTag(holder);

			holder.box.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (box.isChecked()) {
						listener.checkboxChecked(id);
					} else {
						listener.checkboxUnchecked(id);
					}
				}
			});
			holder.editButton
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							listener.editButtonClicked(id);
						}
					});
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.text.setText(title);
		return convertView;
	}

}