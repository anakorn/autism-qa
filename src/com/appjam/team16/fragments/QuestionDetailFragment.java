package com.appjam.team16.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
	//DATABASE ALERT
	//dummy quizzes data array for "add to quiz button". You can initialize this by
	//retrieving all the quizzes in the database
	final CharSequence[] quizzes = {"Quiz 1", "Quiz 2", "Quiz 3", "Quiz 4", "Quiz 5"};
	
	//DATABASE ALERT
	//keep track of which item has been chosen. Have to have the same size as the CharSequence[] above
	//each question will have their own version of this variable, initialize this to all False
	//boolean values will become true if the quizzes in their position is checked/selected
	boolean[] checked = {false, false, false, false, false};
	
	//This ArrayList consists of the position of all the items that are checked
	ArrayList<Integer> selectedItems = new ArrayList<Integer>(); 
	
	//this is used to display the quizzes that the question belongs to
	private ListView quizList;

	private EditText questionName;
	private EditText questionQuestion;
	private Button addQuestionButton;
	private Button recordAudioButton;
	private Button removeAudioButton;
	private Button playbackAudioButton;
	
	private boolean editingQuestion;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View questionView = inflater.inflate(R.layout.question_detail,
				container, false);
		// Start "Add to quiz" button 
		addToQuizButton = (Button) questionView
				.findViewById(R.id.add_to_quiz_button);
		addToQuizButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder quizList = new AlertDialog.Builder(
						getActivity());
				// keep track of selected Items
				//mSelectedItems = new ArrayList<Integer>();

				quizList.setTitle("Choose Quiz(es) to add this Question to")
						
						.setMultiChoiceItems(
								quizzes,
								checked,
								new DialogInterface.OnMultiChoiceClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {
										if (isChecked) {
						                       // add checked item into selectedItem list
						                       selectedItems.add(which);
						                       checked[which] = true;
						                   } else if (selectedItems.contains(which)) {
						                       // Else, if the item is already in the array, remove it 
						                       selectedItems.remove(Integer.valueOf(which));
						                       checked[which] = false;
						                   }
										

									}

								})
						.setPositiveButton("Save", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Toast.makeText(getActivity(), "Selection Saved", Toast.LENGTH_SHORT).show();
								
							}
						})
						.setCancelable(false);

				quizList.create();
				quizList.show();
			}
		});
			
		// End "Add to quiz button
		
		//DATABASE ALERT
		// Add chosen quizzes to quizArray using .add()
		// Start Quiz list display
		// This show the quizArray contents to the list (in question_detail.xml)
		
		quizList = (ListView) questionView.findViewById(R.id.list);
		ArrayAdapter<CharSequence> quizArray = new ArrayAdapter<CharSequence>(
				getActivity(), android.R.layout.simple_list_item_1);
		quizList.setAdapter(quizArray);
				
		// End Quiz list display
		
		
		

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
		} catch (ClassCastException e) 
		{
			throw new ClassCastException(activity.toString()
					+ " must implement QuestionCreatedListener");
		}
		Log.d("com.team16.appjam", "onAttach");
		if (getArguments() != null
				&& getArguments().containsKey(QuestionTable.COLUMN_ID))
		{
			displayQuestion(getArguments().getLong(QuestionTable.COLUMN_ID));
		}
	}

	@Override
	public void onClick(View v) {
		Context context = getActivity();
		switch (v.getId()) {
		case R.id.save_question_bttn:
			if (editingQuestion)
			{
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

}
