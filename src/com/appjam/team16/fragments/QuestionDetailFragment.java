package com.appjam.team16.fragments;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.db.QuestionTable;

public class QuestionDetailFragment extends SherlockFragment implements
		View.OnClickListener, LoaderCallbacks<Cursor> {

	public interface QuestionCreatedListener {
		public void questionCreated();
	}

	public QuestionCreatedListener mCallback;

	private Button addToQuizButton;

	private EditText questionName;
	private EditText questionQuestion;
	private Button addQuestionButton;
	private Button recordAudioButton;
	private Button removeAudioButton;
	private Button playbackAudioButton;

	private boolean editingQuestion;

	private Set<Long> ids;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ids = new HashSet<Long>();
		View questionView = inflater.inflate(R.layout.question_detail,
				container, false);
		// Start "Add to quiz" button
		addToQuizButton = (Button) questionView
				.findViewById(R.id.add_to_quiz_button);
		addToQuizButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addQuestionToQuiz();
			}
		});

		questionName = (EditText) questionView
				.findViewById(R.id.question_title);
		questionQuestion = (EditText) questionView
				.findViewById(R.id.question_question);
		addQuestionButton = (Button) questionView
				.findViewById(R.id.save_question_bttn);
		recordAudioButton = (Button) questionView
				.findViewById(R.id.recordAudioButton);
		removeAudioButton = (Button) questionView
				.findViewById(R.id.deleteAudioButton);
		playbackAudioButton = (Button) questionView
				.findViewById(R.id.playbackAudioButton);

		addQuestionButton.setOnClickListener(this);
		recordAudioButton.setOnClickListener(this);
		removeAudioButton.setOnClickListener(this);
		playbackAudioButton.setOnClickListener(this);
		return questionView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (QuestionCreatedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement QuestionCreatedListener");
		}
		Log.d("com.team16.appjam", "onAttach");
		if (getArguments() != null
				&& getArguments().containsKey(QuestionTable.COLUMN_ID)) {
			displayQuestion(getArguments().getLong(QuestionTable.COLUMN_ID));
		}
	}

	@Override
	public void onClick(View v) {
		Context context = getActivity();
		switch (v.getId()) {
		case R.id.save_question_bttn:
			if (editingQuestion) {
				Toast.makeText(context, "Editing!", Toast.LENGTH_SHORT).show();
				break;
			}
			if (valuesFilledIn())
				Toast.makeText(context, "Submit", Toast.LENGTH_SHORT).show();
			ContentResolver cr = getActivity().getContentResolver();
			ContentValues values = new ContentValues();
			values.put(QuestionTable.COLUMN_TITLE, questionName.getText()
					.toString());
			values.put(QuestionTable.COLUMN_QUESTION_TEXT, questionQuestion
					.getText().toString());
			values.put(QuestionTable.COLUMN_ANSWER_TYPE, 0);
			values.put(QuestionTable.COLUMN_CREATE_TIMESTAMP,
					System.currentTimeMillis());
			values.put(QuestionTable.COLUMN_MODIFY_TIMESTAMP,
					System.currentTimeMillis());
			Uri uri = cr.insert(Team16ContentProvider.QUESTION_URI, values);
			Log.d("com.team16.appjam", uri.toString());
			mCallback.questionCreated();
			break;

		case R.id.recordAudioButton:
			Toast.makeText(context, "Record", Toast.LENGTH_SHORT).show();
			break;

		case R.id.deleteAudioButton:
			Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
			break;

		case R.id.playbackAudioButton:
			Toast.makeText(context, "Playback", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private void addQuestionToQuiz() {
		long[] selectedIds = new long[ids.size()];
		int counter = 0;
		for (Long l : ids) {
			selectedIds[counter++] = l;
		}

		SelectQuizzesDialogFragment select = SelectQuizzesDialogFragment
				.newInstance(selectedIds);
		select.show(getFragmentManager(), "dialog");
	}

	private boolean valuesFilledIn() {
		return true;
	}

	public void displayQuestion(long id) {
		editingQuestion = true;
		Log.d("com.team16.appjam", "Got called!");
		getLoaderManager().initLoader((int) id, null, this);
		this.onCreateLoader((int) id, null);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		Uri uri = ContentUris.withAppendedId(
				Team16ContentProvider.QUESTION_URI, id);
		return new CursorLoader(getActivity(), uri,
				new String[] { QuestionTable.COLUMN_TITLE }, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		Log.d("com.appjam.team16", "Load Finished!");
		if (arg1.moveToFirst()) {
			Log.d("com.appjam.team16", "More shit");
			questionName.setText(arg1.getString(arg1
					.getColumnIndexOrThrow(QuestionTable.COLUMN_TITLE)));
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	public void addQuizzes(long[] ids) {
		for (long l: ids)
			this.ids.add(l);
	}

}
