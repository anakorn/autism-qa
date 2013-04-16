package com.appjam.team16;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class SelectQuestionsDialog extends DialogFragment
{
	
	private static String ADDED_KEYS = "added";
	private static final String FRAGMENT_TITLE = "Choose Questions";
	
	public static SelectQuestionsDialog newInstance (long[] alreadyAddedQuestionIds)
	{
		SelectQuestionsDialog dialog = new SelectQuestionsDialog();
		Bundle args = new Bundle();
		args.putLongArray(ADDED_KEYS, alreadyAddedQuestionIds);
		dialog.setArguments(args);
		return dialog;
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(FRAGMENT_TITLE);
		//http://stackoverflow.com/questions/10761754/dialogfragment-with-extra-space-below-listview
		return null;
	}
	

}
