package com.appjam.team16.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.appjam.team16.R;
import com.appjam.team16.db.QuestionTable;

public class QuizDetailFragment extends Fragment implements
		LoaderCallbacks<Cursor> {

	public interface QuizCreatedListener {
		public void onQuizCreated();
	}

	private ListView questionsList;
	private EditText title;
	private SimpleCursorAdapter adapter;
	private Button submitQuizButton;
	private Button addQuestionButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View quizView = inflater
				.inflate(R.layout.quiz_detail, container, false);

		questionsList = (ListView) quizView.findViewById(R.id.quizQuestionList);
		title = (EditText) quizView.findViewById(R.id.quizTitle);

		View headerView = inflater.inflate(R.layout.quiz_list_header, null);
		View footerView = inflater.inflate(R.layout.quiz_list_footer, null);
		
		submitQuizButton = (Button) footerView.findViewById(R.id.quizFooterButton);
		addQuestionButton = (Button) headerView.findViewById(R.id.quizHeaderButton);
		
		addQuestionButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addQuestion();
			}
		});

		questionsList.addHeaderView(headerView, null, false);
		questionsList.addFooterView(footerView, null, false);

		String[] from = new String[] { QuestionTable.COLUMN_TITLE };
		int[] to = new int[] { android.R.id.text1 };

		adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, null, from, to);
		questionsList.setAdapter(adapter);

		return quizView;
	}
	
	private void addQuestion()
	{
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	}

}
