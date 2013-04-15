package com.appjam.team16;

import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appjam.team16.db.QuestionTable;

public class QuestionDetailFragment extends Fragment implements
		View.OnClickListener {
	public static final String QUESTION_ID = "id"; // used to key bundle values

	public interface QuestionCreatedListener {
		public void questionCreated();
	}
	
	public QuestionCreatedListener mCallback;
	
	private EditText questionName;
	private EditText questionQuestion;
	private Button addQuestionButton;
	private Button recordAudioButton;
	private Button removeAudioButton;
	private Button playbackAudioButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View questionView = inflater.inflate(R.layout.question_detail,
				container, false);

		questionName = (EditText) questionView.findViewById(R.id.questionTitle);
		questionQuestion = (EditText) questionView
				.findViewById(R.id.questionQuestion);
		addQuestionButton = (Button) questionView
				.findViewById(R.id.submitQuestion);
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
	public void onAttach (Activity activity)
	{
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (QuestionCreatedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement QuestionCreatedListener");
		}
	}

	public void displayQuestion(long id) {

	}

	@Override
	public void onClick(View v) 
	{
		Context context = getActivity();
		switch (v.getId())
		{
		case R.id.submitQuestion: 
			Toast.makeText(context, "Submit", Toast.LENGTH_SHORT).show();
			ContentResolver cr = getActivity().getContentResolver();
			ContentValues values = new ContentValues();
			values.put(QuestionTable.COLUMN_TITLE, new Date().toString());
			values.put(QuestionTable.COLUMN_ANSWER_TYPE, "test");
			values.put(QuestionTable.COLUMN_CREATE_TIMESTAMP, System.currentTimeMillis());
			values.put(QuestionTable.COLUMN_MODIFY_TIMESTAMP, System.currentTimeMillis());
			cr.insert(Team16ContentProvider.QUESTION_URI, values);
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
		default: break;
		}
	}

}
