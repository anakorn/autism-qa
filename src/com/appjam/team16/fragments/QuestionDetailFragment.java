package com.appjam.team16.fragments;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.appjam.team16.R;
import com.appjam.team16.Team16ContentProvider;
import com.appjam.team16.db.QuestionTable;

public class QuestionDetailFragment extends SherlockFragment implements
		View.OnClickListener, LoaderCallbacks<Cursor> {

	public interface QuestionCreatedListener {
		public void questionCreated();
	}

	public QuestionCreatedListener mCallback;

	private EditText questionName;
	private EditText questionQuestion;
	private ToggleButton questionType;
	private Button addQuestionButton;
	private Button recordAudioButton;
	private Button removeAudioButton;
	private Button playbackAudioButton;
	private String fileName = "";
	private boolean editingQuestion;
	private long id;

	private Set<Long> ids;

	private MediaPlayer play;
	private MediaRecorder record;

	private ToggleButton btnRecord;
	private ToggleButton btnPlay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		ids = new HashSet<Long>();
		View questionView = inflater.inflate(R.layout.question_detail,
				container, false);

		questionType = (ToggleButton) questionView
				.findViewById(R.id.questionType);

		btnRecord = (ToggleButton) questionView.findViewById(R.id.trecord);
		btnPlay = (ToggleButton) questionView.findViewById(R.id.tplay);
		btnRecord.setOnClickListener(new onToggleClicked());
		btnPlay.setOnClickListener(new onToggleClicked());

		questionName = (EditText) questionView
				.findViewById(R.id.question_title);
		questionQuestion = (EditText) questionView
				.findViewById(R.id.question_question);
		addQuestionButton = (Button) questionView
				.findViewById(R.id.save_question_bttn);
		// recordAudioButton = (Button) questionView
		// .findViewById(R.id.recordAudioButton);
		removeAudioButton = (Button) questionView
				.findViewById(R.id.deleteAudioButton);
		// playbackAudioButton = (Button) questionView
		// .findViewById(R.id.playbackAudioButton);

		addQuestionButton.setOnClickListener(this);
		// recordAudioButton.setOnClickListener(this);
		removeAudioButton.setOnClickListener(this);
		// playbackAudioButton.setOnClickListener(this);
		return questionView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.clear();
		inflater.inflate(R.menu.create_question_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
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
			int questionTypeInt = questionType.isChecked() ? 0 : 1;
			if (editingQuestion) {
				Uri uri = ContentUris.withAppendedId(
						Team16ContentProvider.QUESTION_URI, this.id);
				ContentResolver cr = getActivity().getContentResolver();
				ContentValues values = new ContentValues();
				values.put(QuestionTable.COLUMN_TITLE, questionName.getText()
						.toString());
				values.put(QuestionTable.COLUMN_QUESTION_TEXT, questionQuestion
						.getText().toString());
				values.put(QuestionTable.COLUMN_ANSWER_TYPE, questionTypeInt);
				values.put(QuestionTable.COLUMN_MODIFY_TIMESTAMP,
						System.currentTimeMillis());
				cr.update(uri, values, "", null);
				mCallback.questionCreated();
			} else {
				if (valuesFilledIn())
					Toast.makeText(context, "Submit", Toast.LENGTH_SHORT)
							.show();
				ContentResolver cr = getActivity().getContentResolver();
				ContentValues values = new ContentValues();
				values.put(QuestionTable.COLUMN_TITLE, questionName.getText()
						.toString());
				values.put(QuestionTable.COLUMN_QUESTION_TEXT, questionQuestion
						.getText().toString());
				values.put(QuestionTable.COLUMN_ANSWER_TYPE, questionTypeInt);
				values.put(QuestionTable.COLUMN_CREATE_TIMESTAMP,
						System.currentTimeMillis());
				values.put(QuestionTable.COLUMN_MODIFY_TIMESTAMP,
						System.currentTimeMillis());
				Uri uri = cr.insert(Team16ContentProvider.QUESTION_URI, values);
				Log.d("com.team16.appjam", uri.toString());
				mCallback.questionCreated();
			}
			break;

		// case R.id.recordAudioButton:
		// Toast.makeText(context, "Record", Toast.LENGTH_SHORT).show();
		// break;
		//
		// case R.id.deleteAudioButton:
		// Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
		// break;
		//
		// case R.id.playbackAudioButton:
		// Toast.makeText(context, "Playback", Toast.LENGTH_SHORT).show();
		// break;
		// default:
		// break;
		}
	}

	private boolean valuesFilledIn() {
		return true;
	}

	public void displayQuestion(long id) {
		editingQuestion = true;
		this.id = id;
		Log.d("com.team16.appjam", "Got called!");
		getLoaderManager().initLoader((int) id, null, this);
		this.onCreateLoader((int) id, null);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		Uri uri = ContentUris.withAppendedId(
				Team16ContentProvider.QUESTION_URI, id);
		return new CursorLoader(getActivity(), uri, new String[] {
				QuestionTable.COLUMN_TITLE, QuestionTable.COLUMN_QUESTION_TEXT,
				QuestionTable.COLUMN_QUESTION_AUDIBLE,
				QuestionTable.COLUMN_ANSWER_TYPE }, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		Log.d("com.appjam.team16", "Load Finished!");
		if (arg1.moveToFirst()) {
			Log.d("com.appjam.team16", "More shit");
			questionName.setText(arg1.getString(arg1
					.getColumnIndexOrThrow(QuestionTable.COLUMN_TITLE)));
			questionQuestion
					.setText(arg1.getString(arg1
							.getColumnIndexOrThrow(QuestionTable.COLUMN_QUESTION_TEXT)));
			questionType
					.setChecked(arg1.getInt(arg1
							.getColumnIndexOrThrow(QuestionTable.COLUMN_ANSWER_TYPE)) == 0);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	public void addQuizzes(long[] ids) {
		for (long l : ids)
			this.ids.add(l);
	}

	// Event for record, play
	class onToggleClicked implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.trecord:
				try {
					if (((ToggleButton) v).isChecked()) {
						beginRecording();
					} else {
						stopRecording();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.tplay:
				try {
					if (((ToggleButton) v).isChecked()) {
						playRecording();
					} else {
						stopPlayback();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			}

		}
	}

	private String getFilePath() {
		String filePath = Environment.getExternalStorageDirectory().getPath();

		File file = new File(filePath, "MediaRecorderSample");

		if (!file.exists())
			file.mkdirs();

		return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".3gpp");
	}

	public void stopPlayback() {
		if (play != null)
			play.stop();

	}

	public void playRecording() throws Exception {
		releaseMediaPlayer();
		play = new MediaPlayer();
		play.setDataSource(fileName);
		play.prepare();
		play.start();

	}

	public void releaseMediaPlayer() {
		if (play != null) {
			try {
				play.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void stopRecording() throws IllegalStateException {
		if (record != null) {
			record.stop();

		}

	}

	public void beginRecording() throws Exception {
		releaseRecorder();
		File outFile = new File(fileName);
		if (outFile.exists())
			outFile.delete();
		// String state = Environment.getExternalStorageState();
		// if(Environment.MEDIA_MOUNTED.equals(state))
		// mediaRecorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
		// + "/video.mp4");
		// else
		// mediaRecorder.setOutputFile(getFilesDir().getAbsolutePath() +
		// "/video.mp4");
		record = new MediaRecorder();
		record.setAudioSource(MediaRecorder.AudioSource.MIC);
		record.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		record.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		fileName = getFilePath();
		record.setOutputFile(fileName);
		record.prepare();
		record.start();

	}

	public void releaseRecorder() {
		if (record != null) {
			record.release();
		}

	}

}
