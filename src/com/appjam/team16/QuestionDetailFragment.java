package com.appjam.team16;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QuestionDetailFragment extends Fragment 
{
	public static final String QUESTION_ID = "id"; //used to key bundle values

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View questionView = inflater.inflate(R.layout.question_detail, container, 
				false);
		return questionView;
		
	}
	
	public void displayQuestion (long id)
	{
		
	}

}
