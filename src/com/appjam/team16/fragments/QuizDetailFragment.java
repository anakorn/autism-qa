package com.appjam.team16.fragments;

import com.appjam.team16.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QuizDetailFragment extends Fragment 
{
	
	public interface QuizCreatedListener
	{
		public void onQuizCreated();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View quizView = inflater.inflate(R.layout.quiz_detail,
				container, false);
		return quizView;
	}

}
