package com.appjam.team16;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.appjam.team16.db.AnswerTable;

public class SeeAnswersActivity extends SherlockFragmentActivity implements
		LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_listview);
		ListView lv = (ListView) findViewById (R.id.simple_list);
		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
				new String [] {AnswerTable.COLUMN_CREATE_TIMESTAMP}, 
				new int [] {android.R.id.text1});
		lv.setAdapter(adapter);
		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.see_answers, menu);
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String [] projection = new String[] {AnswerTable.COLUMN_ID, AnswerTable.COLUMN_CREATE_TIMESTAMP};
		return new CursorLoader(this, Team16ContentProvider.ANSWERS_URI, projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

}
